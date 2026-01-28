package com.tencent.trtc.thirdbeauty;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basic.TRTCBaseActivity;
import com.tencent.liteav.TXLiteAVCode;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCCloudListener;
import com.tencent.trtc.debug.GenerateTestUserSig;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
//import com.faceunity.nama.FURenderer;

/**
 * TRTC third-party beauty page
 *The access steps are as follows:
 * - Download the https://github.com/Faceunity/FUTRTCDemoDroid project and add the faceunity module to the project;
 * - If you need to specify the so architecture of the application, you can modify the current module build.gradle:
 * android {
 * // ...
 * defaultConfig {
 * // ...
 * ndk {
 * abiFilters 'armeabi-v7a', 'arm64-v8a'
 * }
 * }
 * }
 * - Initialize the beauty module {@link FURenderer} as needed:
 * FURenderer.setup(getApplicationContext());
 * mFURenderer = new FURenderer.Builder(getApplicationContext())
 * .setCreateEglContext(false)
 * .setInputTextureType(0) //TRTC uses 0 TEXTURE_2D here
 * .setCreateFaceBeauty(true)
 * .build();
 * - TRTC sets {@link TRTCCloud#setLocalVideoProcessListener} callback, please see the API documentation for details
 * {https://liteav.sdk.qcloud.com/doc/api/zh-cn/group__TRTCCloud__android.html#a0b565dc8c77df7fb826f0c45d8ad2d85}
 * - Use third-party beauty to process video data in the {@link TRTCCloudListener.
 * TRTCVideoFrameListener#onProcessVideoFrame} callback method. For details, see the API documentation {
 * https://liteav.sdk.qcloud.com/doc/api/zh-cn/group__TRTCCloudListener__android.html#a22afb08b2a1a18563c7be28c904b166a}
 *Third-Party Beauty Filters
 * The steps are detailed below:
 * - Download FaceUnity at https://github.com/Faceunity/FUTRTCDemoDroid and import it to your project.
 * You can modify `build.gradle` of the current module to specify SO architecture for the app:
 * android {
 * defaultConfig {
 * ndk {
 * abiFilters 'armeabi-v7a', 'arm64-v8a'
 * - Initialize the beauty filter module {@link FURenderer} as needed:
 * FURenderer.setup(getApplicationContext());
 * mFURenderer = new FURenderer.Builder(getApplicationContext())
 * .setCreateEglContext(false)
 * .setInputTextureType(0)    // In TRTC, the parameter is `0` (TEXTURE_2D)
 * .setCreateFaceBeauty(true)
 * .build();
 * - For how to set the callback using {@link TRTCCloud#setLocalVideoProcessListener}, see the API document {https
 * ://liteav.sdk.qcloud.com/doc/api/zh-cn/group__TRTCCloud__android.html#a0b565dc8c77df7fb826f0c45d8ad2d85}.
 * - For how to use third-party beauty filters to process video data in the
 * {@link TRTCCloudListener.TRTCVideoFrameListener#onProcessVideoFrame} callback, see the API document {https
 * ://liteav.sdk.qcloud.com/doc/api/zh-cn/group__TRTCCloudListener__android.html#a22afb08b2a1a18563c7be28c904b166a}.
 */
public class ThirdBeautyFaceUnityActivity extends TRTCBaseActivity implements View.OnClickListener {

    private static final String TAG = "ThirdBeautyFaceUnityActivity";

    private ImageView              mImageBack;
    private TextView               mTextTitle;
    private Button                 mButtonStartPush;
    private EditText               mEditRoomId;
    private EditText               mEditUserId;
    private SeekBar                mSeekBlurLevel;
    private TextView               mTextBlurLevel;
    private TXCloudVideoView       mTXCloudPreviewView;
    private List<TXCloudVideoView> mRemoteVideoList;

    private TRTCCloud    mTRTCCloud;
    private List<String> mRemoteUserIdList;
    private boolean      mStartPushFlag = false;
    //    private FURenderer mFURenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_beauty);

        getSupportActionBar().hide();

        //        FURenderer.setup(getApplicationContext());
        //        mFURenderer = new FURenderer.Builder(getApplicationContext())
        //                .setCreateEglContext(false)
        //                .setInputTextureType(0)   /* TRTC uses 0 TEXTURE_2D*/ here
        //                .setCreateFaceBeauty(true)
        //                .build();

        mTRTCCloud = TRTCCloud.sharedInstance(getApplicationContext());
        mTRTCCloud.setListener(new TRTCCloudImplListener(ThirdBeautyFaceUnityActivity.this));
        if (checkPermission()) {
            initView();
            initData();
        }
    }

    private void enterRoom(String roomId, String userId) {
        TRTCCloudDef.TRTCParams mTRTCParams = new TRTCCloudDef.TRTCParams();
        mTRTCParams.sdkAppId = GenerateTestUserSig.SDKAPPID;
        mTRTCParams.userId = userId;
        mTRTCParams.roomId = Integer.parseInt(roomId);
        mTRTCParams.userSig = GenerateTestUserSig.genTestUserSig(mTRTCParams.userId);
        mTRTCParams.role = TRTCCloudDef.TRTCRoleAnchor;

        mTRTCCloud.startLocalPreview(true, mTXCloudPreviewView);
        mTRTCCloud.startLocalAudio(TRTCCloudDef.TRTC_AUDIO_QUALITY_DEFAULT);
        mTRTCCloud.enterRoom(mTRTCParams, TRTCCloudDef.TRTC_APP_SCENE_LIVE);
    }

    private void exitRoom() {
        if (mTRTCCloud != null) {
            mTRTCCloud.stopAllRemoteView();
            mTRTCCloud.stopLocalAudio();
            mTRTCCloud.stopLocalPreview();
            mTRTCCloud.exitRoom();
        }
    }

    private void destroyRoom() {
        if (mTRTCCloud != null) {
            exitRoom();
            mTRTCCloud.setListener(null);
        }
        mTRTCCloud = null;
        TRTCCloud.destroySharedInstance();
    }

    private void initView() {
        mRemoteUserIdList = new ArrayList<>();
        mRemoteVideoList = new ArrayList<>();

        mImageBack = findViewById(R.id.iv_back);
        mTextTitle = findViewById(R.id.tv_room_number);
        mButtonStartPush = findViewById(R.id.btn_start_push);
        mEditRoomId = findViewById(R.id.et_room_id);
        mEditUserId = findViewById(R.id.et_user_id);
        mSeekBlurLevel = findViewById(R.id.sb_blur_level);
        mTextBlurLevel = findViewById(R.id.tv_blur_level);
        mTXCloudPreviewView = findViewById(R.id.txcvv_main_local);

        mRemoteVideoList.add((TXCloudVideoView) findViewById(R.id.txcvv_video_remote1));
        mRemoteVideoList.add((TXCloudVideoView) findViewById(R.id.txcvv_video_remote2));
        mRemoteVideoList.add((TXCloudVideoView) findViewById(R.id.txcvv_video_remote3));
        mRemoteVideoList.add((TXCloudVideoView) findViewById(R.id.txcvv_video_remote4));
        mRemoteVideoList.add((TXCloudVideoView) findViewById(R.id.txcvv_video_remote5));
        mRemoteVideoList.add((TXCloudVideoView) findViewById(R.id.txcvv_video_remote6));

        mImageBack.setOnClickListener(this);
        mButtonStartPush.setOnClickListener(this);

        String time = String.valueOf(System.currentTimeMillis());
        String userId = time.substring(time.length() - 8);
        mEditUserId.setText(userId);
        mTextTitle.setText(getString(R.string.thirdbeauty_room_id) + ":" + mEditRoomId.getText().toString());
    }

    private void initData() {
        //                  1. Set the TRTCVideoFrameListener callback, see the API documentation for details {
        //https://liteav.sdk.qcloud.com/doc/api/zh-cn/group__TRTCCloud__android.html#a0b565dc8c77df7fb826f0c45d8ad2d85}
        //        mTRTCCloud.setLocalVideoProcessListener(TRTCCloudDef.TRTC_VIDEO_PIXEL_FORMAT_Texture_2D,
        //        TRTCCloudDef.TRTC_VIDEO_BUFFER_TYPE_TEXTURE, new TRTCCloudListener.TRTCVideoFrameListener() {
        //            @Override
        //            public void onGLContextCreated() {
        ////                  2. GLContext creation
        //               mFURenderer.onSurfaceCreated();
        //            }
        //
        //            @Override
        //            public int onProcessVideoFrame(TRTCCloudDef.TRTCVideoFrame srcFrame,
        //            TRTCCloudDef.TRTCVideoFrame dstFrame) {
        ////                  3. Call the third-party beauty module for processing, please refer to the
        //               API documentation for details. {
        //               https://liteav.sdk.qcloud.com/doc/api/zh-cn/
        //               group__TRTCCloudListener__android.html#a22afb08b2a1a18563c7be28c904b166a}
        //               dstFrame.texture.textureId = mFURenderer
        //               .onDrawFrameSingleInput(srcFrame.texture.textureId, srcFrame.width, srcFrame.height);
        //                return 0;
        //            }
        //
        //            @Override
        //            public void onGLContextDestory() {
        ////                   4. GLContext destruction
        //                mFURenderer.onSurfaceDestroyed();
        //            }
        //        });
        //
        //        mSeekBlurLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        //            @Override
        //            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //                if (mStartPushFlag && fromUser) {
        ////                    5. Set the dermabrasion level
        //                    mFURenderer.getFaceBeautyModule().setBlurLevel(seekBar.getProgress() / 9f);
        //                }
        //                mTextBlurLevel.setText(String.valueOf(progress));
        //            }
        //
        //            @Override
        //            public void onStartTrackingTouch(SeekBar seekBar) {
        //
        //            }
        //
        //            @Override
        //            public void onStopTrackingTouch(SeekBar seekBar) {
        //
        //            }
        //        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_back) {
            finish();
        } else if (id == R.id.btn_start_push) {
            String roomId = mEditRoomId.getText().toString();
            String userId = mEditUserId.getText().toString();
            if (!mStartPushFlag) {
                if (!TextUtils.isEmpty(roomId) && !TextUtils.isEmpty(userId)) {
                    mButtonStartPush.setText(R.string.thirdbeauty_stop_push);
                    enterRoom(roomId, userId);
                    mStartPushFlag = true;
                } else {
                    Toast.makeText(ThirdBeautyFaceUnityActivity.this,
                            getString(R.string.thirdbeauty_please_input_roomid_and_userid), Toast.LENGTH_SHORT).show();
                }
            } else {
                mButtonStartPush.setText(R.string.thirdbeauty_start_push);
                exitRoom();
                mStartPushFlag = false;
            }
        }
    }

    protected class TRTCCloudImplListener extends TRTCCloudListener {

        private WeakReference<ThirdBeautyFaceUnityActivity> mContext;

        public TRTCCloudImplListener(ThirdBeautyFaceUnityActivity activity) {
            super();
            mContext = new WeakReference<>(activity);
        }

        @Override
        public void onUserVideoAvailable(String userId, boolean available) {
            if (available) {
                mRemoteUserIdList.add(userId);
            } else {
                if (mRemoteUserIdList.contains(userId)) {
                    mRemoteUserIdList.remove(userId);
                    mTRTCCloud.stopRemoteView(userId, TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
                }
            }
            refreshRemoteVideo();
        }

        private void refreshRemoteVideo() {
            if (mRemoteUserIdList.size() > 0) {
                for (int i = 0; i < mRemoteUserIdList.size() || i < 6; i++) {
                    if (i < mRemoteUserIdList.size() && !TextUtils.isEmpty(mRemoteUserIdList.get(i))) {
                        mRemoteVideoList.get(i).setVisibility(View.VISIBLE);
                        mTRTCCloud.startRemoteView(mRemoteUserIdList.get(i), TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG,
                                mRemoteVideoList.get(i));
                    } else {
                        mRemoteVideoList.get(i).setVisibility(View.GONE);
                    }
                }
            } else {
                for (int i = 0; i < 6; i++) {
                    mRemoteVideoList.get(i).setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onExitRoom(int i) {
            mRemoteUserIdList.clear();
            refreshRemoteVideo();
        }

        @Override
        public void onError(int errCode, String errMsg, Bundle extraInfo) {
            Log.d(TAG, "sdk callback onError");
            ThirdBeautyFaceUnityActivity activity = mContext.get();
            if (activity != null) {
                Toast.makeText(activity, "onError: " + errMsg + "[" + errCode + "]", Toast.LENGTH_SHORT).show();
                if (errCode == TXLiteAVCode.ERR_ROOM_ENTER_FAIL) {
                    activity.exitRoom();
                }
            }
        }
    }

    @Override
    protected void onPermissionGranted() {
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyRoom();
    }
}
