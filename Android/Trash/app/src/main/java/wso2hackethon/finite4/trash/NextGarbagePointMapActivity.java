package wso2hackethon.finite4.trash;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by WAKENSYS on 7/18/2017.
 */

public class NextGarbagePointMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {
    Context context;

    NetworkStatChecker n = new NetworkStatChecker();
    DBOperations dbOperations = new DBOperations();
    Data data = new Data();

    private static final long
            INTERVAL = 1000 * 10;

    private static final long
            FASTEST_INTERVAL = 100 * 2;

    LocationRequest
            mLocationRequest;

    GoogleApiClient
            mGoogleApiClient;

    private GoogleMap
            mMap;

    LatLng
            latLng;

    Marker
            myCurrentLocation;

    ImageView
            backButton;

    int position = 0;

    String[]
            garbage_collecting_point_id,
            garbage_collecting_point_name,
            garbage_collecting_point_description,
            garbage_collecting_point_lat,
            garbage_collecting_point_lon,
            garbage_point_driver_tractor_merge_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next_garbage_point_map_layout);

        context = NextGarbagePointMapActivity.this;

        backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButtonOption();
            }
        });

        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                if(myCurrentLocation != null){
                    myCurrentLocation.setPosition(latLng);
                }
                else{
                    myCurrentLocation = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title("My Position"));
                }

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19));

                if (ActivityCompat.checkSelfPermission(NextGarbagePointMapActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(NextGarbagePointMapActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                         int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setBuildingsEnabled(true);
                // mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                // mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        try{
                            String title = marker.getTitle();
                            LOOP : for(int k = 0; k < garbage_collecting_point_id.length; k++){
                                if(title.equalsIgnoreCase(garbage_collecting_point_name[k])){
                                    position = k;
                                    break LOOP;
                                }
                            }

                            final Dialog alertDialog = new Dialog(context);
                            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            alertDialog.setContentView(R.layout.garbage_point_full_layout);
                            alertDialog.setCancelable(true);

                            Window window = alertDialog.getWindow();
                            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            final CardView collectedButton = (CardView) alertDialog.findViewById(R.id.collectedButton);
                            collectedButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new SendCollectingResponse(garbage_collecting_point_id[position], "2", alertDialog).execute();
                                }
                            });

                            final CardView rejectButton = (CardView) alertDialog.findViewById(R.id.rejectButton);
                            rejectButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new SendCollectingResponse(garbage_collecting_point_id[position], "3", alertDialog).execute();
                                }
                            });

                            alertDialog.show();
                        }
                        catch (Exception e){

                        }
                        return false;
                    }
                });
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

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        SharedPreferences spForLogin = getSharedPreferences("loginCredentials", MODE_PRIVATE);
        String userID = spForLogin.getString("user_id", "0");

        new GetAllNextPoints(userID).execute();

        mMap.setBuildingsEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
    }

    public void backButtonOption(){
        Intent i = new Intent(context, MainMenuForTractorActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        backButtonOption();
    }

    class GetAllNextPoints extends AsyncTask<String, Void, String[][]> {
        boolean internetAvailable = false;
        String driverID;
        ProgressDialog progressDialog;

        public GetAllNextPoints(String driverID){
            this.driverID = driverID;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(context, "Loading", "Please wait");
        }

        @Override
        protected String[][] doInBackground(String... urls) {
            if(n.isConnected(context)){
                this.internetAvailable = true;
                String[][] res = dbOperations.getAllNextGarbagePoints(driverID);
                return res;
            }
            else{
                this.internetAvailable = false;
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[][] result) {
            if(result != null){
                try{
                    garbage_collecting_point_id = result[0];
                    garbage_collecting_point_name = result[1];
                    garbage_collecting_point_description = result[2];
                    garbage_collecting_point_lat = result[3];
                    garbage_collecting_point_lon = result[4];
                    garbage_point_driver_tractor_merge_status = result[5];

                    mMap.clear();
                    myCurrentLocation = null;

                    for(int k = 0; k < garbage_collecting_point_id.length; k++){
                        LatLng garbagePoint = new LatLng(Double.parseDouble(garbage_collecting_point_lat[k].trim()), Double.parseDouble(garbage_collecting_point_lon[k].trim()));
                        if(garbage_point_driver_tractor_merge_status[k].equalsIgnoreCase("2")){
                            mMap.addMarker(new MarkerOptions().position(garbagePoint).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(garbage_collecting_point_name[k]).snippet("Collected"));
                        }
                        else if(garbage_point_driver_tractor_merge_status[k].equalsIgnoreCase("1")){
                            mMap.addMarker(new MarkerOptions().position(garbagePoint).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)).title(garbage_collecting_point_name[k]).snippet("Pending"));
                        }

                        else{
                            mMap.addMarker(new MarkerOptions().position(garbagePoint).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title(garbage_collecting_point_name[k]).snippet("Rejected"));
                        }
                    }

                    try{
                        myCurrentLocation = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title("My Position"));
                    }
                    catch (Exception ignored){}
                }
                catch (Exception e){
                    if(this.internetAvailable){
                        Toast.makeText(context, "No points found", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            else if(this.internetAvailable){
                Toast.makeText(context, "No points found", Toast.LENGTH_SHORT).show();
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

    class SendCollectingResponse extends AsyncTask<String, Void, Boolean> {
        boolean internetAvailable = false;
        String pointID, result;
        ProgressDialog progressDialog;
        Dialog alertDialog;

        public SendCollectingResponse(String pointID, String result, Dialog alertDialog){
            this.result = result;
            this.pointID = pointID;

            this.alertDialog = alertDialog;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(context, "Loading", "Please wait");
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            if(n.isConnected(context)){
                this.internetAvailable = true;
                boolean res = dbOperations.setCollectingResponse(pointID, result);
                return res;
            }
            else{
                this.internetAvailable = false;
                return null;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                SharedPreferences spForLogin = getSharedPreferences("loginCredentials", MODE_PRIVATE);
                String userID = spForLogin.getString("user_id", "0");
                new GetAllNextPoints(userID).execute();
            }

            else if(this.internetAvailable){
                Toast.makeText(context, "Unable to complete your action. PLease try again later", Toast.LENGTH_SHORT).show();
            }

            else{
                Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }

            try{
                 alertDialog.dismiss();
                 progressDialog.dismiss();
            }
            catch(Exception ignored){}
        }
    }
}
