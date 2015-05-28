package project.coen268.scu.dogplaydate;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.parse.Parse;
import com.parse.ParseInstallation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
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
public class CreatePlayDate extends FragmentActivity implements
        OnMapReadyCallback,
        LocationListener{
    private  final LatLng LOCATION_UNIV = new LatLng(37.349642, -121.938987);
    private  final LatLng LOCATION_BUILDING = new LatLng(37.348190, -121.937975);
    double latitude = LOCATION_UNIV.latitude;
    double longitude = LOCATION_UNIV.longitude;
    private GoogleMap googleMap;
    private int PROXIMITY_RADIUS = 2000;
    private Button btnStartDate;
    private EditText editTextSearchPlace;
    private DatePickerDialog datePickerDialogStart;
    private DatePickerDialog datePickerDialogEnd;
    private TimePickerDialog timePickerDialogStart;
    private TimePickerDialog timePickerDialogEnd;
    private EditText editTextStartDate;
    private EditText editTextEndDate;
    private EditText editTextStartTime;
    private EditText editTextEndTime;
    private SimpleDateFormat simpleDateFormat;
    // NOTE: server key is recommend, though it seems that server key or browser key both work fine.
    private static final String GOOGLE_API_KEY = "AIzaSyDVGQDiBMRR0pXxAOrdWPwHaPiQXJMQc08"; //server key
    //private static final String GOOGLE_API_KEY =  "AIzaSyAHkSb33zot8zfyDca3TmYO09_C1PXlYB8"; // browser key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_play_date);
        Parse.initialize(this, "DgaXmRWHs3HaCC2buvdgC1ji2LPlItoxgCol7DcJ", "8a4PcTnqh14fJC5ekKmgxR7pDWgMTl27w2eKZEqK");
        ParseInstallation.getCurrentInstallation().saveInBackground();

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

        //--
        onMapReady(googleMap);

        //--
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
//        textViewStartDate = (TextView) findViewById(R.id.textViewStartDate);
//        textViewEndDate= (TextView) findViewById(R.id.textViewEndDate);
        editTextStartDate = (EditText) findViewById(R.id.editTextStartDate);
        editTextEndDate = (EditText) findViewById(R.id.editTextEndDate);
        editTextStartTime = (EditText) findViewById(R.id.editTextStartTime);
        editTextEndTime = (EditText) findViewById(R.id.editTextEndTime);

        // in xml file, to hide keyboard,  android:focusableInTouchMode="false"
        prepareDatePickerDialog();
        prepareTimePickerDialog();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.setMyLocationEnabled(true);
        LatLng currentLatLng = new LatLng(latitude, longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14));
        map.addMarker(new MarkerOptions()
                .title("Welcome!")
                .snippet("Let's play with the dog.")
                .position(currentLatLng))
                .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.dog_icon));
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
        datePickerDialogStart.show();
    }

    public void onClickEndDate(View v) {
        datePickerDialogEnd.show();
    }

    public void onClickStartTime(View v) {
        timePickerDialogStart.show();
    }

    public void onClickEndTime(View v) {
        timePickerDialogEnd.show();
    }

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

//    @Override
//    public boolean onMarkerClick (Marker marker) {
//        System.out.println("FF_MARKER" + marker.getTitle() +"," + marker.getSnippet());
//        Toast.makeText(getApplicationContext(), marker.getTitle() +"," + marker.getSnippet(), Toast.LENGTH_SHORT).show();
//        return true;
//    }

    void prepareDatePickerDialog() {
        Calendar newCalendar = Calendar.getInstance(); // use today's date as default
        datePickerDialogStart = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance(); // set the user's date as the calendar date
                newDate.set(year, monthOfYear, dayOfMonth);
                editTextStartDate.setText(simpleDateFormat.format(newDate.getTime()));
                // default: at the same day
                editTextEndDate.setText(simpleDateFormat.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialogEnd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editTextEndDate.setText(simpleDateFormat.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    void prepareTimePickerDialog() {
        Calendar newCalendar = Calendar.getInstance();
        timePickerDialogStart = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener(){
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar newDate = Calendar.getInstance();
//                newDate.set(hourOfDay,minute);
/*
                Date now = new Date(); // java.util.Date, NOT java.sql.Date or java.sql.Timestamp!
                String format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(now);
                String format2 = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z").format(now);
                String format3 = new SimpleDateFormat("yyyyMMddHHmmss").format(now);
*/
                newDate.set(newDate.get(Calendar.YEAR), newDate.get(Calendar.MONTH), newDate.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
                editTextStartTime.setText(hourOfDay + " : " + minute);
            }
        }, newCalendar.get(Calendar.HOUR), newCalendar.get(Calendar.MINUTE),false );

        timePickerDialogEnd= new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener(){
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(newDate.get(Calendar.YEAR), newDate.get(Calendar.MONTH), newDate.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
                editTextEndTime.setText(hourOfDay + " : " + minute);
            }
        }, newCalendar.get(Calendar.HOUR), newCalendar.get(Calendar.MINUTE),false );

    }
}