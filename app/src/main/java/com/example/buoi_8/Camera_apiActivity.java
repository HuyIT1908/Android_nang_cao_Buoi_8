package com.example.buoi_8;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class Camera_apiActivity extends AppCompatActivity {
    private Button btnCapture;
    private FrameLayout container;

    private Camera mCamera;

    private CameraPreview mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_api);

        btnCapture = findViewById(R.id.btnCapture);
        container = findViewById(R.id.container);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            mCamera = getCameraInstance();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 999);
        }

        mPreview = new CameraPreview(this, mCamera);

        container.addView(mPreview);

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkCameraHardware(Camera_apiActivity.this)) {
                    try {
                        mCamera.takePicture(null, null, mPicture);
                    }catch (Exception e){
                        Log.e("----------------------" , String.valueOf(e));
                        mCamera.release();
                    }
                }
                show_img();
            }
        });
    }

//    private boolean checkCameraHardware(Context context){
//        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA) ){
////            thiết bị này có một máy ảnh
//            return true;
//        }else {
////            không có máy ảnh trên thiết bị này
//            return false;
//        }
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                mCamera = getCameraInstance();
            } else {
                Toast.makeText(this, "Camera cant run without permission", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            int num = Camera.getNumberOfCameras();
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
    public Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
            setCameraDisplayOrientation(Camera_apiActivity.this, Camera.CameraInfo.CAMERA_FACING_BACK, c);
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            Log.e("mess : ----------------", e.getMessage());
        }
        return c; // returns null if camera is unavailable
    }
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null) {
                Log.d("TAG : -----------------", "Error creating media file, check storage permissions");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d("TAG", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("TAG", "Error accessing file: " + e.getMessage());
            }
        }
    };

    private File getOutputMediaFile(int mediaTypeImage) {
//        File file = new File(Environment.getStorageDirectory().getAbsolutePath());
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        return file;

    }
    public static void setCameraDisplayOrientation(Camera_apiActivity activity,
                                                   int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    public void show_img(){
        try {
//            startActivityForResult(intent , 999);

            File file = create_imageFIile();
            if (file != null){
                Uri photoURI = FileProvider.getUriForFile(
                        this,
                        "com.example.buoi_8.fileprovider",
                        file
                );
            }
        }catch (ActivityNotFoundException e) {
            Toast.makeText(this , "ko thay phan mem chup anh nao",
                    Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.preview_camera , null);

        ImageView img = layout.findViewById(R.id.imv_preview);
        img.setImageResource(R.drawable.ic_launcher_foreground);
//        set_Pic(img);
//        gallery_Add_img();

//        AlertDialog.Builder builder =  new AlertDialog.Builder(Camera_apiActivity.this);
//        builder.setTitle("Show image on AlertDialog ");
//
//        builder.setView(layout);
//        AlertDialog dialog = builder.create();
//        dialog.show();
    }

    String currenPhotoPATH;
    public File create_imageFIile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        String file_Name = "JPGE_" + timeStamp + "_";
        File storage = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                file_Name,
                ".jpg",
                storage
        );
        currenPhotoPATH = image.getAbsolutePath();
        return image;
    }

    private void gallery_Add_img(){
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(currenPhotoPATH);
        Uri uri_Content = Uri.fromFile(file);
        intent.setData(uri_Content);
        this.sendBroadcast(intent);
    }

    private void set_Pic(ImageView img){
        int targetW = img.getWidth();
        int targetH = img.getHeight();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(currenPhotoPATH);

        int photoW = options.outWidth;
        int photoH = options.outHeight;

//        int scaleFactor = Math.max(1 , Math.min(photoW/targetW , photoH/targetH ));

        options.inJustDecodeBounds = false;
//        options.inSampleSize = scaleFactor;
        options.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currenPhotoPATH , options);
        img.setImageBitmap(bitmap);
    }
}