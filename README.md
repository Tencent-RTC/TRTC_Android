# 腾讯云实时音视频 TRTC SDK

## 产品介绍

腾讯实时音视频（Tencent Real-Time Communication，TRTC），将腾讯多年来在网络与音视频技术上的深度积累，以多人音视频通话和低延时互动直播两大场景化方案，通过腾讯云服务向开发者开放，致力于帮助开发者快速搭建低成本、低延时、高品质的音视频互动解决方案，[更多](https://cloud.tencent.com/document/product/647/16788)...

> TRTC SDK 支持Web、Android、iOS、Windows以及Flutter、小程序等所有主流平台， [更多平台](https://github.com/LiteAVSDK?q=TRTC_&type=all&sort=)...



## 更新日志

### Version 9.6 @ 2022.03.24

**重要更新：**

- 全平台：优化 TRTC SDK 体积，详细数据见下表：

|             | **优化前**               | **优化后**               |
| ----------- | ------------------------ | ------------------------ |
| **Android** | armv7: 6.95M arm64: 7.94M | armv7: 4.32M arm64: 4.85M |
| **iOS**     | arm64: 3.23M             | arm64: 3.15M             |
| **Windows** | Win32: 21.3M Win64: 26.9M | Win32: 15.0M Win64: 17.2M |
| **Mac**     | x86_64：18.1M            | x86_64：15.8M            |

- **全平台：完成三方库合规整改，符合国内、海外的合规要求；**

**故障修复：**

- 全平台：修复已知问题，提升稳定性；

**功能优化：**

- iOS：修复在补光灯下偶现的曝光过度问题；
- Mac：优化纹理上传，提升性能；
- Android：优化美颜等预处理流程，修复低端机器下的采集卡顿问题；
- Windows：Live V1 升级到 V2 接口，提供更稳定的 Live 组件；
- Windows：提升了低端机上的显卡兼容性；

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
