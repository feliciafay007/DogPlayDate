package project.coen268.scu.dogplaydate;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;


public class FriendsList extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        final ListView lv = (ListView) findViewById(R.id.listView);
        final ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            final ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1);

            lv.setAdapter(listAdapter);

//            ParseRelation relation = currentUser.getRelation("Friends");

            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo(ParseConstants.USER_COLUMN, currentUser.getUsername());

            query.findInBackground(new FindCallback<ParseUser>() {

                @Override
                public void done(List<ParseUser> list, ParseException e) {
                    if (e == null) {
                        for (int i = 0; i < list.size(); i++) {
                            ParseObject friends = list.get(i);
//                            System.out.println("friends: " + friends.get(ParseConstants.FRIEND_COLUMN));
                            ArrayList<String> friendList = (ArrayList<String>) friends.get(ParseConstants.FRIEND_COLUMN);
                            if (friendList != null && !friendList.isEmpty()) {
                                TreeSet<String> set = new TreeSet<String>(friendList);
                                for (String friend : set) {
                                    listAdapter.add(friend);
                                }
                            }
                        }
                    }
                }

            });
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friends_list, menu);
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
}
