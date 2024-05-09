package com.tencent.trtc.customcamera.helper.render;

/**
 * Android has two sets of EGL classes.
 * For convenience of use, they are abstracted and only the following interfaces are provided.
 *
 * @param <T>
 */
public interface EGLHelper<T> {
    /**
     * Returns EGLContext, used to create shared EGLContext, etc.
     */
    T getContext();

    /**
     * Bind EGLContext to the current thread, as well as the draw Surface and read Surface saved in Helper.
     */
    void makeCurrent();

    /**
     * Unbind the EGLContext, draw Surface, and read Surface bound to the current thread.
     */
    void unmakeCurrent();

    /**
     * Brush rendered content onto the bound draw target.
     */
    boolean swapBuffers();

    /**
     * Destroy the created EGLContext and related resources.
     */
    void destroy();
}
