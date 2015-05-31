package project.coen268.scu.dogplaydate;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * wenyi
 */
public class ViewDatesList extends Activity{
    private final List<DatesRecordEntity> currentDatesRecord = new ArrayList();
    //private final List<String> testList = new ArrayList();
    private static final String TABLENAME_PLAYDATELIST = "playDatesListsTable";
    //Todo: here should be set a parameter, how to get user ID? getExtra from Intent?
    private String userName = "Tracey";
    private String userID = "26";
    private ListView currentListsView;
    private ListView historyListsView;
    private Calendar compareDateBaseline;
    public SimpleDateFormat inputSimpleFormat;
    public SimpleDateFormat localSimpleFormat;

    //test
    String contacts[]={"Ajay","Sachin","Sumit","Tarun","Yogesh"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dates_list);
        compareDateBaseline = Calendar.getInstance();
        currentListsView = (ListView) findViewById(R.id.currentDatesListView);
        historyListsView = (ListView) findViewById(R.id.pastDatesListView);
        inputSimpleFormat = new SimpleDateFormat("MMM dd, yyyy, HH:mm");
        inputSimpleFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        localSimpleFormat= new SimpleDateFormat("MMM dd, yyyy, hh:mm a");
        localSimpleFormat.setTimeZone(TimeZone.getTimeZone("PST"));


        loadListSource();
        currentListsView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        registerForContextMenu(currentListsView);

        currentListsView.setFocusable(false);
//        currentListsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), "Test1 short click happened", Toast.LENGTH_SHORT).show();
//            }
//        });
//        currentListsView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), "Test 1long click happened", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });


//        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,contacts);
//        historyListsView.setAdapter(adapter);
//        registerForContextMenu(historyListsView);
//        historyListsView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Test2 short click happened", Toast.LENGTH_SHORT).show();
//            }
//        });

//        historyListsView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), "Test 2long click happened", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });

    }

    public void loadListSource() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLENAME_PLAYDATELIST);
        //todo: uncommetn this  line
        query.whereEqualTo("userID", userID);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> recordList, ParseException e) {
                if (e == null) {
                    for (ParseObject object : recordList) {
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
                        //todo: might want to comment out these two lines
                        //System.out.println(oneRecord.toString());
                        //System.out.println("list size " + currentDatesRecord.size());
                        currentDatesRecord.add(oneRecord);
                    }
                    currentListsView.setAdapter(new DatesRecordAdapter(getApplicationContext(), R.layout.row_playdateslist, currentDatesRecord));
                    //registerForContextMenu(currentListsView);
                } else {
                    Log.d("FF", "Error: " + e.getMessage());
                    return;
                }
            }
        });
    }
    public void onCreateContextMenu(ContextMenu menu,
                                    View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_munu_playdates_list, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.action_edit:
                Toast.makeText(this, "this is edit", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_delete:
                Toast.makeText(this, "this is delete", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


}
