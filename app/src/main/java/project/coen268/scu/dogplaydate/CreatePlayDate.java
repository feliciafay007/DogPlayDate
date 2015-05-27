package project.coen268.scu.dogplaydate;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import com.google.android.gms.maps.SupportMapFragment;
import com.parse.Parse;
import com.parse.ParseInstallation;
import java.util.Calendar;
import android.app.TimePickerDialog;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

// reference
// http://javapapers.com/android/find-places-nearby-in-google-maps-using-google-places-apiandroid-app/

// Server Key: AIzaSyDVGQDiBMRR0pXxAOrdWPwHaPiQXJMQc08
// Browser Key: AIzaSyAHkSb33zot8zfyDca3TmYO09_C1PXlYB8
public class CreatePlayDate extends FragmentActivity implements OnMapReadyCallback, LocationListener{
    private  final LatLng LOCATION_UNIV = new LatLng(37.349642, -121.938987);
    private  final LatLng LOCATION_BUILDING = new LatLng(37.348190, -121.937975);
    double latitude = LOCATION_UNIV.latitude;
    double longitude = LOCATION_UNIV.longitude;
    private GoogleMap googleMap;
    private Button btnStartDate;
    private final int DATE_DIALOG_ID = 999;
    private int year;
    private int month;
    private int day;
    private EditText editTextSearchPlace;
    private int PROXIMITY_RADIUS = 2000;

    // NOTE: server key is recommend, though it seems that server key or browser key both work fine.
    private static final String GOOGLE_API_KEY = "AIzaSyDVGQDiBMRR0pXxAOrdWPwHaPiQXJMQc08"; //server key
    //private static final String GOOGLE_API_KEY =  "AIzaSyAHkSb33zot8zfyDca3TmYO09_C1PXlYB8"; // browser key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_play_date);
        //Parse.initialize(this, "DgaXmRWHs3HaCC2buvdgC1ji2LPlItoxgCol7DcJ", "8a4PcTnqh14fJC5ekKmgxR7pDWgMTl27w2eKZEqK");
        //ParseInstallation.getCurrentInstallation().saveInBackground();

        editTextSearchPlace =  (EditText) findViewById(R.id.editTextSearchPlace);
        googleMap = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
//        if (location != null) {
//            onLocationChanged(location);
//        }
//        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
        onMapReady(googleMap);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.setMyLocationEnabled(true);
        LatLng currentLatLng = new LatLng(latitude, longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14));
        map.addMarker(new MarkerOptions()
                .title("Welcome!")
                .snippet("Let's play with the dog.")
                .position(currentLatLng));
    }


    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
    }

//    public void onClickSearch(View v) {
//
//        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_BUILDING, 17);
//        mGoogleMap.animateCamera(update);
//    }


    public void onClickSearch(View v) {

        // NOTE: the TYPE keyword should be lower case letters
        String type = editTextSearchPlace.getText().toString().toLowerCase();
        if (type == null || type.isEmpty()) {
            type = "park";
        }
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&types=" + type);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + GOOGLE_API_KEY);
        Toast.makeText(getApplicationContext(), googlePlacesUrl.toString(), Toast.LENGTH_SHORT).show();
        System.out.println(googlePlacesUrl.toString());
        GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
        Object[] toPass = new Object[2];
        toPass[0] = googleMap;
        toPass[1] = googlePlacesUrl.toString();
        googlePlacesReadTask.execute(toPass);
    }

    public void onClickStartDate(View v) {
        DialogFragment dialogFragment = new StartDatePicker();
        dialogFragment.show(getFragmentManager(), "start_date_picker");
    }

    //--start
    Calendar c = Calendar.getInstance();
//    static int startYear = Calendar.YEAR;
//    static int startMonth = Calendar.MONTH;
//    static int startDay = Calendar.DAY_OF_MONTH;

    int startYear = c.get(Calendar.YEAR);
    int startMonth = c.get(Calendar.MONTH);
    int startDay = c.get(Calendar.DAY_OF_MONTH);

   // public static class StartDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener{
   class StartDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            DatePickerDialog dialog = new DatePickerDialog(CreatePlayDate.this, this, startYear, startMonth, startDay);
            return dialog;

        }
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // Do something with the date chosen by the user
            startYear = year;
            startMonth = monthOfYear;
            startDay = dayOfMonth;
            System.out.println("picked time = " + startYear + "-" + startMonth + "-" + startDay);
            Log.i("FF", "picked time = " + startYear + "-" + startMonth + "-" + startDay);
        }
    }

    //---end

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                break;
            case R.id.create_play_date:
                break;
            case R.id.send_message:
                Intent intent = new Intent(CreatePlayDate.this, SendMessage.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
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
}