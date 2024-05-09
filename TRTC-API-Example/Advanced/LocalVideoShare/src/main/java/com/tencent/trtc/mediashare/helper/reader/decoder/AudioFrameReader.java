package com.tencent.trtc.mediashare.helper.reader.decoder;

import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.os.SystemClock;
import android.util.Log;

import com.tencent.trtc.mediashare.helper.basic.Frame;
import com.tencent.trtc.mediashare.helper.reader.exceptions.ProcessException;
import com.tencent.trtc.mediashare.helper.reader.exceptions.SetupException;
import com.tencent.trtc.mediashare.helper.reader.extractor.Extractor;
import com.tencent.trtc.mediashare.helper.reader.extractor.ExtractorAdvancer;
import com.tencent.trtc.mediashare.helper.reader.extractor.RangeExtractorAdvancer;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class AudioFrameReader extends BaseReader {
    private static final String TAG            = "AudioFrameReader";
    private static final int    FRAME_DURATION = 20;

    private final String mVideoPath;
    private final long   mLoopDurationMs;

    private Decoder mAudioDecoder;
    private long    mStartTimeMs = -1;
    private int     mSampleRate;
    private int     mChannels;
    private int     mBytesPreMills;

    private          byte[]  mFrameData;
    private          int     mSize;
    private          long    mLastEndTime   = 0;
    private volatile boolean mIsSendEnabled = true;

    private AudioFrameReadListener mListener;

    public interface AudioFrameReadListener {
        void onFrameAvailable(byte[] data, int sampleRate, int channel, long timestamp);
    }

    public AudioFrameReader(String videoPath, long durationMs, CountDownLatch countDownLatch) {
        super(countDownLatch);
        mVideoPath = videoPath;
        mLoopDurationMs = durationMs;
    }

    public void setListener(AudioFrameReadListener listener) {
        mListener = listener;
    }

    public void enableSend(boolean enable) {
        mIsSendEnabled = enable;
    }

    @Override
    protected void setup() throws SetupException {
        ExtractorAdvancer advancer  = new RangeExtractorAdvancer(MILLISECONDS.toMicros(mLoopDurationMs));
        Extractor         extractor = new Extractor(false, mVideoPath, advancer);
        mAudioDecoder = new Decoder(extractor);
        mAudioDecoder.setLooping(true);
        mAudioDecoder.setup();

        MediaFormat mediaFormat = extractor.getMediaFormat();
        mSampleRate = mediaFormat.getInteger(MediaFormat.KEY_SAMPLE_RATE);
        mChannels = mediaFormat.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
        mBytesPreMills = mChannels * 2 * mSampleRate / 1000;
        if (MediaCodecInfo.CodecProfileLevel.AACObjectHE == mediaFormat.getInteger(MediaFormat.KEY_AAC_PROFILE)) {
            mSampleRate *= 2;
        }

        // Only send data of FRAME_DURATION length each time
        mFrameData = new byte[FRAME_DURATION * mBytesPreMills];
        mSize = 0;
    }

    @Override
    protected void processFrame() throws ProcessException {
        if (mStartTimeMs == -1) {
            mStartTimeMs = SystemClock.elapsedRealtime();
        }

        mAudioDecoder.processFrame();
        Frame frame = mAudioDecoder.dequeueOutputBuffer();
        if (frame == null) {
            return;
        }

        // Check whether the timestamp is connected. If not, insert some silent data in the middle.
        long diff = MICROSECONDS.toMillis(frame.presentationTimeUs) - (mLastEndTime + mSize / mBytesPreMills);
        while (diff > 0) {
            Log.v(TAG, "diff: " + diff);
            int zeroCount = (int) Math.min(diff * mBytesPreMills, mFrameData.length - mSize);
            Arrays.fill(mFrameData, mSize, mSize + zeroCount, (byte) 0);
            mSize += zeroCount;
            diff -= zeroCount / mBytesPreMills;
            waitAndSend();
        }

        while (frame.size > 0) {
            int readSize = Math.min(mFrameData.length - mSize, frame.size);
            frame.buffer.position(frame.offset);
            frame.buffer.get(mFrameData, mSize, readSize);
            frame.size -= readSize;
            frame.offset += readSize;
            mSize += readSize;
            waitAndSend();
        }

        mAudioDecoder.enqueueOutputBuffer(frame);
    }

    private void waitAndSend() {
        if (mSize < mFrameData.length) {
            return;
        }

        // Check how far the current frame is from the expected sending time, sleep for this period, and then send again
        long time = SystemClock.elapsedRealtime() - mStartTimeMs;
        if (mLastEndTime > time) {
            try {
                Thread.sleep(mLastEndTime - time);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        AudioFrameReadListener listener = mListener;
        if (listener != null && mIsSendEnabled) {
            mListener.onFrameAvailable(mFrameData, mSampleRate, mChannels, mLastEndTime);
        }

        //The length of each sending is FRAME_DURATION
        mLastEndTime += FRAME_DURATION;
        mSize = 0;
    }

    @Override
    protected void release() {
        if (mAudioDecoder != null) {
            mAudioDecoder.release();
            mAudioDecoder = null;
        }
    }
}
