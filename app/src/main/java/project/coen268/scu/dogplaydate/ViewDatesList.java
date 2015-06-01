package project.coen268.scu.dogplaydate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
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

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * wenyi
 */
public class ViewDatesList extends Activity{
    private final List<DatesRecordEntity> currentDatesRecord = new ArrayList();
    private final List<DatesRecordEntity> historyDatesRecord = new ArrayList();
    //private final HashMap<Integer, String> currentMap = new HashMap<Integer, String>();
    private final SparseArray<DatesRecordEntity> currentSparseArray = new SparseArray<DatesRecordEntity>();
    private final SparseArray<DatesRecordEntity> historySparseArray = new SparseArray<DatesRecordEntity>();

    private static final String TABLENAME_PLAYDATELIST = "playDatesListsTable";
    //Todo: here should be set a parameter, how to get user ID? getExtra from Intent?
    private String userName = "Tracey";
    private String userID = "26";
    private ListView currentListsView;
    private ListView historyListsView;
    private Calendar compareDateBaseline;
//    public SimpleDateFormat inputSimpleFormat;
//    public SimpleDateFormat localSimpleFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dates_list);
        compareDateBaseline = Calendar.getInstance();
        currentListsView = (ListView) findViewById(R.id.currentDatesListView);
        historyListsView = (ListView) findViewById(R.id.pastDatesListView);
//        inputSimpleFormat = new SimpleDateFormat("MMM dd, yyyy, HH:mm");
//        inputSimpleFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//        localSimpleFormat= new SimpleDateFormat("MMM dd, yyyy, hh:mm a");
//        localSimpleFormat.setTimeZone(TimeZone.getTimeZone("PST"));

        loadListSource();
        currentListsView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        currentListsView.setFocusable(false);
        registerForContextMenu(currentListsView);

        historyListsView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        historyListsView.setFocusable(false);
        registerForContextMenu(historyListsView);
    }

    public void loadListSource() {
        currentDatesRecord.clear();
        historyDatesRecord.clear();
        currentSparseArray.clear();
        historySparseArray.clear();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLENAME_PLAYDATELIST);
        //todo: uncommetn this  line
        //query.whereEqualTo("userID", userID);
        //query.setLimit(30); // default is 100.
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> recordList, ParseException e) {
                if (e == null) {
                    int currentCounter = 0;
                    int historyCounter = 0;
                    for (ParseObject myObject : recordList) {
                        DatesRecordEntity oneRecord = new DatesRecordEntity(
                                myObject.getDate("startTime"),
                                myObject.getDate("endTime"),
                                myObject.getString("userName"),
                                myObject.getString("userID"),
                                myObject.getString("dogName"),
                                myObject.getString("dogID"),
                                myObject.getString("place"),
                                myObject.getObjectId(),
                                myObject.getBoolean("isValid"),
                                myObject.getInt("status")
                        );
                        //todo: might want to comment out these two lines
                        //System.out.println(oneRecord.toString());
                        //System.out.println("list size " + currentDatesRecord.size());
                        if (oneRecord.getEndTime().before(compareDateBaseline.getTime())) {
                            historyDatesRecord.add(oneRecord);
                            historySparseArray .put(historyCounter, oneRecord);
                            ++historyCounter;
                        } else {
                            currentDatesRecord.add(oneRecord);
                            currentSparseArray.put(currentCounter, oneRecord);
                            ++currentCounter;
                        }
                    }
                    currentListsView.setAdapter(new DatesRecordAdapter(getApplicationContext(), R.layout.row_playdateslist, currentDatesRecord));
                    historyListsView.setAdapter(new DatesRecordAdapter(getApplicationContext(), R.layout.row_playdateslist, historyDatesRecord));
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
                // todo: if an event is deleted after someone else has participated, he will get nofitied.
                deleteOneRecord(info.id, (ListView) info.targetView.getParent());
                Toast.makeText(this, "this is delete", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void deleteOneRecord(final long listViewItemId, final ListView listview) {
        final List<DatesRecordEntity> recordList;
        final SparseArray<DatesRecordEntity> sparseArray;
        if (listview == currentListsView) {
            recordList = currentDatesRecord;
            sparseArray = currentSparseArray;
        } else  {
            recordList = historyDatesRecord;
            sparseArray = historySparseArray;
        }
        AlertDialog.Builder deleteDialogBuilder = new AlertDialog.Builder(ViewDatesList.this);
        deleteDialogBuilder.
                setMessage("Do you really want to delete this?\n" + recordList.get((int) listViewItemId).toString()).
                setTitle("Note");
        deleteDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            //todo: currentSparseArray, hisotrySparseArray
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "this is to be deleted: \n" + recordList.get((int) listViewItemId).toString(), Toast.LENGTH_SHORT).show();
                ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLENAME_PLAYDATELIST);
                query.whereEqualTo("objectId", sparseArray.get((int) listViewItemId).getObjectID());
                //System.out.println("objectId" + currentSparseArray.get((int) listViewItemId).getObjectID());
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> objectList, ParseException e) {
                        if (e == null) {
                            for (ParseObject myObject : objectList) {
                                myObject.deleteInBackground(new DeleteCallback() {
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            loadListSource();
                                        } else {
                                            Log.d("FF", "Delete Error1: " + e.getMessage());
                                        }
                                    }
                                });
                            }
                        } else {
                            Log.d("FF", "Delete Error2: " + e.getMessage());
                            return;
                        }
                    }
                });
            }
        });
        deleteDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }

        );
        AlertDialog deleteDialog = deleteDialogBuilder.create();
        deleteDialog.show();
    }

}
