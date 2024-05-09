package com.tencent.trtc.mediashare.helper.reader.decoder.pipeline;

/**
 * As a data provider for a certain processing unit
 *
 * @param <T> data type
 */
public interface Provider<T> {
    /**
     * Read a buffer from the provider. If the reading is completed, return null
     */
    T dequeueOutputBuffer();

    /**
     * Return the buffer to the provider so that the buffer can be reused
     */
    void enqueueOutputBuffer(T buffer);
}
