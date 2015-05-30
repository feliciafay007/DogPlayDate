package project.coen268.scu.dogplaydate;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by D on 2015/5/26.
 */
public class DogAlbum2 extends Activity {
    ListView listview;
    List<ParseObject> ob;
    ProgressDialog mProgressDialog;
    ListViewAdapter adapter;
    private List<WorldPopulation> worldpopulationlist = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// Get the view from listview_main.xml
        setContentView(R.layout.activity_dog_album2);

        Parse.initialize(this, "DgaXmRWHs3HaCC2buvdgC1ji2LPlItoxgCol7DcJ", "8a4PcTnqh14fJC5ekKmgxR7pDWgMTl27w2eKZEqK");
        ParseInstallation.getCurrentInstallation().saveInBackground();


// Execute RemoteDataTask AsyncTask
        new RemoteDataTask().execute();
    }

    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
// Create a progressdialog
            mProgressDialog = new ProgressDialog(DogAlbum2.this);
// Set progressdialog title
            mProgressDialog.setTitle("Download Pictures");
// Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
// Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
// Create the array
            worldpopulationlist = new ArrayList<WorldPopulation>();
            try {
// Locate the class table named "Country" in Parse.com
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                        "ImageUpload");
// Locate the column named "ranknum" in Parse.com and order list
// by ascending
                //query.orderByAscending("ranknum");
                ob = query.find();
                for (ParseObject country : ob) {
// Locate images in flag column
                    ParseFile image = (ParseFile) country.get("photo");

                    WorldPopulation map = new WorldPopulation();
                    map.setRank((String) country.get("date"));
                    map.setCountry((String) country.get("position"));
                    map.setPopulation((String) country.get("memo"));
                    map.setFlag(image.getUrl());
                    worldpopulationlist.add(map);
                }
            } catch (com.parse.ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
// Locate the listview in listview_main.xml
            listview = (ListView) findViewById(R.id.listview);
// Pass the results into ListViewAdapter.java
            adapter = new ListViewAdapter(DogAlbum2.this,
                    worldpopulationlist);
// Binds the Adapter to the ListView
            listview.setAdapter(adapter);
// Close the progressdialog
            mProgressDialog.dismiss();
        }
    }
}
