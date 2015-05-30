package project.coen268.scu.dogplaydate;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;


public class DogProfile extends ActionBarActivity {

    private ImageView dogImage;
    private EditText nameText;
    private EditText ageText;
    private EditText breedText;
    private Button albumButton;
    private Button saveButton;
    private Button upLoadButton;

    private static final int CAMERA_REQUEST = 1888;

    private Bitmap photo;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_profile);
        Parse.initialize(this, "DgaXmRWHs3HaCC2buvdgC1ji2LPlItoxgCol7DcJ", "8a4PcTnqh14fJC5ekKmgxR7pDWgMTl27w2eKZEqK");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        //final ParseObject dogProfile = new ParseObject("DogProfile");

        dogImage = (ImageView)findViewById(R.id.imageView);
        nameText = (EditText)findViewById(R.id.editText);
        ageText = (EditText)findViewById(R.id.editText2);
        breedText = (EditText)findViewById(R.id.editText3);
        albumButton = (Button)findViewById(R.id.button);
        saveButton = (Button)findViewById(R.id.button2);
        upLoadButton = (Button)findViewById(R.id.button3);



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
                dogProfile.put("dogName", name);
                dogProfile.put("dogAge", age);
                dogProfile.put("dogBreed", breed);
                dogProfile.put("dogPic", file);

                dogProfile.saveInBackground();

                Toast.makeText(DogProfile.this, "Profile Uploaded",
                        Toast.LENGTH_SHORT).show();

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
                Intent albumIntent = new Intent(DogProfile.this, DogAlbum.class);
                startActivity(albumIntent);
            }
        });

        albumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent uploadIntent = new Intent(DogProfile.this, DogAlbum2.class);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dog_profile, menu);

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
