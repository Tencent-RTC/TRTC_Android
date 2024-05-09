package com.tencent.trtc.mediashare.helper.reader.decoder;

import static com.tencent.trtc.mediashare.helper.render.opengl.OpenGlUtils.NO_TEXTURE;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.opengl.EGLContext;
import android.opengl.GLES20;
import android.util.Log;

import com.tencent.trtc.mediashare.helper.basic.Frame;
import com.tencent.trtc.mediashare.helper.basic.FrameBuffer;
import com.tencent.trtc.mediashare.helper.basic.TextureFrame;
import com.tencent.trtc.mediashare.helper.reader.decoder.pipeline.ProvidedStage;
import com.tencent.trtc.mediashare.helper.reader.exceptions.ProcessException;
import com.tencent.trtc.mediashare.helper.reader.exceptions.SetupException;
import com.tencent.trtc.mediashare.helper.reader.extractor.Extractor;
import com.tencent.trtc.mediashare.helper.reader.extractor.ExtractorAdvancer;
import com.tencent.trtc.mediashare.helper.reader.extractor.RangeExtractorAdvancer;
import com.tencent.trtc.mediashare.helper.render.EglCore;
import com.tencent.trtc.mediashare.helper.render.opengl.GPUImageFilter;
import com.tencent.trtc.mediashare.helper.render.opengl.GPUImageFilterGroup;
import com.tencent.trtc.mediashare.helper.render.opengl.OesInputFilter;
import com.tencent.trtc.mediashare.helper.render.opengl.OpenGlUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;

/**
 * Draw the decoded content to your own FrameBuffer, and then give it as output to the next node
 */
@TargetApi(17)
public class VideoFrameToTexture extends ProvidedStage<TextureFrame>
        implements SurfaceTexture.OnFrameAvailableListener {
    private static final String TAG = "VideoFrameToTexture";
    private static final int STATE_WAIT_INPUT   = 1;
    private static final int STATE_WAIT_TEXTURE = 2;
    private static final int STATE_WAIT_RENDER  = 3;

    private final int mWidth;
    private final int mHeight;

    private final float[]     mTextureTransform = new float[16];
    private final FloatBuffer mGLCubeBuffer;
    private final FloatBuffer mGLTextureBuffer;

    private Decoder             mVideoDecoder;
    private EglCore             mEglCore;
    private SurfaceTexture      mSurfaceTexture;
    private int                 mSurfaceTextureId = NO_TEXTURE;
    private FrameBuffer         mFrameBuffer;
    private OesInputFilter      mOesInputFilter;
    private GPUImageFilterGroup mGpuImageFilterGroup;

    // Identifies whether the FrameBuffer is used externally and cannot be written to at the moment.
    private boolean mFrameBufferIsUnusable = false;
    private int     mState                 = STATE_WAIT_INPUT;

    public VideoFrameToTexture(int width, int height) {
        mWidth = width;
        mHeight = height;

        mGLCubeBuffer =
                ByteBuffer.allocateDirect(OpenGlUtils.CUBE.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mGLCubeBuffer.put(OpenGlUtils.CUBE).position(0);

        mGLTextureBuffer = ByteBuffer.allocateDirect(OpenGlUtils.TEXTURE.length * 4).order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mGLTextureBuffer.put(OpenGlUtils.TEXTURE).position(0);
    }

    public void setFrameProvider(String mVideoPath, long mLoopDurationMs) throws SetupException {
        SurfaceTexture surfaceTexture = getSurfaceTexture();
        ExtractorAdvancer advancer = new RangeExtractorAdvancer(MILLISECONDS.toMicros(mLoopDurationMs));
        Extractor extractor = new Extractor(true, mVideoPath, advancer);
        mVideoDecoder = new Decoder(extractor, surfaceTexture);
        mVideoDecoder.setLooping(true);
        mVideoDecoder.setup();
    }

    @Override
    public void setup() {
        // Create an EGLCore, using an off-screen Surface
        mEglCore = new EglCore(mWidth, mHeight);
        mEglCore.makeCurrent();

        //Create a SurfaceTexture for output to the decoder. This class takes texture id as input.
        mSurfaceTextureId = OpenGlUtils.generateTextureOES();
        mSurfaceTexture = new SurfaceTexture(mSurfaceTextureId);
        mSurfaceTexture.setOnFrameAvailableListener(this);

        //Create a FrameBuffer as output to the outside (cannot be used asynchronously outside)
        mFrameBuffer = new FrameBuffer(mWidth, mHeight);
        mFrameBuffer.initialize();

        mGpuImageFilterGroup = new GPUImageFilterGroup();
        mOesInputFilter = new OesInputFilter();
        mGpuImageFilterGroup.addFilter(mOesInputFilter);
        mGpuImageFilterGroup.addFilter(new GPUImageFilter(true));
        mGpuImageFilterGroup.init();
        mGpuImageFilterGroup.onOutputSizeChanged(mWidth, mHeight);
    }

    public SurfaceTexture getSurfaceTexture() {
        return mSurfaceTexture;
    }

    @Override
    public void processFrame() throws ProcessException {
        mVideoDecoder.processFrame();
        super.processFrame();
        if (mState == STATE_WAIT_INPUT) {
            Frame frame = mVideoDecoder.dequeueOutputBuffer();
            if (frame != null) {
                // After returning the Frame to the Decoder,
                // the Decoder will be triggered to release the buffer and render it to the Decoder's Surface.
                mVideoDecoder.enqueueOutputBuffer(frame);
                mState = STATE_WAIT_TEXTURE;
            }
        } else if (mState == STATE_WAIT_RENDER) {
            renderOesToFrameBuffer();
        }
    }

    private void renderOesToFrameBuffer() {
        if (mFrameBufferIsUnusable) {
            return;
        }

        mSurfaceTexture.updateTexImage();
        mSurfaceTexture.getTransformMatrix(mTextureTransform);
        long timestamp = mSurfaceTexture.getTimestamp() / 1000000;
        Log.d(TAG, "renderOesToFrameBuffer time stamp : " + timestamp);

        mOesInputFilter.setTexutreTransform(mTextureTransform);
        mGpuImageFilterGroup.draw(mSurfaceTextureId, mFrameBuffer.getFrameBufferId(), mGLCubeBuffer, mGLTextureBuffer);

        // Wait for drawing to complete
        GLES20.glFinish();

        TextureFrame textureFrame = new TextureFrame();
        textureFrame.eglContext = (EGLContext) mEglCore.getEglContext();
        textureFrame.textureId = mFrameBuffer.getTextureId();
        textureFrame.width = mWidth;
        textureFrame.height = mHeight;
        textureFrame.timestampMs = timestamp;
        synchronized (this) {
            mWaitOutBuffers.add(textureFrame);
        }

        mState = STATE_WAIT_INPUT;
    }

    @Override
    protected void recycleBuffers(List<TextureFrame> canReuseBuffers) {
        mFrameBufferIsUnusable = false;
    }

    @Override
    public void release() {
        if (mVideoDecoder != null) {
            mVideoDecoder.release();
            mVideoDecoder = null;
        }

        mGpuImageFilterGroup.destroy();
        mGpuImageFilterGroup = null;

        mFrameBuffer.uninitialize();
        mFrameBuffer = null;

        OpenGlUtils.deleteTexture(mSurfaceTextureId);
        mSurfaceTextureId = NO_TEXTURE;
        mSurfaceTexture.release();
        mSurfaceTexture = null;

        mEglCore.unmakeCurrent();
        mEglCore.destroy();
        mEglCore = null;
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        mState = STATE_WAIT_RENDER;
    }
}
