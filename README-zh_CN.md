# 腾讯云实时音视频 TRTC SDK

_[English](README.md) | 简体中文_
## 产品介绍

腾讯实时音视频（Tencent Real-Time Communication，TRTC），将腾讯多年来在网络与音视频技术上的深度积累，以多人音视频通话和低延时互动直播两大场景化方案，通过腾讯云服务向开发者开放，致力于帮助开发者快速搭建低成本、低延时、高品质的音视频互动解决方案，[更多](https://cloud.tencent.com/document/product/647/16788)...

> TRTC SDK 支持Web、Android、iOS、Windows以及Flutter、小程序等所有主流平台， [更多平台](https://github.com/LiteAVSDK?q=TRTC_&type=all&sort=)...



## 更新日志
## Version 10.1 @ 2022.06.06

**新特性：**
- 全平台：支持平滑切换角色，音视频播放不会因为切角色短暂中断。
- iOS：支持立体声音频采集。
- Android：在 Android 10 及以上系统支持采集系统播放音频（详见：startSystemAudioLoopback）。


**功能优化:** 
- 全平台：优化远端发声->无声频繁切换场景下回声消除能力，音质效果更自然。
- 全平台：优化切换角色 + muteLocalAudio 下的音质和启动效果。
- 全平台：优化带宽预测 onSpeedTest 回调。
- iOS：优化内存管理，避免内存堆积的问题。
- Android：优化部分机型手机上耳返的延迟。
- Windows：优化视频下行时视频渲染链路的性能。
- Windows：优化立体声采集逻辑，有效避免漏回声问题。

**缺陷修复:** 
- 全平台：修复退房回调（onExitRoom）的 reason 异常问题
- 全平台：修复上行自定义视频发送时，时间戳相等情况下的黑屏问题
- 全平台：修复先 muteLocalAudio 再 startLocalAudio音频时 crash 问题
- 全平台：修复不手动设置3A场景下开启自定义音频采集会打开3A。
- 全平台：修复音频自定义渲染偶现的杂音问题。
- iOS：修复中途设置 log 路径（setLogDirPath）且沙盒变化时，内存泄漏的问题。
- iOS & Mac：在系统音频服务异常时，BGM 连播场景的崩溃问题。
- Android：修复偶现的蓝牙耳机不断重连接问题。
- Android：修复部分手机上偶现的无声问题。
- Android：修复红米等部分机型反复插拔耳机导致的崩溃问题。
- Windows & iOS：修复截图失败的问题。
- Windows：修复点播播放器开启镜像后，关闭 vod 必现 crash。
- Windows：修复播片 pts 未使用 generateCustomPts，多个播片播放可能导致 pts 回退问题。
- Windows：修复调用setVideoMuteImage偶现的崩溃问题。

更早期的版本更新历史请点击  [更多](https://cloud.tencent.com/document/product/647/46907)...

## 目录说明

```bash
├─ TRTC-API-Example // TRTC API Example，包括视频通话、语音通话的基础功能以及一些高级功能
|  ├─ Basic                 // 演示 TRTC 基础功能示例代码
|  |  ├─ AudioCall                 // 演示 TRTC 音频通话的示例代码
|  |  ├─ VideoCall                 // 演示 TRTC 视频通话的示例代码
|  |  ├─ Live                      // 演示 TRTC 视频互动直播的示例代码
|  |  ├─ VoiceChatRoom             // 演示 TRTC 语音互动直播的示例代码
|  |  ├─ ScreenShare               // 演示 TRTC 录屏直播的示例代码
|  ├─ Advanced              // 演示 TRTC 高级功能示例代码
|  |  ├─ StringRoomId              // 演示 TRTC 字符串房间号示例代码
|  |  ├─ SetVideoQuality           // 演示 TRTC 画质设定示例代码
|  |  ├─ SetAudioQuality           // 演示 TRTC 音质设定示例代码
|  |  ├─ SetRenderParams           // 演示 TRTC 渲染控制示例代码
|  |  ├─ SpeedTest                 // 演示 TRTC 网络测速示例代码
|  |  ├─ PushCDN                   // 演示 TRTC CDN发布示例代码
|  |  ├─ CustomCamera              // 演示 TRTC 自定义视频采集&渲染发布示例代码
|  |  ├─ SetAudioEffect            // 演示 TRTC 设置音效示例代码
|  |  ├─ SetBackgroundMusic        // 演示 TRTC 设置背景音乐示例代码
|  |  ├─ LocalVideoShare           // 演示 TRTC 本地视频文件分享示例代码
|  |  ├─ LocalRecord               // 演示 TRTC 本地视频录制示例代码
|  |  ├─ JoinMultipleRoom          // 演示 TRTC 加入多个房间示例代码
|  |  ├─ SEIMessage                // 演示 TRTC 收发SEI消息示例代码
|  |  ├─ SwitchRoom                // 演示 TRTC 快速切换房间示例代码
|  |  ├─ RoomPk                    // 演示 TRTC 跨房PK示例代码
|  |  ├─ ThirdBeauty               // 演示 TRTC 第三方美颜示例代码
|  
|  
├─ SDK 
│  ├─README.md     // 提供有TRTC SDK最新版本的下载地址
```



## 联系我们
- 如果你遇到了困难，可以先参阅 [常见问题](https://cloud.tencent.com/document/product/647/43018)；

- 如果你想了解TRTC SDK在复杂场景下的应用，可以参考[更多场景案例](https://cloud.tencent.com/document/product/647/57486)；

- 完整的 API 文档见 [SDK 的 API 文档](http://doc.qcloudtrtc.com/md_introduction_trtc_Android_%E6%A6%82%E8%A7%88.html)；
- 如果需要售后技术支持, 你可以点击[这里](https://cloud.tencent.com/document/product/647/19906)；
- 如果发现了示例代码的 bug，欢迎提交 issue；
