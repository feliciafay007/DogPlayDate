package project.coen268.scu.dogplaydate;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * wenyi
 */

public class GooglePlacesReadTask extends AsyncTask<Object, Integer, String> {
    String googlePlacesData = null;
    GoogleMap googleMap;
    //Felicia
    TreeSet<String> markerKeySet;


    public GooglePlacesReadTask (TreeSet<String> paraMarkerSet) {
        this.markerKeySet = paraMarkerSet;
    }

    @Override
    protected String doInBackground(Object... inputObj) {
        try {
            googleMap = (GoogleMap) inputObj[0];
            String googlePlacesUrl = (String) inputObj[1];
            Http http = new Http();
            googlePlacesData = http.read(googlePlacesUrl);
        } catch (Exception e) {
            Log.d("Google Place Read Task", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        PlacesDisplayTask placesDisplayTask = new PlacesDisplayTask(markerKeySet);
        Object[] toPass = new Object[2];
        toPass[0] = googleMap;
        toPass[1] = result;
        placesDisplayTask.execute(toPass);
    }
}