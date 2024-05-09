package com.tencent.trtc.mediashare.helper.reader.extractor;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.util.Log;

import androidx.annotation.NonNull;

import java.nio.ByteBuffer;

public class RangeExtractorAdvancer extends ExtractorAdvancer {
    private static final String TAG = "RangeExtractorAdvancer";

    protected long mRangeEndUs;
    private   long mFirstFrameTime;
    private   int  mLoopCount = -1;

    public RangeExtractorAdvancer() {
        this(-1);
    }

    /**
     * <p>Construct an Advancer and specify which range of data the file will only read. </p>
     *Note: If the start time is not a keyframe, it will be adjusted to the previous keyframe
     *
     * @param endUs end time
     */
    public RangeExtractorAdvancer(long endUs) {
        mRangeEndUs = endUs;
    }

    @Override
    public void seekTo(long timeUs, boolean isRelativeTime) {
        mMediaExtractor.seekTo(timeUs, MediaExtractor.SEEK_TO_PREVIOUS_SYNC);
        Log.i(TAG, "seekTo timeUs: " + timeUs + ", isRelativeTime: " + isRelativeTime);
    }

    @Override
    public void updateExtractor(MediaExtractor mediaExtractor) {
        super.updateExtractor(mediaExtractor);
        mFirstFrameTime = mMediaExtractor.getSampleTime();
        Log.i(TAG, "first frame time: " + mFirstFrameTime);

        // seek to the previous keyframe at the specified time
        mMediaExtractor.seekTo(mFirstFrameTime, MediaExtractor.SEEK_TO_PREVIOUS_SYNC);
    }

    @Override
    public void readSampleData(MediaCodec.BufferInfo bufferInfo, @NonNull ByteBuffer byteBuf, int offset) {
        if (isInRange()) {
            if (mMediaExtractor.getSampleTime() == mFirstFrameTime) {
                mLoopCount++;
            }

            bufferInfo.size = mMediaExtractor.readSampleData(byteBuf, offset);
            bufferInfo.flags = mMediaExtractor.getSampleFlags();
            bufferInfo.presentationTimeUs = mLoopCount * mRangeEndUs + mMediaExtractor.getSampleTime();
            bufferInfo.offset = offset;
        } else {
            bufferInfo.size = -1;
        }
    }

    @Override
    public boolean advance() {
        return isInRange() && mMediaExtractor.advance();
    }

    @Override
    public long getSampleTime() {
        return mMediaExtractor.getSampleTime();
    }

    protected boolean isInRange() {
        long sampleTime = mMediaExtractor.getSampleTime();
        return (0 <= sampleTime) && (mRangeEndUs == -1 || sampleTime <= mRangeEndUs);
    }
}
