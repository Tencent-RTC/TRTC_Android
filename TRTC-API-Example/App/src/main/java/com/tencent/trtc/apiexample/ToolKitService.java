package com.tencent.trtc.apiexample;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.blankj.utilcode.util.ServiceUtils;

/**
 * This Service is used to keep the application alive and should not be deleted.
 * For systems of 9.0 and later, the camera and microphone will stop working after the application exits the background
 * (https://developer.android.com/about/versions/pie/android-9.0-changes-all)
 * This Service is to ensure that the camera and microphone can still work normally
 * after the application exits the background.
 */
public class ToolKitService extends Service {

    private static final int NOTIFICATION_ID = 1001;

    @Override
    public void onCreate() {
        super.onCreate();
        Notification notification = createForegroundNotification();
        startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    public static void start(Context context) {
        if (ServiceUtils.isServiceRunning(ToolKitService.class)) {
            return;
        }
        Intent service = new Intent(context, ToolKitService.class);
        /*
         * For systems of 9.0 and later, the camera and microphone will stop working after the application exits the
         * background (https://developer.android.com/about/versions/pie/android-9.0-changes-all)
         * This Service is to ensure that the camera and microphone can still work normally after the application exits
         *  the background, so it can only be started in 9.0 and later systems.
         */
        if (Build.VERSION.SDK_INT >= 28) {
            context.startForegroundService(service);
        }
    }

    private Notification createForegroundNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String notificationChannelId = "notification_channel_id_01";
        // For systems above Android 8.0, create a new message channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Channel name visible to users
            String channelName = "TRTC Foreground Service Notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel =
                    new NotificationChannel(notificationChannelId, channelName, importance);
            notificationChannel.setDescription("Channel description");
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, notificationChannelId);
        builder.setSmallIcon(R.mipmap.ic_appicon);
        builder.setContentTitle(getString(R.string.app_name));
        builder.setContentText(getString(R.string.app_running));
        builder.setWhen(System.currentTimeMillis());
        return builder.build();
    }
}