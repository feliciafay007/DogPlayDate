package project.coen268.scu.dogplaydate;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

//public class PlacesDisplayTask extends AsyncTask<Object, Integer, List<HashMap<String, String>>> {
public class PlacesDisplayTask extends AsyncTask<Object, Integer, Void> {

    JSONObject googlePlacesJson;
    GoogleMap googleMap;
    //Felicia
    //HashMap<String, Marker> markerHashMap;
    ArrayList<Marker> markerARRList;
    List<HashMap<String, String>> googlePlacesList = null;
    TreeSet<String> markerKeySet;

    public PlacesDisplayTask (TreeSet<String> paraMarkerSet) {
        this.markerKeySet = paraMarkerSet;
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

                //markerARRList.add(locationMarker);
                //@Felicia
                //System.out.println("AsyncTask: markerARRList size: " + markerARRList.size() + ", " + locationMarker.getTitle());
                //markerHashMap.put(latLng.toString(), locationMarker);
                String currentKey = Double.toString(lat) + Double.toString(lng);
                if (markerKeySet.contains(currentKey)) {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    Marker locationMarker = googleMap.addMarker(markerOptions);
                } else {
                    Marker locationMarker = googleMap.addMarker(markerOptions);
                }
            }
        }catch (Exception e) {
            Log.d("FFFF 22222 Exception", e.toString());
        }
    }

}
