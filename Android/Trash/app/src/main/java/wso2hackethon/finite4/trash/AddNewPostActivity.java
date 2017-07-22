package wso2hackethon.finite4.trash;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by WAKENSYS on 7/20/2017.
 */

public class AddNewPostActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    Data data = new Data();
    DBOperations dbOperations = new DBOperations();
    NetworkStatChecker n = new NetworkStatChecker();

    private CameraSurfaceTextureListener mCameraSurfaceTextureListener;
    private OrientationEventListener mOrientationEventListener;
    public TextureView mTextureView;
    private Camera mCamera;
    private static int mCamId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private Camera.CameraInfo mCurrentCameraInfo;
    private int mOrientation;

    Context context;
    File pictureFile;
    AlertDialog signatureDialogView;

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    private boolean mTurnFlash;

    ImageView image;

    CardView submitButton;

    private static final long
            INTERVAL = 1000 * 60;

    private static final long
            FASTEST_INTERVAL = 100 * 2;

    LocationRequest
            mLocationRequest;

    GoogleApiClient
            mGoogleApiClient;

    double
            latitude = 0,
            longitude = 0;

    String fileName = "";

    String userID = "";

    EditText
            titleField,
            descriptionField;

    ImageView backButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_post_layout);
        context = AddNewPostActivity.this;

        backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButtonOption();
            }
        });

        titleField = (EditText) findViewById(R.id.titleField);
        descriptionField = (EditText) findViewById(R.id.descriptionField);

        SharedPreferences logcatStart = getSharedPreferences("loginCredentials", MODE_PRIVATE);
        userID = logcatStart.getString("user_id", "");

        fileName += (userID + getTime() + "postIMG.jpg");

        submitButton = (CardView) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleField.getText().toString().trim();
                String description = descriptionField.getText().toString().trim();

                if(title.equalsIgnoreCase("") || description.equalsIgnoreCase("")){
                    if(title.equalsIgnoreCase("")){
                        titleField.setError("Please fill this field");
                    }
                    else{
                        titleField.setError(null);
                    }

                    if(description.equalsIgnoreCase("")){
                        descriptionField.setError("Please fill this field");
                    }
                    else{
                        descriptionField.setError(null);
                    }
                }

                else{
                    new AddNewPost(userID, title, description, String.valueOf(latitude), String.valueOf(longitude), fileName).execute();
                }
            }
        });

        image = (ImageView) findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureFile = new File(data.getSTORAGE_PATH(), fileName);

                LayoutInflater factory = LayoutInflater.from(context);
                final View signatureDialog = factory.inflate(R.layout.camera_layout, null);
                signatureDialogView = new AlertDialog.Builder(context).create();
                signatureDialogView.setView(signatureDialog);

                mCameraSurfaceTextureListener = new CameraSurfaceTextureListener();
                mTextureView = (TextureView) signatureDialog.findViewById(R.id.texture_view);
                registerOrientationListener();
                if (ContextCompat.checkSelfPermission(AddNewPostActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    startCam();
                } else {
                    //otherwise send a request to user
                    ActivityCompat.requestPermissions(AddNewPostActivity.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                }
                signatureDialogView.show();
            }
        });

        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onBackPressed() {
        backButtonOption();
    }

    public void backButtonOption(){
        Intent i = new Intent(context, MainMenuForPeopleActivity.class);
        startActivity(i);
        finish();
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
        Camera.Parameters parameters = mCamera.getParameters();
        Camera.Size size = getOptimalPreviewSize(parameters.getSupportedPreviewSizes(), width, height);
        parameters.setPreviewSize(size.width, size.height);
        if (parameters.getSupportedPictureSizes().size() > 0) {
            Camera.Size pictureSize = parameters.getSupportedPictureSizes().get(0);
            parameters.setPictureSize(pictureSize.width, pictureSize.height);
        }
        mCamera.setParameters(parameters);
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
        Toast.makeText(context, "No camera available", Toast.LENGTH_SHORT).show();
        //DialogHelper.showDialog("Camera Info", "No camera available", this);
    }

    private void takeImage() {
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                try {
                    // convert byte array into bitmap
                    Bitmap loadedImage = null;
                    Bitmap rotatedBitmap = null;
                    loadedImage = BitmapFactory.decodeByteArray(data, 0, data.length);
                    Matrix rotateMatrix = new Matrix();
                    rotateMatrix.postRotate(getRotation());

                    rotatedBitmap = Bitmap.createBitmap(loadedImage, 0, 0, loadedImage.getWidth(), loadedImage.getHeight(), rotateMatrix, false);
                    boolean success = true;
                    if (success) {
                        pictureFile.createNewFile();
                        signatureDialogView.dismiss();
                    } else {
                        Toast.makeText(context, "Image Not saved", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ByteArrayOutputStream ostream = new ByteArrayOutputStream();
                    // save image into gallery
                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);

                    FileOutputStream fout = new FileOutputStream(pictureFile);
                    fout.write(ostream.toByteArray());
                    fout.close();
                    ContentValues values = new ContentValues();

                    values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                    values.put(MediaStore.MediaColumns.DATA, pictureFile.getAbsolutePath());

                    getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                    Picasso
                            .with(context)
                            .load(pictureFile)
                            .error(R.drawable.broken_image)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .fit()
                            .into(image);

                    signatureDialogView.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

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
                       /* Log.d("!!!!", "rotation!!! lastOrientation:"
                                + lastOrientation + " mOrientation:"
                                + mOrientation + " orientaion:"
                                + orientation);*/
                    }
                }
            };
        }

        if (mOrientationEventListener.canDetectOrientation()) {
            mOrientationEventListener.enable();
        }
    }

    private void startCam() {
        if (mTextureView.isAvailable()) {
            setupCamera(mTextureView.getSurfaceTexture(), mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            mTextureView.setSurfaceTextureListener(mCameraSurfaceTextureListener);
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
                    Toast.makeText(context, "No permission to open the camera", Toast.LENGTH_SHORT).show();
                    backButtonOption();
                }
                break;
        }
    }

    class AddNewPost extends AsyncTask<String, Void, Boolean> {
        boolean internetAvailable = false;
        ProgressDialog progressDialog;

        String
                userID,
                title,
                description,
                lat,
                lon,
                fileName;

        public AddNewPost(
                String userID,
                String title,
                String description,
                String lat,
                String lon,
                String fileName){

            this.userID = userID;

            this.title = title;
            this.description = description;
            this.lat = lat;
            this.lon = lon;
            this.fileName = fileName;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(context, "Submitting", "Please wait");
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            if(n.isConnected(context)){
                this.internetAvailable = true;
                boolean res = dbOperations.addNewPost(userID, title, description, lat, lon, fileName);
                if(res){
                    uploadImage(fileName);
                    sharePhotoToFacebook(title + description);
                }
                return res;
            }
            else{
                this.internetAvailable = false;
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                Toast.makeText(context, "Successfully added", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(context, MainMenuForPeopleActivity.class);
                startActivity(i);
                finish();
            }

            else if(this.internetAvailable){
                Toast.makeText(context, "Unable to add your post. Please try again later", Toast.LENGTH_SHORT).show();
            }

            else{
                Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }

            try{
                progressDialog.dismiss();
            }
            catch(Exception ignored){}
        }
    }

    public void uploadImage(String fileNameAsString) {
        String fileName = fileNameAsString;
        HttpURLConnection conn;
        DataOutputStream dos;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = null;
        try {
            sourceFile = pictureFile;
            try {
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(data.getSERVER_PHP_ROOT_PATH() + "uploadImage.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "close");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                String serverResponseMessage = conn.getResponseMessage();
                System.out.println(serverResponseMessage + " code is:" + conn.getResponseCode());
                fileInputStream.close();
                dos.flush();
                dos.close();
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        } catch (Exception e) {
        }
    }

    private void sharePhotoToFacebook(String title){
        Bitmap bitmap = null;
        File f = new File(data.getSTORAGE_PATH(), fileName);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .setCaption(title)
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareApi.share(content, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(context, "Successfully Shared on Facebook", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(context, "Cancelled Share on Facebook", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(context, "Unable to Share on Facebook", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new com.google.android.gms.location.LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();


                if (ActivityCompat.checkSelfPermission(AddNewPostActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddNewPostActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                         int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public String getTime(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT0:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("KK:mm");
        date.setTimeZone(TimeZone.getTimeZone("GMT0:00"));
        int currentHour = cal.get(Calendar.HOUR);
        int currentMinutes = cal.get(Calendar.MINUTE);
        int currentSeconds = cal.get(Calendar.SECOND);
        return(currentHour + ":" + currentMinutes + ":" + currentSeconds);
    }
}
