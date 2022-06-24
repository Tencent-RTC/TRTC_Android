# TRTC SDK

_[简体中文](README-zh_CN.md) | English_
## Overview

Leveraging Tencent's many years of experience in network and audio/video technologies, Tencent Real-Time Communication (TRTC) offers solutions for group audio/video calls and low-latency interactive live streaming. With TRTC, you can quickly develop cost-effective, low-latency, and high-quality interactive audio/video services. [Learn more](https://cloud.tencent.com/document/product/647/16788)...

> We offer SDKs for web, Android, iOS, Windows, Flutter, WeChat Mini Program, and [other mainstream platforms](https://github.com/LiteAVSDK?q=TRTC_&type=all&sort=).



## Changelog
## Version 10.2 @ 2022.06.26

**New features:**

- All platforms: A new, more flexible and powerful mixed-stream retweet API has been introduced. See: startPublishMediaStream;
- All platforms: New 3D audio effect function, see: enable3DSpatialAudioEffect;
- All platforms: Add vocal detection function. When muteLoalAudio and setAudioCaptureVolume are 0, it will not affect the vocal detection result. See enableAudioVolumeEvaluation for details, Tips: It is convenient to prompt the user to open the microphone;
- All platforms: Add permission verification when switching roles. For details, see: switchRole(TRTCRoleType role, const char* privateMapKey).
- iOS & Mac: Customize C++ interface for preprocessing, support video processing in texture mode;

**Function optimization:**

- Android: Optimize ear return effect and reduce latency;
- Android: Optimize the audio capture link to solve the noise problem of some models;
- iOS: Optimize the uplink video processing link to save CPU and GPU usage;
- Windows & Mac: Optimize the encoding performance when sharing windows, the encoding width and height are no longer affected by the capture window size;
- Windows: Optimize performance, reduce memory fragmentation and performance overhead caused by its allocation;

Bug fixes:

- All platforms: Fix occasional uplink failure when switching network types;
- iOS: Fix the noise problem with locally recorded files on some iOS 14 systems;


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
