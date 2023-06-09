## Third-Party Beauty SDK

_[简体中文](README-zh_CN.md) | English_

In this tutorial, we'll show you how to quickly integrate a third-party beauty SDK (taking Faceunity Beauty SDK as an example) into TRTC SDK. Follow the steps below:

### Step 1: Download the project and add the faceunity module

- Download the project: [https://github.com/Faceunity/FUTRTCDemoDroid](https://github.com/Faceunity/FUTRTCDemoDroid).
- Add the `faceunity` module to your project.

### Step 2: Modify build.gradle

If you need to specify the so architecture for your application, modify the `build.gradle` file of the current module:

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

### Step 3: Initialize the beauty module

Initialize the beauty module based on your needs:

```java
FURenderer.setup(getApplicationContext());
mFURenderer = new FURenderer.Builder(getApplicationContext())
    .setCreateEglContext(false)
    .setInputTextureType(0)    // TRTC uses 0 TEXTURE_2D here
    .setCreateFaceBeauty(true)
    .build();
```

### Step 4: Set custom preprocessing callback

Set the custom preprocessing callback for TRTC: `TRTCCloud.setLocalVideoProcessListener`. Refer to the [API Documentation](https://trtc.tencentcloud.com/document/35113/50762#714daaa9eb9a8b4a05dd5db8ed862815) for details.

### Step 5: Use third-party beauty processing for video data

Use third-party beauty processing for video data in the `TRTCCloudListener.TRTCVideoFrameListener#onProcessVideoFrame` method. Refer to the [API Documentation](https://trtc.tencentcloud.com/document/35113/50763#ca7517167849fe21e3fc21c678b8b427) for details.