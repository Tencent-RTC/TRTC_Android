# TRTC SDK

_[简体中文](README-zh_CN.md) | English_
## Overview

Leveraging Tencent's many years of experience in network and audio/video technologies, Tencent Real-Time Communication (TRTC) offers solutions for group audio/video calls and low-latency interactive live streaming. With TRTC, you can quickly develop cost-effective, low-latency, and high-quality interactive audio/video services. [Learn more](https://trtc.io/document)...

> We offer SDKs for web, Android, iOS, Windows, Flutter, WeChat Mini Program, and [other mainstream platforms](https://github.com/LiteAVSDK?q=TRTC_&type=all&sort=).



## Changelog
### Version 11.3 @ 2023.07.07

**New features**

- All Platforms: Added trapezoid correction for video (only supported by the Professional version) for manual correction of perspective distortion. See `setPerspectiveCorrectionPoints` for details.
- All Platforms: Added audio spectrum callback, which can be used for sound wave animation or volume spectrum display. See `enableAudioVolumeEvaluation` and `TRTCVolumeInfo` for details.
- All Platforms: Added a new reverb effect "Studio 2". See `TXVoiceReverbType` for details.
- All Platforms: Added SEI parameter settings for mixed stream, used for transport SEI infomation when publishing stream to CDN. See `TRTCTranscodingConfig` for details.
- Windows: Added real-time music scoring for Yinsuda Authorized Music, which can be used for real-time scoring of online singing. See `createSongScore` for details.
- iOS & Android: Added support for .ogg format music files in `startPlayMusic`.
- Flutter: Added `setSystemAudioLoopbackVolume`(iOS).


**Improvements**

- All platforms: Optimized adaptive digital gain algorithm to improve listening experience.
- All platforms: Optimized the loading speed of the first video frame after entering the room.
- All platforms: Optimized weak network resistance for single user streaming to improve smoothness under network delay and jitter.
- Android: Optimized audio capture and playback feature to avoid abnormal sound issues on some Android devices.
- Android: Optimized video sub-stream hardware encoding performance, improving quality of screen sharing.
- iOS: Optimized audio device restart strategy to reduce the occurrence of sound interruptions.
- iOS & Android: Removed on-demand related interfaces from `TXLivePlayer`. For on-demand video playback, please use `TXVodPlayer`.

**Bug fixes**
- Android: Fixed the issue where some locally recorded videos on Android 12 and above system versions cannot be played on Apple's Safari.


For the release notes of earlier versions, click [More](https://www.tencentcloud.com/document/product/647/39426).


## Contents

```bash
├─ TRTC-API-Example // TRTC API examples, including those for basic features such as audio call and video call as well as some advanced features
|  ├─ Basic                 // Demos for TRTC basic features
|  |  ├─ AudioCall                 // Demo for audio call in TRTC
|  |  ├─ VideoCall                 // Demo for video call in TRTC
|  |  ├─ Live                      // Demo for interactive video live streaming in TRTC
|  |  ├─ VoiceChatRoom             // Demo for interactive audio live streaming in TRTC
|  |  ├─ ScreenShare               // Demo for screen sharing live streaming in TRTC
|  ├─ Advanced              // Demos for TRTC advanced features
|  |  ├─ StringRoomId              // Demo for string room ID in TRTC
|  |  ├─ SetVideoQuality           // Demo for video quality setting in TRTC
|  |  ├─ SetAudioQuality           // Demo for audio quality setting in TRTC
|  |  ├─ SetRenderParams           // Demo for rendering control in TRTC
|  |  ├─ SpeedTest                 // Demo for network speed test in TRTC
|  |  ├─ PushCDN                   // Demo for CDN push in TRTC
|  |  ├─ CustomCamera              // Demo for custom video capturing and rendering in TRTC
|  |  ├─ SetAudioEffect            // Demo for sound effect configuration in TRTC
|  |  ├─ SetBackgroundMusic        // Demo for background music configuration in TRTC
|  |  ├─ LocalVideoShare           // Demo for local video file sharing in TRTC
|  |  ├─ LocalRecord               // Demo for local video recording in TRTC
|  |  ├─ JoinMultipleRoom          // Demo for multi-room join in TRTC
|  |  ├─ SEIMessage                // Demo for SEI message sending/receiving in TRTC
|  |  ├─ SwitchRoom                // Demo for quick room switching in TRTC
|  |  ├─ RoomPk                    // Demo for cross-room competition in TRTC
|  |  ├─ ThirdBeauty               // Demo for third-party beauty filters in TRTC
|  
|  
├─ SDK 
│  ├─README.md     // Download address of the latest version of TRTC SDK
```



## Contact Us
- If you have questions, see [FAQs](https://www.tencentcloud.com/document/product/647/36057).

- To learn about how the TRTC SDK can be used in different scenarios, see [Sample Code](https://www.tencentcloud.com/document/product/647/42963).

- For complete API documentation, see [SDK API Documentation](https://www.tencentcloud.com/document/product/647/35125).

- Communication & Feedback   
Welcome to join our Telegram Group to communicate with our professional engineers! We are more than happy to hear from you~
Click to join: [https://t.me/+EPk6TMZEZMM5OGY1](https://t.me/+EPk6TMZEZMM5OGY1)   
Or scan the QR code   
  <img src="https://qcloudimg.tencent-cloud.cn/raw/79cbfd13877704ff6e17f30de09002dd.jpg" width="300px">    

