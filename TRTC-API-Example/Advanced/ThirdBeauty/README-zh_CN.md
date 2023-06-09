 ## 第三方美颜 SDK

_[English](README.md) | 简体中文_

本教程将向您展示如何在 TRTC SDK 中快速接入第三方美颜 SDK（以相芯美颜 SDK 为例）。具体步骤如下：

### 步骤 1: 下载工程并添加 faceunity 模块

- 下载：[https://github.com/Faceunity/FUTRTCDemoDroid](https://github.com/Faceunity/FUTRTCDemoDroid) 工程。
- 将 `faceunity` 模块添加到工程中。

### 步骤 2: 修改 build.gradle

如果您需要指定应用的 so 架构，可以修改当前模块的 `build.gradle` 文件：

```gradle
android {
    // ...
    defaultConfig {
        // ...
        ndk {
            abiFilters 'armeabi-v7a', 'arm64-v8a'
        }
    }
}
```

### 步骤 3: 初始化美颜模块

根据您的需求初始化美颜模块：

```java
FURenderer.setup(getApplicationContext());
mFURenderer = new FURenderer.Builder(getApplicationContext())
    .setCreateEglContext(false)
    .setInputTextureType(0)    // TRTC 这里用的 0 TEXTURE_2D
    .setCreateFaceBeauty(true)
    .build();
```

### 步骤 4: 设置自定义预处理回调

设置 TRTC 的自定义预处理回调：`TRTCCloud.setLocalVideoProcessListener`。请查阅 [API 说明文档](https://cloud.tencent.com/document/product/647/79628#714daaa9eb9a8b4a05dd5db8ed862815) 了解详细信息。

### 步骤 5: 使用第三方美颜处理视频数据

在 `TRTCCloudListener.TRTCVideoFrameListener#onProcessVideoFrame` 方法中使用第三方美颜处理视频数据。请查阅 [API 说明文档](https://cloud.tencent.com/document/product/647/79629#ca7517167849fe21e3fc21c678b8b427) 了解详细信息。