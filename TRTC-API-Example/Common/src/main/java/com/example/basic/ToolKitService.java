package com.example.basic;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

/**
 * This Service is used to keep the application alive and should not be deleted.
 * For systems of 9.0 and later, the camera and microphone will stop working after the application exits the background
 * (https://developer.android.com/about/versions/pie/android-9.0-changes-all)
 * This Service is to ensure that the camera and microphone can still work normally
 * after the application exits the background.
 */
public class ToolKitService extends Service {

    private static final int     NOTIFICATION_ID = 1001;
    private static       boolean mIsServiceStart = false;
    private static       Intent  mIntent;

    public static void startToolKitService(Context context) {
        mIsServiceStart = true;
        mIntent = new Intent(context, ToolKitService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(mIntent);
        } else {
            context.startService(mIntent);
        }
    }

    public static void stopToolKitService(Context context) {
        if (mIsServiceStart) {
            context.stopService(mIntent);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createForegroundNotification();
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

    private void createForegroundNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        String notificationChannelId = "notification_channel_id_01";
        String channelName = "TRTC Foreground Service Notification";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel =
                    new NotificationChannel(notificationChannelId, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationChannel.enableVibration(true);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, notificationChannelId);
        builder.setTicker("Nature");
        builder.setSmallIcon(getApplicationContext().getApplicationInfo().icon);
        builder.setContentTitle(getString(getApplicationContext().getApplicationInfo().labelRes));
        builder.setContentText(getString(R.string.app_running));
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setOngoing(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(notificationChannelId);
        }

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE);
        } else {
            startForeground(NOTIFICATION_ID, notification);
        }
    }
}