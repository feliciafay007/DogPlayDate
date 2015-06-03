package project.coen268.scu.dogplaydate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.ArrayList;
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

    private ParseImageView dogImage1;
    private ParseImageView dogImage2 ;
    private ParseImageView dogImage3;

    //Slide menu
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

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

        dogImage1 = (ParseImageView) findViewById(R.id.dogImg1);
        dogImage2 = (ParseImageView) findViewById(R.id.dogImg2);
        dogImage3 = (ParseImageView) findViewById(R.id.dogImg3);
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




        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("DogProfile");
        query1.whereEqualTo("userName", this.user);
        query1.whereEqualTo("dogNumber", 1);
        query1.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (scoreList.size()!=0) {
                    ParseFile image = scoreList.get(0).getParseFile("dogPic");
                    dogImage1.setParseFile(image);
                    dogImage1.loadInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            System.out.println("done fetching profile image1");
                        }
                    });
                } else {

                }
            }
        });

        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("DogProfile");
        query2.whereEqualTo("userName", this.user);
        query2.whereEqualTo("dogNumber", 2);
        query2.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (scoreList.size()!=0) {
                    ParseFile image = scoreList.get(0).getParseFile("dogPic");
                    dogImage2.setParseFile(image);
                    dogImage2.loadInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            System.out.println("done fetching profile image2");
                        }
                    });
                } else {

                }
            }
        });

        ParseQuery<ParseObject> query3 = ParseQuery.getQuery("DogProfile");
        query3.whereEqualTo("userName", this.user);
        query3.whereEqualTo("dogNumber", 3);
        query3.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (scoreList.size()!=0) {
                    ParseFile image = scoreList.get(0).getParseFile("dogPic");
                    dogImage3.setParseFile(image);
                    dogImage3.loadInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            System.out.println("done fetching profile image");
                        }
                    });
                } else {

                }
            }
        });


        dogImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addDog = new Intent(ProfileActivity.this, DogProfile.class);
                addDog.putExtra("userName", user);
                startActivity(addDog);
            }
        });

        dogImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addDog = new Intent(ProfileActivity.this, DogProfile2.class);
                addDog.putExtra("userName", user);
                startActivity(addDog);
            }
        });

        dogImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addDog = new Intent(ProfileActivity.this, DogProfile3.class);
                addDog.putExtra("userName", user);
                startActivity(addDog);
            }
        });


        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // My dogs
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Events
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Share to
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        // Logout
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));



        // Recycle the typed array
        navMenuIcons.recycle();

        // Recycle the typed array
//        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
//        ActionBar bar = getActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_dog, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }


        // @Wenyi
        // 1 get user ID
        // 2 get dog ID
        final String localUserName = name;
        final int localDogNumber = 1; // every person has dog, ranging from ID = 1 to , ID = 3
        btn_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addEvent = new Intent(ProfileActivity.this, CreatePlayDate.class);
                //validate if there is any dog, if no ,show dialog, elese, get dog Id;
                addEvent.putExtra("userID", localUserName);
                addEvent.putExtra("dogID", (int)2);
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

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new MyDogsFragment();
                break;
            case 2:
                fragment = new EventsFragment();
                break;
            case 3:
                fragment = new ShareToFragment();
                break;
            case 4:
                fragment = new LogoutFragment();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    //@ Wenyi
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle item selection
//        switch (item.getItemId()) {
////            case R.id.menu_forgot_password:
////                forgotPassword();
////                return true;
//            case R.id.menu_share:
//                share_your_app();
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    private void share_your_app () {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Here is a nice app that we can use together";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hi, Let's use DogPlayDate");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void onResume() {
        super.onResume();  // Always call the superclass method first

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("DogProfile");
        query1.whereEqualTo("userName", this.user);
        query1.whereEqualTo("dogNumber", 1);
        query1.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (scoreList.size()!=0) {
                    ParseFile image = scoreList.get(0).getParseFile("dogPic");
                    dogImage1.setParseFile(image);
                    dogImage1.loadInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            System.out.println("done fetching profile image");
                        }
                    });
                } else {

                }
            }
        });

        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("DogProfile");
        query2.whereEqualTo("userName", this.user);
        query2.whereEqualTo("dogNumber", 2);
        query2.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (scoreList.size()!=0) {
                    ParseFile image = scoreList.get(0).getParseFile("dogPic");
                    dogImage2.setParseFile(image);
                    dogImage2.loadInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            System.out.println("done fetching profile image");
                        }
                    });
                } else {

                }
            }
        });

        ParseQuery<ParseObject> query3 = ParseQuery.getQuery("DogProfile");
        query3.whereEqualTo("userName", this.user);
        query3.whereEqualTo("dogNumber", 3);
        query3.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (scoreList.size()!=0) {
                    ParseFile image = scoreList.get(0).getParseFile("dogPic");
                    dogImage3.setParseFile(image);
                    dogImage3.loadInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            System.out.println("done fetching profile image");
                        }
                    });
                } else {

                }
            }
        });


    }


}
