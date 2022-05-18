# TRTC SDK

## Overview

Leveraging Tencent's many years of experience in network and audio/video technologies, Tencent Real-Time Communication (TRTC) offers solutions for group audio/video calls and low-latency interactive live streaming. With TRTC, you can quickly develop cost-effective, low-latency, and high-quality interactive audio/video services. [Learn more](https://cloud.tencent.com/document/product/647/16788)...

> We offer SDKs for web, Android, iOS, Windows, Flutter, WeChat Mini Program, and [other mainstream platforms](https://github.com/LiteAVSDK?q=TRTC_&type=all&sort=).



## Changelog

## Version 10.0 @ 2022.05.17

**Function optimization:**

- All Platforms: Optimize the speed of the anchor entry and exit notificationcallbacks ([onRemoteUserEnterRoom](https://liteav.sdk.qcloud.com/doc/api/zh-cn/group__TRTCCloudDelegate__ios.html#a390831928a4d2a7977c4c1572da8be58) / [onRemoteUserLeaveRoom](https://liteav.sdk.qcloud.com/doc/api/zh-cn/group__TRTCCloudDelegate__ios.html#afa7d16e1e4c66d938fc2bc69f3e34c28));

- Windows: Optimize the performance of screen sharing,doubling the performance when no filter window is set;

**Bug fixes:**

- iOS&Mac: Fixan occasional problem that [onComplete](https://liteav.sdk.qcloud.com/doc/api/zh-cn/group__TXAudioEffectManager__ios.html#a08ab9a6d9100f133420e44ae5eb99ee6) callback error when starting to play BGM;

- Android: Fix a crash caused by the network module;

- All Platforms: Fixthe problem of SEI sending abnormally;


For the release notes of earlier versions, click [More](https://cloud.tencent.com/document/product/647/46907).


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
- If you have questions, see [FAQs](https://cloud.tencent.com/document/product/647/43018).

- To learn about how the TRTC SDK can be used in different scenarios, see [Sample Code](https://intl.cloud.tencent.com/document/product/647/42963).

- For complete API documentation, see [SDK API Documentation](http://doc.qcloudtrtc.com/md_introduction_trtc_Android_%E6%A6%82%E8%A7%88.html).
- [Contact us](https://intl.cloud.tencent.com/contact-us) for technical support.
- To report bugs in our sample code, please create an issue.
