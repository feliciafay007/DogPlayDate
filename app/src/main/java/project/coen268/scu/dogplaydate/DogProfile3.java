package project.coen268.scu.dogplaydate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.util.List;


public class DogProfile3 extends ActionBarActivity {

    private ParseImageView dogImage;
    private EditText nameText;
    private EditText ageText;
    private EditText breedText;
    private Button albumButton;
    private Button saveButton;
    private Button upLoadButton;
    private String user;

    private static final int CAMERA_REQUEST = 1888;

    private Bitmap photo;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_profile);
        Parse.initialize(this, "DgaXmRWHs3HaCC2buvdgC1ji2LPlItoxgCol7DcJ", "8a4PcTnqh14fJC5ekKmgxR7pDWgMTl27w2eKZEqK");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        //final ParseObject dogProfile = new ParseObject("DogProfile");

        dogImage = (ParseImageView)findViewById(R.id.imageView);
        nameText = (EditText)findViewById(R.id.editText);
        ageText = (EditText)findViewById(R.id.editText2);
        breedText = (EditText)findViewById(R.id.editText3);
        albumButton = (Button)findViewById(R.id.button);
        saveButton = (Button)findViewById(R.id.button2);
        upLoadButton = (Button)findViewById(R.id.button3);

        Intent intent = getIntent();
        String myuser = intent.getStringExtra("userName");
        user = myuser;


       /* ParseQuery<ParseObject> query = ParseQuery.getQuery("PlayDatesListsTable");

        long yourmilliseconds = System.currentTimeMillis()+86400000;
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date resultdate = new Date(yourmilliseconds);

        query.whereLessThan("startTime", resultdate);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    System.out.println(scoreList.size()+"!!!!!!!!!!!!!!!!!!!!!!");
                    //Log.d("score", "Retrieved " + scoreList.size() + " scores");
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });*/


       /* final int NOTIF_ID = 1234;
        NotificationManager notifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification note = new Notification(R.drawable.dogicon, "New Event", System.currentTimeMillis());

        PendingIntent intent = PendingIntent.getActivity(this, 0, new Intent(DogProfile.this, DogAlbum.class), 0);

        note.setLatestEventInfo(this, "Alert", "You have one upcoming event.", intent);

        notifManager.notify(NOTIF_ID, note);*/


        ParseQuery<ParseObject> query = ParseQuery.getQuery("DogProfile");
        query.whereEqualTo("userName", this.user);
        query.whereEqualTo("dogNumber", 3);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (scoreList.size()!=0) {
                    String dogName = scoreList.get(0).getString("dogName");
                    String dogAge = scoreList.get(0).getString("dogAge");
                    String dogBreed = scoreList.get(0).getString("dogBreed");
                    ParseFile image = scoreList.get(0).getParseFile("dogPic");

                    nameText.setText(dogName);
                    ageText.setText(dogAge);
                    breedText.setText(dogBreed);
                    dogImage.setParseFile(image);
                    dogImage.loadInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            System.out.println("done fetching profile image");
                        }
                    });

                } else {
                    saveButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            String name = nameText.getText().toString();
                            String age = ageText.getText().toString();
                            String breed = breedText.getText().toString();


                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] image = stream.toByteArray();
                            ParseFile file = new ParseFile("dogPic.jpg", image);
                            file.saveInBackground();

                            ParseObject dogProfile = new ParseObject("DogProfile");


                            dogProfile.put("userName", user);
                            dogProfile.put("dogNumber", 3);
                            dogProfile.put("dogName", name);
                            dogProfile.put("dogAge", age);
                            dogProfile.put("dogBreed", breed);
                            dogProfile.put("dogPic", file);

                            dogProfile.saveInBackground();

                            Toast.makeText(DogProfile3.this, "Profile Uploaded",
                                    Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }
        });



        dogImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);



            }
        });

        upLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent albumIntent = new Intent(DogProfile3.this, DogAlbum.class);
                albumIntent.putExtra("userName", user);
                albumIntent.putExtra("dogNumber", 3);
                startActivity(albumIntent);
            }
        });

        albumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent uploadIntent = new Intent(DogProfile3.this, DogAlbum2.class);
                uploadIntent.putExtra("userName", user);
                uploadIntent.putExtra("dogNumber", 3);
                startActivity(uploadIntent);
            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            dogImage.setImageBitmap(photo);
        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
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
