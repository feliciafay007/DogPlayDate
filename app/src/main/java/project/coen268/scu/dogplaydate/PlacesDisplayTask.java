package project.coen268.scu.dogplaydate;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//public class PlacesDisplayTask extends AsyncTask<Object, Integer, List<HashMap<String, String>>> {
public class PlacesDisplayTask extends AsyncTask<Object, Integer, Void> {

    JSONObject googlePlacesJson;
    GoogleMap googleMap;
    //Felicia
    //HashMap<String, Marker> markerHashMap;
    ArrayList<Marker> markerARRList;
    List<HashMap<String, String>> googlePlacesList = null;

    public PlacesDisplayTask (ArrayList<Marker> paraArrList) {
        this.markerARRList = paraArrList;
    }

    @Override
    // protected List<HashMap<String, String>> doInBackground(Object... inputObj) {
    protected Void doInBackground(Object... inputObj) {

        //List<HashMap<String, String>> googlePlacesList = null;
        Places placeJsonParser = new Places();
        try {
            googleMap = (GoogleMap) inputObj[0];
            googlePlacesJson = new JSONObject((String) inputObj[1]);
            googlePlacesList = placeJsonParser.parse(googlePlacesJson);
        } catch (Exception e) {
            Log.d("FFFF 1111 Exception", e.toString());
        }
        //return googlePlacesList;
        return null;
    }

    @Override
    // protected void onPostExecute(List<HashMap<String, String>> list) {
    protected void onPostExecute(Void v) {
        List<HashMap<String, String>> list = googlePlacesList;
        googleMap.clear();
        try {
            for (int i = 0; i < list.size(); i++) {
                MarkerOptions markerOptions = new MarkerOptions();
                HashMap<String, String> googlePlace = list.get(i);
                double lat = Double.parseDouble(googlePlace.get("lat"));
                double lng = Double.parseDouble(googlePlace.get("lng"));
                String placeName = googlePlace.get("place_name");
                String vicinity = googlePlace.get("vicinity");
                LatLng latLng = new LatLng(lat, lng);
                markerOptions.position(latLng);
                markerOptions.title(placeName);
                //markerOptions.title(placeName + "," + vicinity);
                //markerOptions.snippet("Let's play at " + placeName + " : " + vicinity);
                Marker locationMarker = googleMap.addMarker(markerOptions);
                markerARRList.add(locationMarker);
                //@Felicia
                System.out.println("AsyncTask: markerARRList size: " + markerARRList.size() + ", " + locationMarker.getTitle());
                //markerHashMap.put(latLng.toString(), locationMarker);
            }
        }catch (Exception e) {
            Log.d("FFFF 22222 Exception", e.toString());
        }
    }

}
