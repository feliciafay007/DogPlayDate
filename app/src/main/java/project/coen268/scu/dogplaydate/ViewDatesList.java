package project.coen268.scu.dogplaydate;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * wenyi
 */
public class ViewDatesList extends ActionBarActivity{
    private final List<DatesRecordEntity> currentDatesRecord = new ArrayList();
    //private final List<String> testList = new ArrayList();
    private static final String TABLENAME_PLAYDATELIST = "playDatesListsTable";
    //Todo: here should be set a parameter, how to get user ID? getExtra from Intent?
    private String userName = "Tracey";
    private String userID = "28";
    ListView currentListsView;
    ListView historyListsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dates_list);
        currentListsView = (ListView) findViewById(R.id.currentDatesListView);
        historyListsView = (ListView) findViewById(R.id.pastDatesListView);
        loadListSource();
        currentListsView.setAdapter(new DatesRecordAdapter(this, R.layout.row_playdateslist, currentDatesRecord));
        //currentListsView.setAdapter(new DatesRecordAdapter(this, R.layout.row_historical_playdateslist, currentDatesRecord));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_dates_list, menu);
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

    public void loadListSource() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLENAME_PLAYDATELIST);
        query.whereEqualTo("userID", userID);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> recordList, ParseException e) {
                if (e == null) {
                    //Log.d("PLAYDATE", "Retrieved " + recordList.size() + " playdates records");
                    for (ParseObject object : recordList) {
                        //                        DatesRecordEntity oneRecord = new DatesRecordEntity(
                        DatesRecordEntity oneRecord = new DatesRecordEntity(
                                object.getDate("startTime"),
                                object.getDate("endTime"),
                                object.getString("userName"),
                                object.getString("userID"),
                                object.getString("dogName"),
                                object.getString("dogID"),
                                object.getString("place"),
                                object.getBoolean("isValid"),
                                object.getInt("status")
                        );
                        System.out.println(oneRecord.toString());
                        //System.out.println("list size " + currentDatesRecord.size());
                        currentDatesRecord.add(oneRecord);
                    }
                    currentListsView.setAdapter(new DatesRecordAdapter(getApplicationContext(), R.layout.row_playdateslist, currentDatesRecord));
                } else {
                    Log.d("FF", "Error: " + e.getMessage());
                    return;
                }
            }
        });
    }
}
