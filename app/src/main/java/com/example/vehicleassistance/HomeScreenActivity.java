package com.example.vehicleassistance;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.theartofdev.edmodo.cropper.CropImage;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.Menu;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private LinearLayout mRevealView;
    private boolean hidden = true;
    private ImageButton gallery_btn, photo_btn, video_btn, audio_btn, location_btn, contact_btn;
    private static final String MyPREFERENCES = "MyPrefs";

    private FloatingActionButton GPSButton;
    private boolean isMapAdded = true;
    public static final int PICK_IMAGE = 1;
    public static final int RESIZE_DIMEN = 360;
    Bitmap profilePic, resizedBitmap;
    ImageView userImage;
    TextView userName,userEmail;
    private AutoCompleteTextView searchEditText;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));


    private static final int DEFAULT_ZOOM = 15;
    private GoogleMap mMap;
    private Location Last_Known_Location;
    int PROXIMITY_RADIUS = 10000;
    double latitude, longitude;
    SharedPreferences imagePreferences,preferences;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        BottomNavigationView BottomNavView = findViewById(R.id.bottom_nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        BottomNavView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        searchEditText = (AutoCompleteTextView) findViewById(R.id.search_edit_text_map);


        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();


        setSupportActionBar(toolbar);

        if (isMapAdded) {
            Fragment fragment = new mapFragment();          //Here Map is added
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_frame_container,
                    fragment).commit();
            isMapAdded = false;
        }
        initView();         //Initialise all services attributes

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        headerView =  navigationView.getHeaderView(0);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        onEnterButtonClicked();      //When Enter button is pressed in search
        addUserData();               //Add user Name and image in navigation drawer
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_addshop) {
            Intent intent = new Intent(HomeScreenActivity.this, AddShopActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_addvehicle) {
            Intent intent = new Intent(HomeScreenActivity.this, AddVehicleActivity.class);
            startActivity(intent);
        }else if(id==R.id.nav_log_out){
            new AlertDialog.Builder(this)
                    .setIcon(null)
                    .setTitle("Log Out")
                    .setMessage("Are you sure you want to log out ?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //startActivity();
                            firebaseAuth.signOut();
                            Intent intent1=new Intent(HomeScreenActivity.this,MainActivity.class);
                            startActivity(intent1);
                            finish();

                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Animation();
                    return true;
                case R.id.navigation_settings:
                    hideRevealView();
                    return true;
                case R.id.navigation_notifications:
                    hideRevealView();
                    return true;
            }
            return false;
        }
    };


    private void initView() {
        mRevealView = (LinearLayout) findViewById(R.id.reveal_items);
        mRevealView.setVisibility(View.GONE);

        gallery_btn = (ImageButton) findViewById(R.id.filter_fuel_stations_button);
        photo_btn = (ImageButton) findViewById(R.id.filter_service_centres_button);
        video_btn = (ImageButton) findViewById(R.id.filter_showrooms_button);
        audio_btn = (ImageButton) findViewById(R.id.audio_img_btn);
        location_btn = (ImageButton) findViewById(R.id.location_img_btn);
        contact_btn = (ImageButton) findViewById(R.id.contact_img_btn);

        gallery_btn.setOnClickListener(this);
        photo_btn.setOnClickListener(this);
        video_btn.setOnClickListener(this);
        audio_btn.setOnClickListener(this);
        location_btn.setOnClickListener(this);
        contact_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        hideRevealView();
        Object dataTransfer[] = new Object[2];
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        String url = "";
        switch (v.getId()) {

            case R.id.filter_fuel_stations_button:
                mMap.clear();
                url = getURL(Last_Known_Location.getLatitude(), Last_Known_Location.getLongitude(), "gas_station");
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(this, "Showing nearby Fuel Stations", Toast.LENGTH_LONG).show();

                break;
            case R.id.filter_service_centres_button:
                mMap.clear();
                String serviceCentre = "car_repair";
                url = getURL(Last_Known_Location.getLatitude(), Last_Known_Location.getLongitude(), serviceCentre);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                break;
            case R.id.filter_showrooms_button:
                mMap.clear();
                url = getURL(Last_Known_Location.getLatitude(), Last_Known_Location.getLongitude(), "car_dealer");
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                break;
            case R.id.audio_img_btn:

                break;
            case R.id.location_img_btn:

                break;
            case R.id.contact_img_btn:

                break;
        }
    }

    private void hideRevealView() {
        if (mRevealView.getVisibility() == View.VISIBLE) {
            mRevealView.setVisibility(View.GONE);
            hidden = true;
        }
    }

    public void Animation() {
        int cx = (mRevealView.getLeft() + mRevealView.getRight());
        int cy = mRevealView.getTop();
        int radius = Math.max(mRevealView.getWidth(), mRevealView.getHeight());


        if (hidden) {
            Animator anim = android.view.ViewAnimationUtils.createCircularReveal(mRevealView, cy, cx, 0, radius);
            mRevealView.setVisibility(View.VISIBLE);
            anim.start();
            hidden = false;
        } else {
            Animator anim = android.view.ViewAnimationUtils.createCircularReveal(mRevealView, cy, cx, radius, 0);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mRevealView.setVisibility(View.INVISIBLE);
                    hidden = true;
                }
            });
            anim.start();
        }
    }

    private void onEnterButtonClicked() {

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
                LAT_LNG_BOUNDS, null);

        searchEditText.setAdapter(mPlaceAutocompleteAdapter);

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                    String searchString = searchEditText.getText().toString();
                    Geocoder geocoder = new Geocoder(HomeScreenActivity.this);
                    List<Address> list = new ArrayList<>();
                    try {
                        list = geocoder.getFromLocationName(searchString, 1);

                    } catch (IOException e) {
                    }
                    if (list.size() > 0) {
                        Address address = list.get(0);
                        Toast.makeText(HomeScreenActivity.this, "Address:-" + address, Toast.LENGTH_SHORT).show();
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title("Address"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(

                                new LatLng(address.getLatitude(),
                                        address.getLongitude()), DEFAULT_ZOOM));

                    }
                }
                return false;
            }
        });

    }

    public void getMapObject(GoogleMap googleMap) {
        //Get the object of google map from fragment
        mMap = googleMap;
    }

    public void getLastKnownLocation(Location location) {
        //Get the object of google map from fragment
        Last_Known_Location = location;
    }

    private String getURL(double latitude, double longitude, String nearbyplaces) {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location=" + latitude + "," + longitude);
        googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type=" + nearbyplaces);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key=" + BuildConfig.google_maps_key);

        Log.d("getURL", "getURL: " + googlePlaceUrl);

        return googlePlaceUrl.toString();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection falied", Toast.LENGTH_SHORT).show();
    }
    public void addUserData(){
        TextView nav_user = (TextView)headerView.findViewById(R.id.user_name);
//        nav_user.setText("hello user");
        userImage=headerView.findViewById(R.id.user_image);
        userName=headerView.findViewById(R.id.user_name);
        userEmail=headerView.findViewById(R.id.user_email);

        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userEmail.setText(sharedPreferences.getString("email",""));
        userName.setText(sharedPreferences.getString("firstName","")+" "+sharedPreferences.getString("lastName",""));

        SharedPreferences preferences = getSharedPreferences("myprefs",MODE_PRIVATE);
        String img_str=preferences.getString("userphoto", "");

        //Check whether image present locally or not
        if (!img_str.equals("")) {
            //decode string to image
            String base = img_str;
            byte[] imageAsBytes = Base64.decode(base.getBytes(), Base64.DEFAULT);
            userImage.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        }

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage(userImage);
            }
        });
    }


    public void pickImage(View view) {

        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                1);

        Intent getIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(getIntent, "Select a photo"), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            Toast.makeText(this, "Image picked", Toast.LENGTH_SHORT).show();
            if (data != null) {
                Uri uri = data.getData();

                CropImage.activity(uri)
                        .setAllowFlipping(false)
                        .setAllowRotation(false)
                        .setAspectRatio(1, 1)
                        .start(this);

            }

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri uri = result.getUri();
                try {
                    profilePic = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    ((ImageView)headerView.findViewById(R.id.user_image)).setImageBitmap(profilePic);
                        setProfilePhoto(userImage);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }
    public void setProfilePhoto(View view){
        //code image to string
        userImage.buildDrawingCache();
        Bitmap bitmap = userImage.getDrawingCache();
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte[] image=stream.toByteArray();
        //System.out.println("byte array:"+image);
        //final String img_str = "data:image/png;base64,"+ Base64.encodeToString(image, 0);
        //System.out.println("string:"+img_str);
        String img_str = Base64.encodeToString(image, 0);
        //decode string to image
        String base=img_str;
        byte[] imageAsBytes = Base64.decode(base.getBytes(), Base64.DEFAULT);

        //save in shared preferences
        preferences = getSharedPreferences("myprefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userphoto",img_str);
        editor.commit();
    }
}

