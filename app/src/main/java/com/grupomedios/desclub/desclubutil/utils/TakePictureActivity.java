package com.grupomedios.desclub.desclubutil.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.grupomedios.desclub.desclubandroid.R;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class TakePictureActivity extends Activity {

    private static final int REQUEST_CODE_TAKE_PICTURE = 301;
    private static final int REQUEST_CODE_SELECT_PICTURE = 302;
    public static final String ARG_DATA_PICTURE = "picture";
    private String mCurrentPhotoPath;

    public static final String ARG_ACTION_TAKE_PICTURE = "take_picture";
    public static final String ARG_HIGH_QUALITY = "highQuality";
    private boolean highQuality = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getIntent().getExtras();
        boolean takePicture = false;
        if (arguments != null) {
            takePicture = arguments.getBoolean(ARG_ACTION_TAKE_PICTURE, false);
            highQuality = arguments.getBoolean(ARG_HIGH_QUALITY, false);
        }

        if (!takePicture)
            showTakePictureDialog();
        else {
            new Timer().schedule(new TimerTask() {

                @Override
                public void run() {
                    takePicture();

                }
            }, 1000);
        }
    }

    public void showTakePictureDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setMessage(R.string.picture_title);
        dialog.setPositiveButton(R.string.picture_profile_camera,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        takePicture();
                    }
                });

        dialog.setNegativeButton(getString(R.string.picture_profile_gallery),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectPicture();
                    }
                });
        dialog.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                setResult(RESULT_CANCELED);
                dialog.dismiss();
                TakePictureActivity.this.finish();
            }
        });
        dialog.create().show();

    }
//
    private void takePicture() {
        try {
            if (CameraUtils.hasHardwareCamera(this)
                    && CameraUtils.hasCameraApp(this)) {

                File myPictureFile;

                myPictureFile = FileUtils.getOutputMediaFile(FileUtils.FOLDER_TYPE_FINAL, FileUtils.MEDIA_TYPE_IMAGE_JPG);
                mCurrentPhotoPath = myPictureFile.getAbsolutePath();

                try {
                    Intent takePictureIntent = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(myPictureFile));
                    takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                    startActivityForResult(takePictureIntent,
                            REQUEST_CODE_TAKE_PICTURE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Intent takePictureIntent = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(myPictureFile));
                    startActivityForResult(takePictureIntent,
                            REQUEST_CODE_TAKE_PICTURE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selectPicture() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        photoPickerIntent.setType("image/jpeg");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_TAKE_PICTURE) {
                setResult(mCurrentPhotoPath);
            }
            if (requestCode == REQUEST_CODE_SELECT_PICTURE) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mCurrentPhotoPath = cursor.getString(columnIndex);
                cursor.close();

                setResult(mCurrentPhotoPath);

            }
        }
        finish();
    }

    private void setResult(String picturePath) {
        Intent data = new Intent();
        data.putExtra(ARG_DATA_PICTURE, picturePath);
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    public int convertToPx(int dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
