package com.grupomedios.desclub.desclubutil.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Matias Sieff on 29/04/2015.
 */
public class FileUtils {

    public static final int MEDIA_TYPE_IMAGE_JPG = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int MEDIA_TYPE_IMAGE_PNG = 3;
    public static final int MEDIA_TYPE_MP3 = 4;
    public static final int MEDIA_TYPE_AAC = 5;

    public static final String FOLDER_TYPE_FINAL = "Desclub";
    public static final String FOLDER_TYPE_TEMP = ".Desclub-Temp";

    public static File getOutputMediaFile(final String folderType, final int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), folderType);
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        return createMediaFile(mediaStorageDir, type);
    }

    public static boolean createTempFolder() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), FOLDER_TYPE_TEMP);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return false;
            }
        }

        return true;
    }


    public static File getInternalOutputMediaFile(Context context, final int type) {
        File mediaStorageDir = new File(context.getFilesDir().toString());

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        return createMediaFile(mediaStorageDir, type);
    }

    private static File createMediaFile(File folder, final int type) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;

        switch (type) {
            case MEDIA_TYPE_IMAGE_JPG:
                mediaFile = new File(folder.getPath() + File.separator +
                        "IMG_" + timeStamp + ".jpg");
                break;
            case MEDIA_TYPE_VIDEO:
                mediaFile = new File(folder.getPath() + File.separator +
                        "VID_" + timeStamp + ".mp4");
                break;
            case MEDIA_TYPE_IMAGE_PNG:
                mediaFile = new File(folder.getPath() + File.separator +
                        "VID_" + timeStamp + ".png");
                break;
            case MEDIA_TYPE_MP3:
                mediaFile = new File(folder.getPath() + File.separator +
                        "AUDIO_" + timeStamp + ".mp3");
                break;
            case MEDIA_TYPE_AAC:
                mediaFile = new File(folder.getPath() + File.separator +
                        "AUDIO_" + timeStamp + ".aac");
                break;
            default:
                mediaFile = null;
        }

        return mediaFile;
    }

    public static File getTemptFolder() {
        return new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), FOLDER_TYPE_TEMP);
    }

    public static File getInternalFolder(Context context) {
        return new File(context.getFilesDir().toString());
    }

    public static boolean exists(String filePath) {
        File file = new File(filePath);
        return file != null && file.exists();
    }

    public static void deleteTempFolder() {
        try {
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), FOLDER_TYPE_TEMP);
            if (mediaStorageDir != null && mediaStorageDir.exists())
                renameAndDelete(mediaStorageDir);
        } catch (Exception e) {
        }
    }

    public static final void renameAndDelete(File fileOrDirectory) {
        File newFile = new File(fileOrDirectory.getParent() + File.separator
                + "_" + fileOrDirectory.getName());
        fileOrDirectory.renameTo(newFile);
        delete(newFile);
    }

    public static final void delete(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                delete(child);

        fileOrDirectory.delete();
    }

    public static void updateAndroidGallery(Context context, File file) {
        MediaScannerConnection.scanFile(context,
                new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
    }

    public static String getPathFromUri(Activity context, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.managedQuery(uri, projection, null, null, null);
        context.startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static Uri saveBitmapToFile(Context context, Bitmap bitmap, int fileType) {
        // Save in a file
        OutputStream fout = null;
        File imageFile = FileUtils.getInternalOutputMediaFile(context, fileType);
        if (imageFile != null) {
            try {
                fout = new FileOutputStream(imageFile);

                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fout);
                fout.flush();
                fout.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return Uri.fromFile(imageFile);
        } else
            return null;
    }


    public static Uri saveBitmapToFileInGallery(Context context, Bitmap bitmap, int fileType) {
        // Save in a file
        OutputStream fout = null;
        File imageFile = FileUtils.getOutputMediaFile(FOLDER_TYPE_FINAL, fileType);
        if (imageFile != null) {
            try {
                fout = new FileOutputStream(imageFile);

                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fout);
                fout.flush();
                fout.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            updateAndroidGallery(context, imageFile);
            return Uri.fromFile(imageFile);
        } else
            return null;
    }

}
