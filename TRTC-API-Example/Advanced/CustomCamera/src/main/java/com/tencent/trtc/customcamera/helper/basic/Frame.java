package com.tencent.trtc.customcamera.helper.basic;

import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;

import java.nio.ByteBuffer;

public class Frame {
    /**
     * The data contained in this frame, this buffer may be returned from {@link MediaCodec#getInputBuffers()},
     * Needs to be returned to MediaCodec after use is completed.
     */
    public ByteBuffer buffer;

    /**
     * If {@link Frame#buffer} is returned from another module, this member records its index.
     */
    public int bufferIndex;

    /**
     * Identifies which byte in the cache starts to be valid data.
     */
    public int offset;

    /**
     * Identifies the length of valid data in the cache.
     */
    public int size;

    /**
     * The display time corresponding to this data
     */
    public long presentationTimeUs;

    /**
     * Some flags, see {@link BufferInfo#flags} for details
     */
    public int flags;
}
