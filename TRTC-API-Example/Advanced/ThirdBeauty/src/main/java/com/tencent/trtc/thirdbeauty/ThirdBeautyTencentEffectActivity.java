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

/**
 * TRTC Third-Party Beauty Filter View
 * Access steps：
 * First step：
 * Integrate Tencent Effect SDK and copy resources（You can refer to the access document provided by Tencent
 * Effects：https://cloud.tencent.com/document/product/616/65889）
 * Second step：Authentication and initialization of Tencent Effect SDK,
 * see details{@link ThirdBeautyTencentEffectActivity#authXmagic()},to obtain the license, please refer to {https
 * ://cloud.tencent.com/document/product/616/65878}
 * Third step：Using Tencent Effect in TRTC，see details{@link ThirdBeautyTencentEffectActivity#initData()}
 * - For how to set the callback using {@link TRTCCloud#setLocalVideoProcessListener}, see the API document {https
 * ://liteav.sdk.qcloud.com/doc/api/zh-cn/group__TRTCCloud__android.html#a0b565dc8c77df7fb826f0c45d8ad2d85}.
 * - For how to use third-party beauty filters to process video data in the
 * {@link TRTCCloudListener.TRTCVideoFrameListener#onProcessVideoFrame} callback, see the API document {https
 * ://liteav.sdk.qcloud.com/doc/api/zh-cn/group__TRTCCloudListener__android.html#a22afb08b2a1a18563c7be28c904b166a}.
 * Note：The applicationId and License provided by Tencent Effects are in one-to-one correspondence.
 * During the test process, the applicationId needs to be modified to the applicationId corresponding to the License.
 **/
public class ThirdBeautyTencentEffectActivity extends TRTCBaseActivity implements View.OnClickListener {
    private static final String TAG = "TencentEffectActivity";

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
    //    private XmagicApi    mXmagicApi;
    private List<String> mRemoteUserIdList;
    private boolean      mStartPushFlag = false;

    //    private XmagicProperty<XmagicProperty.XmagicPropertyValues> mProperty;
    //    The following parameters are replaced according to the actual application
    //    private final String XMAGIC_LICENSE_URL = "";
    //    private final String XMAGIC_LICENSE_KEY = "";
    //    private final String XMAGIC_RES_PATH    = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_beauty_tencent_effect);
        getSupportActionBar().hide();
        //        authXmagic();
        mTRTCCloud = TRTCCloud.sharedInstance(getApplicationContext());
        mTRTCCloud.setListener(
                new ThirdBeautyTencentEffectActivity.TRTCCloudImplListener(ThirdBeautyTencentEffectActivity.this));
        if (checkPermission()) {
            initView();
            initData();
        }
    }

    //    private void authXmagic() {
    //        TELicenseCheck.getInstance().setTELicense(this,
    //                XMAGIC_LICENSE_URL,
    //                XMAGIC_LICENSE_KEY,
    //                new TELicenseCheck.TELicenseCheckListener() {
    //                    @Override
    //                    public void onLicenseCheckFinish(int errorCode, String msg) {
    //                        //Note: This callback is not necessarily in the calling thread
    //                        if (errorCode == TELicenseCheck.ERROR_OK) {
    //                            initXmagicApi();
    //                        }
    //                    }
    //                });
    //    }

    //    private void initXmagicApi() {
    //        mXmagicApi = new XmagicApi(this, XMAGIC_RES_PATH, new XmagicApi.OnXmagicPropertyErrorListener() {
    //            @Override
    //            public void onXmagicPropertyError(String s, int i) {
    //
    //            }
    //        });
    //        mProperty = new XmagicProperty<>(XmagicProperty.Category.BEAUTY, null, null,
    //                XmagicConstant.BeautyConstant.BEAUTY_SMOOTH,
    //                new XmagicProperty.XmagicPropertyValues(0, 100, 50, 0, 1));
    //    }

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
        //1. Set the TRTCVideoFrameListener callback, see the API documentation for details {https://liteav.sdk.qcloud.com/doc/api/zh-cn/group__TRTCCloud__android.html#a0b565dc8c77df7fb826f0c45d8ad2d85}
        //        mTRTCCloud.setLocalVideoProcessListener(TRTCCloudDef.TRTC_VIDEO_PIXEL_FORMAT_Texture_2D,
        //                TRTCCloudDef.TRTC_VIDEO_BUFFER_TYPE_TEXTURE, new TRTCCloudListener.TRTCVideoFrameListener() {
        //                    @Override
        //                    public void onGLContextCreated() {
        //                    }
        //
        //                    @Override
        //                    public int onProcessVideoFrame(TRTCCloudDef.TRTCVideoFrame trtcVideoFrame,
        //                                                   TRTCCloudDef.TRTCVideoFrame trtcVideoFrame1) {
        //                        //2. Call the third-party beauty module for processing, please refer to the API documentation for details. {https://liteav.sdk.qcloud.com/doc/api/zh-cn/group__TRTCCloudListener__android.html#a22afb08b2a1a18563c7be28c904b166a}
        //                        if (mXmagicApi != null) {
        //                            trtcVideoFrame1.texture.textureId = mXmagicApi.process(trtcVideoFrame.texture
        //                            .textureId,
        //                                    trtcVideoFrame.width, trtcVideoFrame.height);
        //                        } else {
        //                            trtcVideoFrame1.texture.textureId = trtcVideoFrame.texture.textureId;
        //                        }
        //                        return trtcVideoFrame1.texture.textureId;
        //                    }
        //
        //                    @Override
        //                    public void onGLContextDestory() {
        //                        //3. GLContext destroy
        //                        if (mXmagicApi != null) {
        //                            mXmagicApi.onDestroy();
        //                        }
        //                    }
        //                });
        mSeekBlurLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //                //Set beauty microdermabrasion level
                //                if (mStartPushFlag && fromUser && mXmagicApi != null) {
                //                    mProperty.effValue.setCurrentDisplayValue(progress);
                //                    mXmagicApi.updateProperty(mProperty);
                //                }
                mTextBlurLevel.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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
                    Toast.makeText(ThirdBeautyTencentEffectActivity.this,
                            getString(R.string.thirdbeauty_please_input_roomid_and_userid), Toast.LENGTH_SHORT).show();
                }
            } else {
                mButtonStartPush.setText(R.string.thirdbeauty_start_push);
                exitRoom();
                mStartPushFlag = false;
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

    private void destroyRoom() {
        if (mTRTCCloud != null) {
            exitRoom();
            mTRTCCloud.setListener(null);
        }
        mTRTCCloud = null;
        TRTCCloud.destroySharedInstance();
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

    protected class TRTCCloudImplListener extends TRTCCloudListener {

        private WeakReference<ThirdBeautyTencentEffectActivity> mContext;

        public TRTCCloudImplListener(ThirdBeautyTencentEffectActivity activity) {
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
            ThirdBeautyTencentEffectActivity activity = mContext.get();
            if (activity != null) {
                Toast.makeText(activity, "onError: " + errMsg + "[" + errCode + "]", Toast.LENGTH_SHORT).show();
                if (errCode == TXLiteAVCode.ERR_ROOM_ENTER_FAIL) {
                    activity.exitRoom();
                }
            }
        }
    }
}