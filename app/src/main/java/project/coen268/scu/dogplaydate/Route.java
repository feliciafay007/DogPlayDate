package project.coen268.scu.dogplaydate;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Route extends FragmentActivity implements LocationListener {

    GoogleMap googleMap;
    private final LatLng MY_LOCATION = new LatLng(37.348406, -121.940132);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        googleMap = fm.getMap();

        googleMap.addMarker(new MarkerOptions().position(MY_LOCATION).title("Find Me Here!"));
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(MY_LOCATION, 10);
        googleMap.animateCamera(update);


        // Enabling MyLocation Layer of Google Map
        googleMap.setMyLocationEnabled(true);

        // Getting LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        Location location = locationManager.getLastKnownLocation(provider);
//        Location location = googleMap.getMyLocation();

//        googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
//
////            @Override
////            public void onMyLocationChange(Location location) {
////                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
////                Marker mMarker = googleMap.addMarker(new MarkerOptions().position(loc).title("Here"));
////                if(googleMap != null){
////                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
////                }
////            }
//                @Override
//                public void onMyLocationChange(Location lastKnownLocation) {
//                    CameraUpdate myLoc = CameraUpdateFactory.newCameraPosition(
//                            new CameraPosition.Builder().target(new LatLng(lastKnownLocation.getLatitude(),
//                                    lastKnownLocation.getLongitude())).zoom(6).build());
//                    googleMap.moveCamera(myLoc);
//                    googleMap.setOnMyLocationChangeListener(null);
//                }
//        });

        if (location != null) {
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(provider, 20000, 0, this);
    }



    @Override
    public void onLocationChanged(Location location) {

        TextView tvLocation = (TextView) findViewById(R.id.tv_location);

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);

        // Showing the current location in Google Map
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        // Setting latitude and longitude in the TextView tv_location
        tvLocation.setText("Latitude:" +  latitude  + ", Longitude:"+ longitude );
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_route, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

//import android.location.Location;
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//public class Route extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
//
//    private GoogleApiClient mGoogleApiClient;
//    private Location mLastLocation;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_route);
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                mGoogleApiClient);
//
//
//        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        GoogleMap googleMap = fm.getMap();
//        if (mLastLocation != null) {
//            System.out.println("lat: " + String.valueOf(mLastLocation.getLatitude()));
//            System.out.println("long: " + String.valueOf(mLastLocation.getLongitude()));
//            googleMap.addMarker(new MarkerOptions().position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())).title("Here"));
//        }
//    }
//
//    @Override
//    public void onConnected(Bundle bundle) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_route);
////        mGoogleApiClient = new GoogleApiClient.Builder(this)
////                .addConnectionCallbacks(this)
////                .addOnConnectionFailedListener(this)
////                .addApi(LocationServices.API)
////                .build();
////
////        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
////                mGoogleApiClient);
////
////
////        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
////        GoogleMap googleMap = fm.getMap();
////        if (mLastLocation != null) {
////            System.out.println("lat: " + String.valueOf(mLastLocation.getLatitude()));
////            System.out.println("long: " + String.valueOf(mLastLocation.getLongitude()));
////            googleMap.addMarker(new MarkerOptions().position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())).title("Here"));
////        }
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//
//    }
//}
