package project.coen268.scu.dogplaydate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by D on 2015/5/25.
 */
public class DogAlbum extends Activity {

    private ImageView albumImage;
    private EditText dateText;
    private EditText positionText;
    private EditText memoText;
    private static final int CAMERA_REQUEST = 1888;
    private String user;
    private int num;
    private SimpleDateFormat format;
    private Calendar newCalendar;
    Button button;

    private Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_album);

        Parse.initialize(this, "DgaXmRWHs3HaCC2buvdgC1ji2LPlItoxgCol7DcJ", "8a4PcTnqh14fJC5ekKmgxR7pDWgMTl27w2eKZEqK");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        button = (Button) findViewById(R.id.uploadbtn);
        albumImage = (ImageView)findViewById(R.id.imageView2);
        dateText = (EditText)findViewById(R.id.editText4);
        positionText = (EditText)findViewById(R.id.editText5);
        memoText = (EditText)findViewById(R.id.editText6);

        Intent intent = getIntent();
        String myuser = intent.getStringExtra("userName");
        user = myuser;
        int mynum = intent.getIntExtra("dogNumber",0);
        num = mynum;

        //
        newCalendar = Calendar.getInstance(); // use today's date as default
        format = new SimpleDateFormat("MMM dd, yyyy");
        dateText.setText(format.format(newCalendar.getTime()));
        //

        albumImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                String date = dateText.getText().toString();
                String position = positionText.getText().toString();
                String memo = memoText.getText().toString();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] image = stream.toByteArray();
                ParseFile file = new ParseFile("AlbumPhoto.jpg", image);
                file.saveInBackground();

                ParseObject c = new ParseObject("ImageUpload");
                c.put("user", user);
                c.put("dogNum", num);
                c.put("date", date);
                c.put("position", position);
                c.put("memo", memo);
                c.put("photo", file);
                c.saveInBackground();

                Toast.makeText(DogAlbum.this, "Image Uploaded",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            albumImage.setImageBitmap(photo);
        }
    }

    @Override
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


