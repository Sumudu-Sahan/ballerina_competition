package wso2hackethon.finite4.trash;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GarbageMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {
    Context context;

    NetworkStatChecker n = new NetworkStatChecker();
    DBOperations dbOperations = new DBOperations();
    Data data = new Data();

    private static final long
            INTERVAL = 1000 * 60;

    private static final long
            FASTEST_INTERVAL = 100 * 2;

    LocationRequest
            mLocationRequest;

    GoogleApiClient
            mGoogleApiClient;

    private GoogleMap
            mMap;

    LatLng latLng;

    Marker
            myCurrentLocation;

    String[]
            tractorLatList,
            tractorLonList,
            garbageLatList,
            garbageLonList;

    ArrayList<Marker> tractors = new ArrayList<>();

    Timer timer = new Timer();

    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garbage_map);

        context = GarbageMapActivity.this;

        backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButtonOption();
            }
        });

        Intent i = getIntent();
        if(i != null){
            try{
                tractorLatList = i.getStringArrayExtra("tractorLatList");
                tractorLonList = i.getStringArrayExtra("tractorLonList");
                garbageLatList = i.getStringArrayExtra("garbageLatList");
                garbageLonList = i.getStringArrayExtra("garbageLonList");
            }
            catch (Exception e){

            }
        }

        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onBackPressed() {
        backButtonOption();
    }

    public void backButtonOption(){
        try{
            timer.cancel();
        }
        catch (Exception e){

        }
        Intent i = new Intent(context, MainMenuForPeopleActivity.class);
        startActivity(i);
        finish();
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
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));

                if (ActivityCompat.checkSelfPermission(GarbageMapActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GarbageMapActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        String title = marker.getTitle();
                        System.out.println("marker: " + marker);
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
        new GetAllGarbageRegularPoints().execute();
        new GetAllGarbagePoints().execute();
        new GetAllTractors().execute();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            new GetAllGarbageRegularPoints().execute();
                            new GetAllGarbagePoints().execute();
                            new GetAllTractors().execute();
                        }
                        catch (Exception e){

                        }
                    }
                });
            }
        }, 20000, 20000);

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

    class GetAllTractors extends AsyncTask<String, Void, String[][]> {
        boolean internetAvailable = false;
        //ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            //progressDialog = ProgressDialog.show(context, "Loading", "Please wait");
        }

        @Override
        protected String[][] doInBackground(String... urls) {
            if(n.isConnected(context)){
                this.internetAvailable = true;
                String[][] res = dbOperations.getAllTractors();
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
                    String[] tractor_id = result[0];
                    String[] tractor_image = result[1];
                    String[] tractor_lat = result[2];
                    String[] tractor_lon = result[3];
                    String[] tractor_type = result[4];

                    tractors.clear();
                    mMap.clear();
                    myCurrentLocation = null;

                    for(int k = 0; k < tractor_id.length; k++){
                        LatLng tractorLatLng = new LatLng(Double.parseDouble(tractor_lat[k].trim()), Double.parseDouble(tractor_lon[k].trim()));
                        try{
                            if(tractors.get(k) != null){
                                tractors.get(k).setPosition(tractorLatLng);
                            }

                            else{
                                tractors.add(mMap.addMarker(new MarkerOptions().position(tractorLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.tractor_point)).title(tractor_type[k])));
                            }
                        }
                        catch (Exception e){
                            tractors.add(mMap.addMarker(new MarkerOptions().position(tractorLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.tractor_point)).title(tractor_type[k])));
                        }
                    }
                    try{
                        myCurrentLocation = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title("My Position"));
                    }
                    catch (Exception ignored){}
                    new GetAllGarbageRegularPoints().execute();
                    new GetAllGarbagePoints().execute();
                }
                catch (Exception e){
                    e.printStackTrace();
                    if(this.internetAvailable){
                        Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            else if(this.internetAvailable){
                Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
            }

            else{
                Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }

            try{
                //progressDialog.dismiss();
            }
            catch(Exception ignored){}
        }
    }

    class GetAllGarbagePoints extends AsyncTask<String, Void, String[][]> {
        boolean internetAvailable = false;
       // ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            //progressDialog = ProgressDialog.show(context, "Loading", "Please wait");
        }

        @Override
        protected String[][] doInBackground(String... urls) {
            if(n.isConnected(context)){
                this.internetAvailable = true;
                String[][] res = dbOperations.getAllGarbagePoints();
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
                    String[] garbage_post_id = result[0];
                    String[] garbage_post_user_id = result[1];
                    String[] garbage_post_title = result[2];
                    String[] garbage_post_description = result[3];
                    String[] garbage_post_image = result[4];
                    String[] garbage_post_lat = result[5];
                    String[] garbage_post_lon = result[6];
                    String[] garbage_post_status = result[7];

                    for(int k = 0; k < garbage_post_id.length; k++){
                        LatLng garbagePoint = new LatLng(Double.parseDouble(garbage_post_lat[k].trim()), Double.parseDouble(garbage_post_lon[k].trim()));
                       if(garbage_post_status[k].equalsIgnoreCase("Solved")){
                           mMap.addMarker(new MarkerOptions().position(garbagePoint).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(garbage_post_title[k]).snippet("Collected"));
                       }
                        else{
                           mMap.addMarker(new MarkerOptions().position(garbagePoint).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title(garbage_post_title[k]).snippet("Not Collected"));
                       }
                    }
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
               // progressDialog.dismiss();
            }
            catch(Exception ignored){}
        }
    }

    class GetAllGarbageRegularPoints extends AsyncTask<String, Void, String[][]> {
        boolean internetAvailable = false;
        //ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            //progressDialog = ProgressDialog.show(context, "Loading", "Please wait");
        }

        @Override
        protected String[][] doInBackground(String... urls) {
            if(n.isConnected(context)){
                this.internetAvailable = true;
                String[][] res = dbOperations.getAllGarbageRegularPoints();
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
                    String[] garbage_collecting_point_id = result[0];
                    String[] garbage_collecting_point_name = result[1];
                    String[] garbage_collecting_point_description = result[2];
                    String[] garbage_collecting_point_lat = result[3];
                    String[] garbage_collecting_point_lon = result[4];

                    for(int k = 0; k < garbage_collecting_point_id.length; k++){
                        LatLng garbagePoint = new LatLng(Double.parseDouble(garbage_collecting_point_lat[k].trim()), Double.parseDouble(garbage_collecting_point_lon[k].trim()));
                        mMap.addMarker(new MarkerOptions().position(garbagePoint).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)).title(garbage_collecting_point_name[k]).snippet(garbage_collecting_point_description[k]));
                    }
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
                //progressDialog.dismiss();
            }
            catch(Exception ignored){}
        }
    }
}
