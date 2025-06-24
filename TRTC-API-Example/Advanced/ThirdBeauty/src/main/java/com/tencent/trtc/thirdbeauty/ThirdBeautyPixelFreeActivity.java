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
import com.hapi.pixelfree.PFBeautyFiterType;
import com.tencent.liteav.TXLiteAVCode;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCCloudListener;
import com.tencent.trtc.debug.GenerateTestUserSig;
import com.tencent.trtc.thirdbeauty.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.hapi.pixelfree.PFDetectFormat;
import com.hapi.pixelfree.PFIamgeInput;
import com.hapi.pixelfree.PFRotationMode;
import com.hapi.pixelfree.PFSrcType;
import com.hapi.pixelfree.PixelFree;


public class ThirdBeautyPixelFreeActivity extends TRTCBaseActivity implements View.OnClickListener {

    private static final String TAG = "ThirdBeautyPixelFreeActivity";

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
    private PixelFree mPixelFree = new PixelFree();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_beauty);

        getSupportActionBar().hide();

        mTRTCCloud = TRTCCloud.sharedInstance(getApplicationContext());
        mTRTCCloud.setListener(new com.tencent.trtc.thirdbeauty.ThirdBeautyPixelFreeActivity.TRTCCloudImplListener(com.tencent.trtc.thirdbeauty.ThirdBeautyPixelFreeActivity.this));
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
//                          1. Set the TRTCVideoFrameListener callback, see the API documentation for details {
//        https://liteav.sdk.qcloud.com/doc/api/zh-cn/group__TRTCCloud__android.html#a0b565dc8c77df7fb826f0c45d8ad2d85}
                mTRTCCloud.setLocalVideoProcessListener(TRTCCloudDef.TRTC_VIDEO_PIXEL_FORMAT_Texture_2D,
                TRTCCloudDef.TRTC_VIDEO_BUFFER_TYPE_TEXTURE, new TRTCCloudListener.TRTCVideoFrameListener() {
                    @Override
                    public void onGLContextCreated() {

                        mPixelFree.create();
                        byte[] bytes = mPixelFree.readBundleFile(ThirdBeautyPixelFreeActivity.this, "pixelfreeAuth.lic");
                        mPixelFree.auth(ThirdBeautyPixelFreeActivity.this, bytes, bytes.length);

                        byte[] bytes2 = mPixelFree.readBundleFile(ThirdBeautyPixelFreeActivity.this, "filter_model.bundle");
                        mPixelFree.createBeautyItemFormBundle(
                                bytes2,
                                bytes2.length,
                                PFSrcType.PFSrcTypeFilter);
                    }

                    @Override
                    public int onProcessVideoFrame(TRTCCloudDef.TRTCVideoFrame srcFrame,
                    TRTCCloudDef.TRTCVideoFrame dstFrame) {
////        //                  3. Call the third-party beauty module for processing, please refer to the
////                       API documentation for details. {
//                       https://liteav.sdk.qcloud.com/doc/api/zh-cn/group__TRTCCloudListener__android.html#a22afb08b2a1a18563c7be28c904b166a}
////                       dstFrame.texture.textureId = mFURenderer
////                       .onDrawFrameSingleInput(srcFrame.texture.textureId, srcFrame.width, srcFrame.height);

                        PFIamgeInput pxInput = new PFIamgeInput();
                        pxInput.setWigth(srcFrame.width);
                        pxInput.setHeight(srcFrame.height);
                        pxInput.setFormat(PFDetectFormat.PFFORMAT_IMAGE_TEXTURE);
                        pxInput.setRotationMode(PFRotationMode.PFRotationMode0);
                        pxInput.setTextureID(srcFrame.texture.textureId);
                        mPixelFree.processWithBuffer(pxInput);

                        dstFrame.texture.textureId = pxInput.getTextureID();
                        Log.d("[PixelFree]", "onProcessVideoFrame: "+dstFrame.texture.textureId+ "zzz"+ srcFrame.texture.textureId);
                        return pxInput.getTextureID();
                    }

                    @Override
                    public void onGLContextDestory() {
        //                   4. GLContext destruction
//                        mPixelFree.;
                    }
                });

                mSeekBlurLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (mStartPushFlag && fromUser) {
        //                    5. Set the dermabrasion level
                            mPixelFree.pixelFreeSetBeautyFiterParam(PFBeautyFiterType.PFBeautyFiterTypeFaceBlurStrength,progress/9f);
                        }
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
                    Toast.makeText(com.tencent.trtc.thirdbeauty.ThirdBeautyPixelFreeActivity.this,
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

        private WeakReference<com.tencent.trtc.thirdbeauty.ThirdBeautyPixelFreeActivity> mContext;

        public TRTCCloudImplListener(com.tencent.trtc.thirdbeauty.ThirdBeautyPixelFreeActivity activity) {
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
//            Log.d(TAG, "sdk callback onError");
            com.tencent.trtc.thirdbeauty.ThirdBeautyPixelFreeActivity activity = mContext.get();
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
