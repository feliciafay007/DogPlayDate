package project.coen268.scu.dogplaydate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by D on 2015/5/25.
 */
public class DogAlbum extends Activity {

    private ImageView albumImage;
    private EditText dateText;
    private EditText positionText;
    private EditText memoText;
    private static final int CAMERA_REQUEST = 1888;

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


        albumImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
               /* Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                        R.drawable.beyonce);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] image = stream.toByteArray();
                ParseFile file = new ParseFile("androidbegin.jpg", image);
                file.saveInBackground();*/

                String date = dateText.getText().toString();
                String position = positionText.getText().toString();
                String memo = memoText.getText().toString();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] image = stream.toByteArray();
                ParseFile file = new ParseFile("AlbumPhoto.jpg", image);
                file.saveInBackground();

                ParseObject c = new ParseObject("ImageUpload");
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
        getMenuInflater().inflate(R.menu.menu_dog_profile, menu);
        return true;
    }

}


