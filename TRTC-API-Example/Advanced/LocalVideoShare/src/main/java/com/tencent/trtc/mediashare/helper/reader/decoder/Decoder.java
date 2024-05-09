package com.tencent.trtc.mediashare.helper.reader.decoder;

import android.graphics.SurfaceTexture;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Build;
import android.util.Log;
import android.view.Surface;

import com.tencent.trtc.mediashare.helper.basic.Frame;
import com.tencent.trtc.mediashare.helper.basic.Utils;
import com.tencent.trtc.mediashare.helper.reader.decoder.pipeline.ProvidedStage;
import com.tencent.trtc.mediashare.helper.reader.exceptions.ProcessException;
import com.tencent.trtc.mediashare.helper.reader.exceptions.SetupException;
import com.tencent.trtc.mediashare.helper.reader.extractor.Extractor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static java.util.concurrent.TimeUnit.MICROSECONDS;

/**
 * Decoding logic
 */
public class Decoder extends ProvidedStage<Frame> {
    private static final String TAG = "Decoder";

    private final Extractor             mExtractor;
    private final Object                mNativeWindow;
    private final MediaCodec.BufferInfo mBufferInfo;

    private MediaCodec mMediaCodec;
    private boolean    mIsLooping = false;

    /**
     * When looping this time, ignore frames whose time is less than this time (need to be used when processing Seek)
     */
    private long mSkipFrameBeforeInThisLoop = 0;

    public Decoder(Extractor extractor) {
        this(extractor, null);
    }

    public Decoder(Extractor extractor, SurfaceTexture surfaceTexture) {
        mExtractor = extractor;
        mNativeWindow = surfaceTexture;
        mBufferInfo = new MediaCodec.BufferInfo();
    }

    public void setLooping(boolean isLooping) {
        mIsLooping = isLooping;
    }

    @Override
    public void setup() throws SetupException {
        Surface outputSurface = null;
        try {
            outputSurface = getOutputSurface(mNativeWindow);
            Log.i(TAG, "output surface: " + outputSurface);
        } catch (Exception e) {
            Log.e(TAG, "get output surface failed.", e);
        }

        mExtractor.setup();
        MediaFormat inputFormat = mExtractor.getMediaFormat();
        String      mimeType    = inputFormat.getString(MediaFormat.KEY_MIME);
        Log.i(TAG, String.format(Locale.ENGLISH, "Decoder[%d] for %s", mExtractor.getTraceIndex(), mimeType));
        try {
            mMediaCodec = MediaCodec.createDecoderByType(mimeType);
            mMediaCodec.configure(inputFormat, outputSurface, null, 0);
            mMediaCodec.start();
        } catch (IOException e) {
            throw new SetupException("configure MediaCodec failed.", e);
        }

        setState(State.SETUPED);
    }

    @Override
    public void processFrame() throws ProcessException {
        try {
            super.processFrame();
            feedDataToMediaCodec();
            drainDecodedFrame();
        } catch (Exception e) {
            throw new ProcessException("decode failed", e);
        }
    }

    @Override
    public void release() {
        mExtractor.release();
        mMediaCodec.stop();
        mMediaCodec.release();
        Log.i(TAG, "released decoder");
    }

    @Override
    protected void recycleBuffers(List<Frame> canReuseBuffers) {
        for (Frame frame : canReuseBuffers) {
            if (mNativeWindow != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mMediaCodec.releaseOutputBuffer(frame.bufferIndex, MICROSECONDS.toNanos(frame.presentationTimeUs));
            } else {
                mMediaCodec.releaseOutputBuffer(frame.bufferIndex, mNativeWindow != null);
            }
            // Log.v(TAG, String.format("[%d] destroy output buffer %d", mExtractor.getTraceIndex(),
            //        frame.presentationTimeUs));
        }
    }

    private Surface getOutputSurface(Object window) throws ExecutionException, InterruptedException {
        if (window == null) {
            return null;
        }

        do {
            if (window instanceof Future) {
                window = ((Future) window).get();
            } else if (window instanceof Surface) {
                return (Surface) window;
            } else if (window instanceof SurfaceTexture) {
                return new Surface((SurfaceTexture) window);
            } else {
                return null;
            }
        } while (true);
    }

    private void drainDecodedFrame() {
        synchronized (this) {
            // Decode too fast, we should wait for consumer.
            if (mWaitOutBuffers.size() >= DEFAULT_FRAME_COUNT) {
                return;
            }
        }

        int decoderStatus = mMediaCodec.dequeueOutputBuffer(mBufferInfo, 0);
        if (decoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
            return;
        }

        if (decoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
            Log.i(TAG, "decoder output buffers changed");
            return;
        }

        if (decoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
            MediaFormat newFormat = mMediaCodec.getOutputFormat();
            Log.i(TAG, "decoder output format changed: " + newFormat);
            return;
        }

        if (decoderStatus < 0) {
            throw new RuntimeException("unexpected result from decoder.dequeueOutputBuffer: " + decoderStatus);
        }

        ByteBuffer buffer;
        // If a higher version machine reads data through getOutputBuffers,
        // it will get an inaccessible ByteBuffer and its data cannot be accessed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            buffer = mMediaCodec.getOutputBuffer(decoderStatus);
        } else {
            buffer = mMediaCodec.getOutputBuffers()[decoderStatus];
        }

        Frame frame = new Frame();
        frame.buffer = buffer;
        frame.bufferIndex = decoderStatus;
        frame.offset = mBufferInfo.offset;
        frame.size = mBufferInfo.size;
        frame.presentationTimeUs = mBufferInfo.presentationTimeUs;
        frame.flags = mBufferInfo.flags;

        // Ignore frames before Seek (also don't ignore EOS frames)
        if (mSkipFrameBeforeInThisLoop > frame.presentationTimeUs && !Utils.hasEosFlag(frame.flags)) {
            mMediaCodec.releaseOutputBuffer(frame.bufferIndex, false);
        } else {
            synchronized (this) {
                mWaitOutBuffers.add(frame);
            }
        }

        if (Utils.hasEosFlag(frame.flags)) {
            setState(State.ALL_DATA_READY);
        }
    }

    private void feedDataToMediaCodec() throws SetupException {
        if (isAllDataReady()) {
            return;
        }

        int inputBufIndex = mMediaCodec.dequeueInputBuffer(0);
        if (inputBufIndex < 0) {
            return;
        }

        ByteBuffer            inputBuf   = mMediaCodec.getInputBuffers()[inputBufIndex];
        MediaCodec.BufferInfo bufferInfo = mExtractor.readSampleData(inputBuf);
        if (mIsLooping && Utils.hasEosFlag(bufferInfo.flags)) {
            mExtractor.restart();
            bufferInfo.set(0, 0, 0, 0);
            mSkipFrameBeforeInThisLoop = 0;
        }

        mMediaCodec.queueInputBuffer(inputBufIndex, bufferInfo.offset, bufferInfo.size,
                bufferInfo.presentationTimeUs, bufferInfo.flags);
    }
}
