package project.coen268.scu.dogplaydate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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
    private String user;
    private int num;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// Get the view from listview_main.xml
        setContentView(R.layout.activity_dog_album2);

        Parse.initialize(this, "DgaXmRWHs3HaCC2buvdgC1ji2LPlItoxgCol7DcJ", "8a4PcTnqh14fJC5ekKmgxR7pDWgMTl27w2eKZEqK");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        Intent intent = getIntent();
        String myuser = intent.getStringExtra("userName");
        user = myuser;
        int mynum = intent.getIntExtra("dogNumber",0);
        num = mynum;


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
            worldpopulationlist = new ArrayList<WorldPopulation>();
            try {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                        "ImageUpload");

                query.whereEqualTo("user", user);
                query.whereEqualTo("dogNum", num);
                ob = query.find();
                for (ParseObject country : ob) {
                    // use dealsObject.get('columnName') to access the properties of the Deals object.
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
            listview = (ListView) findViewById(R.id.listview);
            adapter = new ListViewAdapter(DogAlbum2.this,
                    worldpopulationlist);
            listview.setAdapter(adapter);
            mProgressDialog.dismiss();
        }

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
//            case R.id.menu_forgot_password:
//                forgotPassword();
//                return true;
            case R.id.menu_share:
                share_your_app();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void share_your_app () {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Here is a nice app that we can use together";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hi, Let's use DogPlayDate");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
}
