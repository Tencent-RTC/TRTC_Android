# 腾讯云实时音视频 TRTC SDK

_[English](README.md) | 简体中文_
## 产品介绍

腾讯实时音视频（Tencent Real-Time Communication，TRTC），将腾讯多年来在网络与音视频技术上的深度积累，以多人音视频通话和低延时互动直播两大场景化方案，通过腾讯云服务向开发者开放，致力于帮助开发者快速搭建低成本、低延时、高品质的音视频互动解决方案，[更多](https://cloud.tencent.com/document/product/647/16788)...

> TRTC SDK 支持Web、Android、iOS、Windows以及Flutter、小程序等所有主流平台， [更多平台](https://github.com/LiteAVSDK?q=TRTC_&type=all&sort=)...



## 更新日志
### Version 11.3 @ 2023.07.07

**新特性**
- 全平台：新增视频画面梯形校正功能（仅 Professinal 版本支持），用于手动修正透视画面角度畸变。详见 `setPerspectiveCorrectionPoints`。
- 全平台：新增声音频谱回调，可用于声浪动画或音量频谱展示。详见 `enableAudioVolumeEvaluation` 及 `TRTCVolumeInfo`。
- 全平台：新增混响效果“录音棚2”，详见 `TXVoiceReverbType`。
- 全平台：新增混流接口 SEI 参数设置，用于转推 CDN 分发时透传 SEI。详见 `TRTCTranscodingConfig`。
- Windows：新增音速达版权曲库音乐打分功能，可用于合唱实时评分，详见 `createSongScore`。
- iOS&Android：新增 `startPlayMusic` 对 .ogg 格式音乐文件的支持。
- Flutter：新增 `setSystemAudioLoopbackVolume` 方法（iOS）。


**功能优化**
- 全平台：优化自适应数字增益算法，提升声音听感，增强音量稳定性，避免音量过大或过小。
- 全平台：优化视频秒开耗时，提升进房后视频第一帧画面的加载速度。
- 全平台：优化单主播推流的弱网抗性，提升网络延时抖动情况下的流畅度。
- Android：优化音频采集播放逻辑，避免在部分 Android 设备上出现声音异常的问题。
- Android：优化视频辅流硬编表现，提升共享屏幕的画面质量。
- iOS：优化音频设备重启逻辑，减少声音中断的发生次数。
- iOS&Android：TXLivePlayer 删除点播相关接口，点播视频请使用 TXVodPlayer 进行播放。

**缺陷修复**
- Android：修复Android 12及以上版本系统部分本地录制的视频在苹果Safari 浏览器上无法播放的问题。



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

- 完整的 API 文档见 [SDK 的 API 文档](https://cloud.tencent.com/document/product/647/32267)；
- 如果需要售后技术支持, 你可以点击[这里](https://cloud.tencent.com/document/product/647/19906)；
- 如果发现了示例代码的 bug，欢迎提交 issue；
