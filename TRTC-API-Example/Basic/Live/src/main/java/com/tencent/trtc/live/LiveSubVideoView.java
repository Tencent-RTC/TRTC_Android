package com.tencent.trtc.live;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 * Small Image in Interactive Live Video Streaming Room
 *
 * - Use the buttons on the small image to enable/disable audio/video
 */
public class LiveSubVideoView extends FrameLayout {

    private TXCloudVideoView    mSubVideoView;               // [Control] Sub-screen View
    private Button              mButtonMuteAudio;            // [Control] Turn off the sound
    private Button              mButtonMuteVideo;            // [Control] Close video
    private LiveSubViewListener mListener;                   // Mute and notify when video off
    private LinearLayout        mVideoMutedTipsView;         // [Control] Default avatar shown on video close

    public LiveSubVideoView(Context context) {
        super(context);
    }

    /**
     * Live view of auxiliary video.
     *
     * @param context
     * @param attrs
     */
    public LiveSubVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.live_sub_view_layout, this);
        mSubVideoView = findViewById(R.id.live_cloud_view);
        mVideoMutedTipsView = findViewById(R.id.ll_mute_video_default);
        mButtonMuteVideo = findViewById(R.id.btn_remote_mute_video);
        mButtonMuteAudio = findViewById(R.id.btn_remote_mute_audio);
        mButtonMuteAudio.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onMuteRemoteAudioClicked(view);
                }
            }
        });
        mButtonMuteVideo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onMuteRemoteVideoClicked(view);
                }
            }
        });
        mButtonMuteVideo.setVisibility(View.GONE);
        mButtonMuteAudio.setVisibility(View.GONE);
    }

    public LiveSubVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TXCloudVideoView getVideoView() {
        mButtonMuteVideo.setVisibility(View.VISIBLE);
        mButtonMuteAudio.setVisibility(View.VISIBLE);
        return mSubVideoView;
    }

    public LinearLayout getMuteVideoDefault() {
        return mVideoMutedTipsView;
    }

    public void setLiveSubViewListener(LiveSubViewListener listener) {
        mListener = listener;
    }

    public interface LiveSubViewListener {
        void onMuteRemoteAudioClicked(View view);

        void onMuteRemoteVideoClicked(View view);
    }

}
