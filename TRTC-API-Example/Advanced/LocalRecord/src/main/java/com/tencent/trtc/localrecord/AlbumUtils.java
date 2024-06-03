package com.tencent.trtc.localrecord;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Operation album album tool class
 */
public class AlbumUtils {
    private static final String TAG = "AlbumUtils";

    private AlbumUtils() {

    }

    /**
     * Insert video into local photo album
     *
     * @param videoPath The video path saved to the album
     * @param coverPath The path to store album thumbnails
     */
    public static void saveVideoToDCIM(Context context, String videoPath, String coverPath) {
        if (Build.VERSION.SDK_INT >= 29) {
            saveVideoToDCIMOnAndroid10(context, videoPath);
        } else {
            saveVideoToDCIMBelowAndroid10(context, videoPath, coverPath);
        }
    }

    private static void saveVideoToDCIMBelowAndroid10(Context context, String videoPath, String coverPath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoPath);
        int duration = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));

        File file = new File(videoPath);
        if (file.exists()) {
            try {
                ContentValues values = initCommonContentValues(file);
                values.put(MediaStore.Video.VideoColumns.DATE_TAKEN, System.currentTimeMillis());
                values.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");
                values.put(MediaStore.Video.VideoColumns.DURATION, duration);
                context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
                if (coverPath != null) {
                    insertVideoThumb(context, file.getPath(), coverPath);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "file :" + videoPath + " is not exists");
        }
    }

    /**
     * How to save video files locally in Android 10(Q)
     */
    private static void saveVideoToDCIMOnAndroid10(Context context, String videoPath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoPath);
        int duration = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));

        File file = new File(videoPath);
        if (file.exists()) {
            ContentValues values = new ContentValues();
            long currentTimeInSeconds = System.currentTimeMillis();
            values.put(MediaStore.MediaColumns.TITLE, file.getName());
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, file.getName());
            values.put(MediaStore.MediaColumns.DATE_MODIFIED, currentTimeInSeconds);
            values.put(MediaStore.MediaColumns.DATE_ADDED, currentTimeInSeconds);
            values.put(MediaStore.MediaColumns.SIZE, file.length());
            values.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");
            // duration
            values.put(MediaStore.Video.VideoColumns.DURATION, duration);
            values.put(MediaStore.Video.VideoColumns.DATE_TAKEN, System.currentTimeMillis());
            // Android 10 inserted into the gallery flag
            values.put(MediaStore.MediaColumns.IS_PENDING, 1);

            Uri collection = MediaStore.Video.Media.getContentUri("external_primary");
            Uri item = context.getContentResolver().insert(collection, values);
            ParcelFileDescriptor pfd = null;
            FileOutputStream fos = null;
            FileInputStream fis = null;
            try {
                pfd = context.getContentResolver().openFileDescriptor(item, "w");
                // Write data into the pending image.
                fos = new FileOutputStream(pfd.getFileDescriptor());
                fis = new FileInputStream(file);
                byte[] data = new byte[1024];
                int length = -1;
                while ((length = fis.read(data)) != -1) {
                    fos.write(data, 0, length);
                }
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (pfd != null) {
                    try {
                        pfd.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            values.clear();
            values.put(MediaStore.Video.VideoColumns.IS_PENDING, 0);
            context.getContentResolver().update(item, values, null, null);
        } else {
            Log.d(TAG, "file :" + videoPath + " is not exists");
        }
    }

    @NonNull
    private static ContentValues initCommonContentValues(@NonNull File saveFile) {
        ContentValues values = new ContentValues();
        long currentTimeInSeconds = System.currentTimeMillis();
        values.put(MediaStore.MediaColumns.TITLE, saveFile.getName());
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, saveFile.getName());
        values.put(MediaStore.MediaColumns.DATE_MODIFIED, currentTimeInSeconds);
        values.put(MediaStore.MediaColumns.DATE_ADDED, currentTimeInSeconds);
        values.put(MediaStore.MediaColumns.DATA, saveFile.getAbsolutePath());
        values.put(MediaStore.MediaColumns.SIZE, saveFile.length());
        return values;
    }

    private static void insertVideoThumb(Context context, String videoPath, String coverPath) {
        Cursor cursor = context.getContentResolver()
                .query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Video.Thumbnails._ID},
                        // return id list
                        String.format("%s = ?", MediaStore.Video.Thumbnails.DATA), //Query database based on path
        new String[]{videoPath}, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String videoId = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Thumbnails._ID));
                // Query the Video id
                ContentValues thumbValues = new ContentValues();
                thumbValues.put(MediaStore.Video.Thumbnails.DATA, coverPath);// Thumbnail path
                thumbValues.put(MediaStore.Video.Thumbnails.VIDEO_ID, videoId);// Video ID used for binding
                // The kind of Video is generally 1
                thumbValues.put(MediaStore.Video.Thumbnails.KIND, MediaStore.Video.Thumbnails.MINI_KIND);
                // Only returns the image size information, not the specific content of the image
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                Bitmap bitmap = BitmapFactory.decodeFile(coverPath, options);
                if (bitmap != null) {
                    thumbValues.put(MediaStore.Video.Thumbnails.WIDTH, bitmap.getWidth());// Thumbnail width
                    thumbValues.put(MediaStore.Video.Thumbnails.HEIGHT, bitmap.getHeight());// Thumbnail height
                    if (!bitmap.isRecycled()) {
                        bitmap.recycle();
                    }
                }
                context.getContentResolver()
                        .insert(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, thumbValues);// Thumbnail database
            }
            cursor.close();
        }
    }

}
