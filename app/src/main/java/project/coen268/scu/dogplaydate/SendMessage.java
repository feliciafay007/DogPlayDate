package project.coen268.scu.dogplaydate;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Random;
// @Wenyi: this is a demo ,showing how to send messages to parse and receive from it.

public class SendMessage extends ActionBarActivity {
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        textView = (TextView) findViewById(R.id.textView);
        Button buttonSendToParse = (Button)  findViewById(R.id.buttonSendToParse);
        buttonSendToParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject gameScore = new ParseObject("GameScore");
                Random rand = new Random();
                gameScore.put("score", 1337);
                gameScore.put("playerName", "Sean Plott");
                gameScore.put("cheatMode", false);
                gameScore.put("randNum", rand.nextInt(10));
                gameScore.saveInBackground();
                Toast.makeText(getApplicationContext(), "is it send to parse?", Toast.LENGTH_SHORT).show();
            }
        });

        Button buttonReceiveFromParse = (Button) findViewById(R.id.buttonReceivedFromParse);
        buttonReceiveFromParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseQuery<ParseObject> query = ParseQuery.getQuery("GameScore");
                query.getInBackground("BNXVGsZEmH", new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            // object will be your game score
                            int score = object.getInt("score");
                            String playerName = object.getString("playerName");
                            boolean cheatMode = object.getBoolean("cheatMode");
                            int randNumber = object.getInt("randNum");
                            //Log.i("FF", textView.getText().toString());
                            textView.setText(textView.getText().toString() + "\n" + score + ", " + playerName + ", " + randNumber);
                        } else {
                            // something went wrong
                            return;
                        }
                    }
                });
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                break;
            case R.id.send_message:
                break;
            case R.id.create_play_date:
                Intent intent = new Intent(SendMessage.this, CreatePlayDate.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
