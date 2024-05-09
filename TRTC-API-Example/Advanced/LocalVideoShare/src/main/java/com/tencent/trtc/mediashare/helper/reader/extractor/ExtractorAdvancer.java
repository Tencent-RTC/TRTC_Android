package com.tencent.trtc.mediashare.helper.reader.extractor;

import android.media.MediaCodec;
import android.media.MediaExtractor;

import androidx.annotation.NonNull;

import java.nio.ByteBuffer;

/**
 * Used to control the behavior of Extractor, such as moving forward normally, or only extracting key frames
 */
public abstract class ExtractorAdvancer {
    protected MediaExtractor mMediaExtractor;

    /**
     * Update MediaExtractor
     */
    public void updateExtractor(MediaExtractor mediaExtractor) {
        mMediaExtractor = mediaExtractor;
    }

    /**
     * Jump to the specified position to play
     *
     * @param timeUs specified time
     * @param isRelativeTime whether it is relative time
     */
    public abstract void seekTo(long timeUs, boolean isRelativeTime);

    /**
     * See {@link MediaExtractor#readSampleData(ByteBuffer, int)}
     */
    public abstract void readSampleData(MediaCodec.BufferInfo bufferInfo, @NonNull ByteBuffer byteBuf, int offset);

    /**
     *Next frame data
     */
    public abstract boolean advance();

    /**
     * Get the timestamp of the current frame
     */
    public abstract long getSampleTime();
}
