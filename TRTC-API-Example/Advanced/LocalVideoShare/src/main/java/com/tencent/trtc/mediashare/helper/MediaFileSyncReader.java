package com.tencent.trtc.mediashare.helper;

import android.content.Context;
import android.media.MediaFormat;
import android.opengl.EGLContext;
import android.util.Log;
import android.widget.Toast;

import com.tencent.trtc.mediashare.helper.basic.Utils;
import com.tencent.trtc.mediashare.helper.reader.decoder.AudioFrameReader;
import com.tencent.trtc.mediashare.helper.reader.decoder.VideoFrameReader;
import com.tencent.trtc.mediashare.helper.reader.exceptions.SetupException;

import java.util.concurrent.CountDownLatch;

import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Help class for live sharing of local media files,
 * used to help developers quickly implement TRTC custom audio/video collection related functions;
 * Mainly includes:
 * - Implement the hard decoding process of audio and video frames of media files such as .mp4 and .mp3;
 * - Implement audio and video alignment and other operations;
 * - Return the decoded data in the form of callback;
 */
public class MediaFileSyncReader {
    private static final String TAG = "TestSendCustomData";

    private final boolean          mWithVideo;
    private       String           mMediaFilePath;
    private       Context          mContext;
    private VideoFrameReader mVideoFrameReader;
    private AudioFrameReader mAudioFrameReader;
    private       boolean          mIsAudioStopped = false;

    public interface AudioFrameReadListener  extends AudioFrameReader.AudioFrameReadListener {
        void onFrameAvailable(byte[] data, int sampleRate, int channel, long timestamp);
    }

    public interface VideoFrameReadListener extends VideoFrameReader.VideoFrameReadListener {
        void onFrameAvailable(EGLContext eglContext, int textureId, int width, int height, long timestamp);
    }

    public MediaFileSyncReader(Context context, String mediaFilePath, boolean withVideo) {
        mWithVideo = withVideo;
        mContext = context.getApplicationContext();
        mMediaFilePath = mediaFilePath;
    }

    /**
     * Start playback of video and audio data.
     *
     * @param audioListener
     * @param videoListener
     */
    public synchronized void start(AudioFrameReadListener audioListener, VideoFrameReadListener videoListener) {
        if (mAudioFrameReader != null || mVideoFrameReader != null) {
            return;
        }

        long duration;
        try {
            // The duration of the loop is based on the audio length and aligned with 20ms.
            MediaFormat mediaFormat = Utils.retrieveMediaFormat(mMediaFilePath, false);
            duration = mediaFormat.getLong(MediaFormat.KEY_DURATION);
            duration = (duration / MILLISECONDS.toMicros(20) + 1) * MILLISECONDS.toMicros(20);
        } catch (SetupException e) {
            Log.e(TAG, "setup failed.", e);
            Toast.makeText(mContext, "打开文件失败!", Toast.LENGTH_LONG).show();
            return;
        }

        CountDownLatch countDownLatch = new CountDownLatch(mWithVideo ? 2 : 1);
        if (mWithVideo) {
            mVideoFrameReader = new VideoFrameReader(mMediaFilePath, MICROSECONDS.toMillis(duration), countDownLatch);
            mVideoFrameReader.setListener(videoListener);
            mVideoFrameReader.start();
        }

        mAudioFrameReader = new AudioFrameReader(mMediaFilePath, MICROSECONDS.toMillis(duration), countDownLatch);
        mAudioFrameReader.setListener(audioListener);
        mAudioFrameReader.start();
        stopAudio(mIsAudioStopped);
    }

    public void stopAudio(boolean stop) {
        mIsAudioStopped = stop;
        if (mAudioFrameReader != null) {
            mAudioFrameReader.enableSend(!mIsAudioStopped);
        }
    }

    public synchronized void stop() {
        if (mVideoFrameReader != null) {
            mVideoFrameReader.stopRead();
            mVideoFrameReader.setListener(null);
            mVideoFrameReader = null;
        }

        if (mAudioFrameReader != null) {
            mAudioFrameReader.stopRead();
            mAudioFrameReader.setListener(null);
            mAudioFrameReader = null;
        }
    }
}
