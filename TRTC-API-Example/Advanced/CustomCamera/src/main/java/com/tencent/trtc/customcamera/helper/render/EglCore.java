package com.tencent.trtc.customcamera.helper.render;

import android.annotation.TargetApi;
import android.view.Surface;

/**
 * When eglMakeCurrent, you need to associate the Context and the window (or Surface), so combine the two.
 */
public class EglCore {
    private EGLHelper mEglHelper;

    /**
     * Create an EglCore, in which the window is an off-screen Surface and does not share other EGLContext
     *
     * @param width The width of the off-screen Surface
     * @param height The height of the off-screen Surface
     */
    public EglCore(int width, int height) {
        this((android.opengl.EGLContext) null, width, height);
    }

    /**
     * Create an EglCore, the window is the incoming Surface, and do not share other EGLContext
     *
     * @param surface The rendering target of the newly created EGLContext
     */
    public EglCore(Surface surface) {
        this((android.opengl.EGLContext) null, surface);
    }

    /**
     * Create an EglCore and associate it with an off-screen surface
     *
     * @param sharedContext used for shared OpenGL Context, can be null
     * @param width The width of the off-screen surface
     * @param height The height of the off-screen surface
     */
    public EglCore(android.opengl.EGLContext sharedContext, int width, int height) {
        mEglHelper = EGL14Helper.createEGLSurface(null, sharedContext, null, width, height);
    }

    /**
     * Create an EglCore and associate it with an off-screen surface
     *
     * @param sharedContext used for shared OpenGL Context, can be null
     * @param width The width of the off-screen surface
     * @param height The height of the off-screen surface
     */
    public EglCore(javax.microedition.khronos.egl.EGLContext sharedContext, int width, int height) {
        mEglHelper = EGL10Helper.createEGLSurface(null, sharedContext, null, width, height);
    }

    /**
     * Create an EglCore and associate it with the incoming surface
     *
     * @param sharedContext used for shared OpenGL Context, can be null
     * @param surface rendering target
     */
    public EglCore(android.opengl.EGLContext sharedContext, Surface surface) {
        mEglHelper = EGL14Helper.createEGLSurface(null, sharedContext, surface, 0, 0);
    }

    /**
     * Create an EglCore and associate it with the incoming surface
     *
     * @param sharedContext used for shared OpenGL Context, can be null
     * @param surface rendering target
     */
    public EglCore(javax.microedition.khronos.egl.EGLContext sharedContext, Surface surface) {
        mEglHelper = EGL10Helper.createEGLSurface(null, sharedContext, surface, 0, 0);
    }

    public void makeCurrent() {
        mEglHelper.makeCurrent();
    }

    public void unmakeCurrent() {
        mEglHelper.unmakeCurrent();
    }

    public void swapBuffer() {
        mEglHelper.swapBuffers();
    }

    public Object getEglContext() {
        return mEglHelper.getContext();
    }

    public void destroy() {
        mEglHelper.destroy();
        mEglHelper = null;
    }

    @TargetApi(18)
    public void setPresentationTime(long nsecs) {
        if (mEglHelper instanceof EGL14Helper) {
            ((EGL14Helper) mEglHelper).setPresentationTime(nsecs);
        }
    }
}

