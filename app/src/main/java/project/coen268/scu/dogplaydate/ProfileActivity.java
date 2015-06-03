package project.coen268.scu.dogplaydate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import com.parse.SaveCallback;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Lu Yu
 */
public class ProfileActivity extends ActionBarActivity {
    //    private ImageView profileImage;
    private ParseImageView profileImage;
    private Button btn_upload;
    private Button btn_route;
    private Button btn_event;
    private Button btn_friend;

    private static int RESULT_LOAD_CAMERA_IMAGE = 2;
    private static int RESULT_LOAD_GALLERY_IMAGE = 1;
    private String mCurrentPhotoPath;
    private File cameraImageFile;
    private String user;

    private static final String IMAGE_TABLE = "Image";
    private static final String IMAGE_COLUMN = "Image";
    private static final String IMAGE_FILE_COLUMN = "ImageFile";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Parse.initialize(this, "DgaXmRWHs3HaCC2buvdgC1ji2LPlItoxgCol7DcJ", "8a4PcTnqh14fJC5ekKmgxR7pDWgMTl27w2eKZEqK");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        this.user = name;

        //load profile image from Parse
        profileImage = (ParseImageView) findViewById(R.id.profileImg);

        ParseQuery<ParseObject> query = ParseQuery.getQuery(IMAGE_TABLE);
        query.whereEqualTo(IMAGE_COLUMN, this.user);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    if (!scoreList.isEmpty()) {
                        ParseObject imageRow = scoreList.get(scoreList.size() - 1);
                        ParseFile imageFile = imageRow.getParseFile(IMAGE_FILE_COLUMN);
                        profileImage.setParseFile(imageFile);
                        profileImage.loadInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                System.out.println("done fetching profile image");
                            }
                        });
                    }
                } else {

                }
            }
        });

        ParseImageView dogImage1 = (ParseImageView) findViewById(R.id.dogImg1);
        ParseImageView dogImage2 = (ParseImageView) findViewById(R.id.dogImg2);
        ParseImageView dogImage3 = (ParseImageView) findViewById(R.id.dogImg3);
        TextView profileName = (TextView) findViewById(R.id.profileName);
        btn_upload = (Button) findViewById(R.id.uploadProfileImg);
        profileImage.setOnClickListener(chooseImageListener);
        btn_upload.setOnClickListener(uploadListener);
        btn_route = (Button) findViewById(R.id.btn_route);
        btn_event = (Button) findViewById(R.id.btn_event);
        btn_friend = (Button) findViewById(R.id.btn_friendsList);

        profileName.setText(name);


//        dogImage1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent addDog = new Intent(ProfileActivity.this, DogProfile.class);
//                startActivity(addDog);
//            }
//        });

        btn_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addFriend = new Intent(ProfileActivity.this, FindFreinds.class);
                startActivity(addFriend);
            }
        });

        btn_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addRoute = new Intent(ProfileActivity.this, Route.class);
                startActivity(addRoute);
            }
        });

//        btn_event.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent addEvent = new Intent(ProfileActivity.this, Event.class);
//                startActivity(addEvent);
//            }
//        });



        // @Wenyi
        // 1 get user ID
        // 2 get dog ID
        final String localName = name;
        btn_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addEvent = new Intent(ProfileActivity.this, CreatePlayDate.class);
                addEvent.putExtra("userID", localName);
                //validate if there is any dog, if no ,show dialog, elese, get dog Id;
                //addEvent.putExtra("dogID", );
                startActivity(addEvent);
            }
        });
    }

    View.OnClickListener chooseImageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialogChooseFrom();
        }
    };

    View.OnClickListener uploadListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            byte[] image = null;

            try {
                image = readInFile(mCurrentPhotoPath);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Create the ParseFile
            final ParseFile file = new ParseFile("profile", image);
            // Upload the image into Parse Cloud
            file.saveInBackground();
            // Create a New Class called "ImageUpload" in Parse
            final ParseObject imgupload = new ParseObject(IMAGE_TABLE);
            // Create a column named "ImageName" and set the string
            imgupload.put(IMAGE_COLUMN, user);
            // Create a column named "ImageFile" and insert the image
            imgupload.put(IMAGE_FILE_COLUMN, file);
            // Create the class and the columns
            imgupload.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    Toast.makeText(getBaseContext(), "Done!", Toast.LENGTH_LONG).show();
                    profileImage.setParseFile(file);
                    profileImage.loadInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            System.out.println("done fetching profile image");
                        }
                    });
                }
            });
        }
    };

    private void dialogChooseFrom() {

        final CharSequence[] items = {"From Gallery", "From Camera"};

        AlertDialog.Builder chooseDialog = new AlertDialog.Builder(this);
        chooseDialog.setTitle("Pick your choice").setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (items[which].equals("From Gallery")) {

                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, RESULT_LOAD_GALLERY_IMAGE);

                } else {

                    try {

                        File photoFile = createImageFile();
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(cameraIntent, RESULT_LOAD_CAMERA_IMAGE);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        chooseDialog.show();
    }

    private byte[] readInFile(String path) throws IOException {

        byte[] data = null;
        File file = new File(path);
        InputStream input_stream = new BufferedInputStream(new FileInputStream(file));
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        data = new byte[16384]; // 16K
        int bytes_read;

        while ((bytes_read = input_stream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, bytes_read);
        }

        input_stream.close();
        return buffer.toByteArray();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == RESULT_LOAD_GALLERY_IMAGE && null != data) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mCurrentPhotoPath = cursor.getString(columnIndex);
                cursor.close();

            } else if (requestCode == RESULT_LOAD_CAMERA_IMAGE) {
                mCurrentPhotoPath = cameraImageFile.getAbsolutePath();
            }

            File image = new File(mCurrentPhotoPath);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
            profileImage.setImageBitmap(bitmap);
        }
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + "_";

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File folder = new File(storageDir.getAbsolutePath() + "/PlayIOFolder");

        if (!folder.exists()) {
            folder.mkdir();
        }

        cameraImageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                folder      /* directory */
        );

        return cameraImageFile;
    }


    //@ Wenyi
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
