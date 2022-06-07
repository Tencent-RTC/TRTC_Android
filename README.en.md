# TRTC SDK

## Overview

Leveraging Tencent's many years of experience in network and audio/video technologies, Tencent Real-Time Communication (TRTC) offers solutions for group audio/video calls and low-latency interactive live streaming. With TRTC, you can quickly develop cost-effective, low-latency, and high-quality interactive audio/video services. [Learn more](https://cloud.tencent.com/document/product/647/16788)...

> We offer SDKs for web, Android, iOS, Windows, Flutter, WeChat Mini Program, and [other mainstream platforms](https://github.com/LiteAVSDK?q=TRTC_&type=all&sort=).



## Changelog
## Version 10.1 @ 2022.06.06

**New features:**

- All platforms: Support smooth switching of roles, audio and video playback will not be interrupted briefly by role-switching.
- iOS: Support stereo audio capture.
- Android: Support capturing system playback audio on Android 10 and above (see: startSystemAudioLoopback for details).


**Function optimization:**

- All platforms: Optimize the echo cancellation capability in music scenes for more natural sound quality effect.
- All platforms: Optimize the sound quality and startup effect of switching roles + muteLocalAudio.
- All platforms: Optimize bandwidth prediction onSpeedTest callbacks.
- iOS:Optimize memory management to avoid memory buildup.
- Android: Optimize the delay of the ear return on some models of mobile phones.
- Windows: Optimize the performance of the video rendering link when video is downlink.
- Windows: Optimize the stereo capture logic to effectively avoid the problem of echo leakage.

**Bug fixes:**

- All platforms: Fix the reason exception issue of the check-out callback (onExitRoom)
- All platforms: Fix the black screen problem when sending upstream custom video with equal timestamp.
- All platforms: Fix the crash problem when muteLocalAudio first and then startLocalAudio audio
- All platforms: Fix the problem that turning on custom audio capture without manually setting 3A scene will open 3A.
- All platforms: Fix the occasional noise issue in custom audio rendering.
- iOS: Fixa memory leak when setting the log path (setLogDirPath) midway and the sandbox changes.
- iOS & Mac: Fix the crash of BGM-continuous-play scenes when the system audio service is abnormal.
- Android: Fix an occasional Bluetooth headset reconnection issue.
- Android: Fixthe occasional silence issue on some phones.
- Android: Fix the crash caused by repeatedly plugging and unpluggingheadphones on some models such as Redmi.
- Windows & iOS: Fix the problem of screenshot failure.
- Windows: Fix the crash when closing vod after turning on mirroring in VOD player.
- Windows: Fix the problem that multiple playback of pts may cause the pts to fall back if the pts are not using generatedCustomPts.
- Windows:  Fix the crash about muteLocalVideo api.


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
