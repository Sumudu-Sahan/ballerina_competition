package wso2hackethon.finite4.trash;

/**
 * Created by Sumudu on 7/22/2017.
 */

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Pair;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by Sumudu on 7/22/2017.
 */
public class CameraActivity extends Activity {
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static String TAG = "CameraActivity";
    String imgName = "";

    private CameraSurfaceTextureListener mCameraSurfaceTextureListener;
    private OrientationEventListener mOrientationEventListener;
    public TextureView mTextureView;
    private Camera mCamera;
    private static int mCamId = Camera.CameraInfo.CAMERA_FACING_BACK;

    private Camera.CameraInfo mCurrentCameraInfo;
    private Camera.Size mOptimalPreviewSize;
    private boolean mTurnFlash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.camera_layout);

        mCameraSurfaceTextureListener = new CameraSurfaceTextureListener();
        mTextureView = (TextureView) findViewById(R.id.texture_view);

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerOrientationListener();

        //first check permission has granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCam();
        } else {
            //otherwise send a request to user
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCam();
                } else {
                    System.exit(0);
                }
                break;
        }
    }

    private void startCam() {
        if (mTextureView.isAvailable()) {
            setupCamera(mTextureView.getSurfaceTexture(), mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            mTextureView.setSurfaceTextureListener(mCameraSurfaceTextureListener);
        }
    }

    private int mOrientation;

    private void registerOrientationListener() {
        if (mOrientationEventListener == null) {
            mOrientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
                @Override
                public void onOrientationChanged(int orientation) {
                    int lastOrientation = mOrientation;

                    if (orientation >= 315 || orientation < 45) {
                        if (mOrientation != Surface.ROTATION_0) {
                            mOrientation = Surface.ROTATION_0;
                        }
                    } else if (orientation >= 45 && orientation < 135) {
                        if (mOrientation != Surface.ROTATION_90) {
                            mOrientation = Surface.ROTATION_90;
                        }
                    } else if (orientation >= 135 && orientation < 225) {
                        if (mOrientation != Surface.ROTATION_180) {
                            mOrientation = Surface.ROTATION_180;
                        }
                    } else if (mOrientation != Surface.ROTATION_270) {
                        mOrientation = Surface.ROTATION_270;
                    }

                    if (lastOrientation != mOrientation) {
                        Log.d("!!!!", "rotation!!! lastOrientation:"
                                + lastOrientation + " mOrientation:"
                                + mOrientation + " orientaion:"
                                + orientation);
                    }
                }
            };
        }

        if (mOrientationEventListener.canDetectOrientation()) {
            mOrientationEventListener.enable();
        }
    }

    private void takeImage() {
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            private File imageFile;

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                try {
                    // convert byte array into bitmap
                    Bitmap loadedImage = null;
                    Bitmap rotatedBitmap = null;
                    loadedImage = BitmapFactory.decodeByteArray(data, 0, data.length);
                    // rotate Image
                    Matrix rotateMatrix = new Matrix();
                    rotateMatrix.postRotate(getRotation());
                    rotatedBitmap = Bitmap.createBitmap(loadedImage, 0, 0, loadedImage.getWidth(), loadedImage.getHeight(), rotateMatrix, false);
                    String state = Environment.getExternalStorageState();
                    File folder = null;
                    if (state.contains(Environment.MEDIA_MOUNTED)) {
                        folder = new File(Environment
                                .getExternalStorageDirectory() + "/Demo");
                    } else {
                        folder = new File(Environment
                                .getExternalStorageDirectory() + "/Demo");
                    }

                    boolean success = true;
                    if (!folder.exists()) {
                        success = folder.mkdirs();
                    }
                    if (success) {
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        imageFile = new File(folder.getAbsolutePath()
                                + File.separator
                                + timeStamp
                                + "Image.jpg");

                        imageFile.createNewFile();

                        Toast.makeText(getApplicationContext(), "Taken an image", Toast.LENGTH_SHORT).show();

                        //DialogHelper.showDialog("Success", "Taken an image", CameraActivity.this);
                    } else {
                        Toast.makeText(CameraActivity.this, "Image Not saved",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ByteArrayOutputStream ostream = new ByteArrayOutputStream();

                    // save image into gallery
                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);

                    FileOutputStream fout = new FileOutputStream(imageFile);
                    fout.write(ostream.toByteArray());
                    fout.close();
                    ContentValues values = new ContentValues();

                    values.put(MediaStore.Images.Media.DATE_TAKEN,
                            System.currentTimeMillis());
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                    values.put(MediaStore.MediaColumns.DATA,
                            imageFile.getAbsolutePath());

                    getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        mOrientationEventListener.disable();
        releaseCam();
    }

    private void releaseCam() {
        if (mCamera != null) {
            mTurnFlash = false;
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }


    public void onFlashClick(View view) {

        if (mCamera != null) {

            mTurnFlash = mTurnFlash ? false : true;
            Camera.Parameters p = mCamera.getParameters();
            if (mTurnFlash) {
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            } else {
                p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            }

            mCamera.setParameters(p);
        }
    }

    public void onCaptureClick(View view) {
        takeImage();
    }

    public void onSwitchCameraClick(View view) {

        mCamId = (mCamId == Camera.CameraInfo.CAMERA_FACING_BACK ? Camera.CameraInfo.CAMERA_FACING_FRONT : Camera.CameraInfo.CAMERA_FACING_BACK);
        if (hasCamera(mCamId)) {
            releaseCam();
            startCam();
            return;
        }

        Toast.makeText(getApplicationContext(), "No camera available", Toast.LENGTH_SHORT).show();
       // DialogHelper.showDialog("Camra Info", "No camera available", this);
    }


    class CameraSurfaceTextureListener implements TextureView.SurfaceTextureListener {

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            releaseCam();
            return true;
        }


        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Log.d("!!!!", "onSurfaceTextureAvailable!!!");
            setupCamera(surface, width, height);

        }
    }

    private void setupCamera(SurfaceTexture surface, int width, int height) {
        Pair<Camera.CameraInfo, Integer> backCamera = getCamera(mCamId);
        mCurrentCameraInfo = backCamera.first;
        mCamera = Camera.open(mCamId);
        cameraDisplayRotation();

        try {
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
        } catch (IOException ioe) {
            // Something bad happened
        }

        if (mCamera == null) {
            return;
        }

//        Camera.Parameters parameters = mCamera.getParameters();
//        mOptimalPreviewSize = getOptimalPreviewSize(parameters.getSupportedPictureSizes(), width, height);
//        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//        parameters.setPreviewSize(mOptimalPreviewSize.width, mOptimalPreviewSize.height);
//        mCamera.setParameters(parameters);
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int width, int height) {

        List<Camera.Size> collectorSizes = new ArrayList<>();

        for (Camera.Size size : sizes) {
            if (width > height) {
                if (size.width > width && size.height > height) {
                    collectorSizes.add(size);
                }
            } else {
                if (size.width > height && size.height > width) {
                    collectorSizes.add(size);
                }
            }
        }

        if (collectorSizes.size() > 0) {
            return Collections.min(collectorSizes, new Comparator<Camera.Size>() {
                @Override
                public int compare(Camera.Size lhs, Camera.Size rhs) {
                    return Long.signum(lhs.width * lhs.height - rhs.width * rhs.height);
                }
            });
        }
        return sizes.get(0);
    }

    private int getRotation() {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 0;
                break;
            case Surface.ROTATION_90:
                degree = 90;
                break;
            case Surface.ROTATION_180:
                degree = 180;
                break;
            case Surface.ROTATION_270:
                degree = 270;
                break;

            default:
                break;
        }

        if (mCurrentCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            // frontFacing
            rotation = (mCurrentCameraInfo.orientation + degree) % 330;
            rotation = (360 - rotation) % 360;
            if (rotation > 90 && rotation <= 180) {
                rotation = 180;
            } else if (rotation > 180 && rotation <= 360) {
                rotation = 0;
            } else if (rotation == 90) {
                rotation = -90;
            }
        } else {
            // Back-facing
            rotation = (mCurrentCameraInfo.orientation - degree + 360) % 360;
        }
        return rotation;
    }

    private int cameraDisplayRotation() {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
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

        if (mCurrentCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            // frontFacing
            rotation = (mCurrentCameraInfo.orientation + degrees) % 330;
            rotation = (360 - rotation) % 360;

            if (rotation > 90 && rotation <= 180) {
                rotation = 180;
            } else if (rotation > 180 && rotation <= 360) {
                rotation = 0;
            }

        } else {
            // Back-facing
            rotation = (mCurrentCameraInfo.orientation - degrees + 360) % 360;
        }
        mCamera.setDisplayOrientation(rotation);

        return degrees;
    }

    private Pair<Camera.CameraInfo, Integer> getCamera(int facing) {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        final int numberOfCameras = Camera.getNumberOfCameras();

        for (int i = 0; i < numberOfCameras; ++i) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == facing) {
                return new Pair<Camera.CameraInfo, Integer>(cameraInfo, Integer.valueOf(i));
            }
        }
        return null;
    }

    private boolean hasCamera(int facing) {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        final int numberOfCameras = Camera.getNumberOfCameras();

        for (int i = 0; i < numberOfCameras; ++i) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == facing) {
                return true;
            }
        }
        return false;
    }

}



