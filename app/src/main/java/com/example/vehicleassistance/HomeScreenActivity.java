package com.example.vehicleassistance;

import android.accounts.Account;
import android.accounts.AccountManager;
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

import com.bumptech.glide.Glide;
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
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
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
    private ImageButton fuel_stations_btn, service_centres_btn, showroom_btn, washing_centers_btn, location_btn, contact_btn;
    private static final String MyPREFERENCES = "MyPrefs";
    private static final String MyGooglePREFERENCES = "googlePrefs";

    private FloatingActionButton GPSButton;
    private boolean isMapAdded = true;
    public static final int PICK_IMAGE = 1;
    public static final int RESIZE_DIMEN = 360;
    Bitmap profilePic, resizedBitmap;
    ImageView userImage;
    SearchView searchView;
    TextView userName, userEmail;
    private AutoCompleteTextView searchEditText;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));


    private static final int DEFAULT_ZOOM = 15;
    private GoogleMap mMap;
    private Location Last_Known_Location;
    int PROXIMITY_RADIUS = 5000;
    SharedPreferences imagePreferences, preferences, googlePreferences;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    View headerView;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        BottomNavigationView BottomNavView = findViewById(R.id.bottom_nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        BottomNavView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        GPSButton = findViewById(R.id.myLocationButton);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        setSupportActionBar(toolbar);

        if (isMapAdded) {
            Fragment fragment = new mapFragment();          //Here Map is added
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_frame_container,
                    fragment).commit();
            isMapAdded = false;
            currentFragment=fragment;
        }
        GPSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((mapFragment) currentFragment).getDeviceLocation();
            }
        });
        initView();         //Initialise all services attributes

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        addUserData();               //Add user Name and image in navigation drawer
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (mRevealView.getVisibility() == View.VISIBLE) {
            Animation();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        searchView =
                (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search Here");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchString) {
//                Toast.makeText(HomeScreenActivity.this, "Submited", Toast.LENGTH_SHORT).show();
//                String searchString = searchView.toString();
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
                            .title(searchString));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(

                            new LatLng(address.getLatitude(),
                                    address.getLongitude()), DEFAULT_ZOOM));

                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(HomeScreenActivity.this, Dialog.class);
            startActivity(intent);

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {
            Intent intent = new Intent(HomeScreenActivity.this,SparePartsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_addshop) {
            Intent intent = new Intent(HomeScreenActivity.this, AddShopActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_addvehicle) {
            Intent intent = new Intent(HomeScreenActivity.this, AddVehicleActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_log_out) {
            new AlertDialog.Builder(this)
                    .setIcon(null)
                    .setTitle("Log Out")
                    .setMessage("Are you sure you want to log out ?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //startActivity();
                            firebaseAuth = FirebaseAuth.getInstance();
                            firebaseUser = firebaseAuth.getCurrentUser();

                            firebaseAuth.signOut();
                            Bundle bundle = getIntent().getExtras();
                            String method = bundle.getString("signInMethod");
                            if (method.equals("register")) {
                                SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userId", "null");
                                editor.apply();

                                Intent intent1 = new Intent(HomeScreenActivity.this, MainActivity.class);
                                startActivity(intent1);
                                finish();
                            } else {
                                SharedPreferences sharedPreferences = getSharedPreferences(MyGooglePREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("logOut", true);
                                editor.apply();
                                Intent intent1 = new Intent(HomeScreenActivity.this, MainActivity.class);
                                startActivity(intent1);
                                finish();
                            }

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
//                    mapFragment mapFragment= (mapFragment) getSupportFragmentManager().findFragmentByTag("unique_tag");
//                   // Toast.makeText(HomeScreenActivity.this,""+mapFragment.isVisible(),Toast.LENGTH_SHORT).show();
//                    if(mapFragment==null){

//                         mapFragment mapFragment=new mapFragment();
//                        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
//                    transaction.replace(R.id.nav_frame_container,mapFragment).addToBackStack("unique_tag").commit();
//                    }

                    //                    FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
//                    transaction.replace(R.id.nav_frame_container,mapfragment).addToBackStack("tag").commit();
                    return true;
                case R.id.navigation_settings:
                    hideGPS();
                    hideRevealView();
                    return true;
                case R.id.navigation_notifications:
                    hideGPS();
                    hideRevealView();
                    notificationFragment fragment=new notificationFragment();
                    FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.nav_frame_container,fragment).addToBackStack("tag").commit();
                    return true;
            }
            return false;
        }
    };


    private void initView() {
        mRevealView = findViewById(R.id.reveal_items);
        mRevealView.setVisibility(View.GONE);

        fuel_stations_btn = (ImageButton) findViewById(R.id.filter_fuel_stations_button);
        service_centres_btn = (ImageButton) findViewById(R.id.filter_service_centres_button);
        showroom_btn = (ImageButton) findViewById(R.id.filter_showrooms_button);
        washing_centers_btn = (ImageButton) findViewById(R.id.filter_washing_centres_button);
        location_btn = (ImageButton) findViewById(R.id.location_img_btn);
        contact_btn = (ImageButton) findViewById(R.id.contact_img_btn);

        fuel_stations_btn.setOnClickListener(this);
        service_centres_btn.setOnClickListener(this);
        showroom_btn.setOnClickListener(this);
        washing_centers_btn.setOnClickListener(this);
        location_btn.setOnClickListener(this);
        contact_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        hideRevealView();
        Object dataTransfer[] = new Object[2];
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.setUserLocation(Last_Known_Location);
        String url = "";
        switch (v.getId()) {

            case R.id.filter_fuel_stations_button:
                mMap.clear();
                url = getURL(Last_Known_Location.getLatitude(), Last_Known_Location.getLongitude(), "gas_station", "");
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(this, "Showing nearby Fuel Stations", Toast.LENGTH_LONG).show();

                break;
            case R.id.filter_service_centres_button:
                mMap.clear();
                String serviceCentre = "";
                url = getURL(Last_Known_Location.getLatitude(), Last_Known_Location.getLongitude(), "car_repair",serviceCentre);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(this, "Showing nearby service centres", Toast.LENGTH_LONG).show();
                break;
            case R.id.filter_showrooms_button:
                mMap.clear();
                url = getURL(Last_Known_Location.getLatitude(), Last_Known_Location.getLongitude(), "car_dealer", "");
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(this, "Showing nearby Showrooms", Toast.LENGTH_LONG).show();
                break;
            case R.id.filter_washing_centres_button:
                mMap.clear();
                url = getURL(Last_Known_Location.getLatitude(), Last_Known_Location.getLongitude(), "car_wash", "");
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(this, "Showing nearby Washing Centres", Toast.LENGTH_LONG).show();
                break;
            case R.id.location_img_btn:
                mMap.clear();
                url = getURL(Last_Known_Location.getLatitude(), Last_Known_Location.getLongitude(), "car_repair", "Towing");
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                Log.d("url:", url);

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(this, "Showing nearby Towing Centres", Toast.LENGTH_LONG).show();
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
    private void hideGPS(){
//        GPSButton.setVisibility(View.GONE);

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

    public void getMapObject(GoogleMap googleMap) {
        //Get the object of google map from fragment
        mMap = googleMap;
    }

    public void getLastKnownLocation(Location location) {
        //Get the object of google map from fragment
        Last_Known_Location = location;
    }

    private String getURL(double latitude, double longitude, String nearbyplaces, String name) {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location=" + latitude + "," + longitude);
        googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type=" + nearbyplaces);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("name=" + name);
        googlePlaceUrl.append("&key=" + BuildConfig.google_maps_key);
        return googlePlaceUrl.toString();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show();
    }

    public void addUserData() {
        TextView nav_user = (TextView) headerView.findViewById(R.id.user_name);
//        nav_user.setText("hello user");
        userImage = headerView.findViewById(R.id.user_image);
        userName = headerView.findViewById(R.id.user_name);
        userEmail = headerView.findViewById(R.id.user_email);

        Bundle bundle = getIntent().getExtras();
        String signInMethod = bundle.getString("signInMethod");
        if (signInMethod.equals("google")) {
            Toast.makeText(this, "Sign In with google", Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences = getSharedPreferences(MyGooglePREFERENCES, Context.MODE_PRIVATE);
            userName.setText(sharedPreferences.getString("name", ""));
            userEmail.setText(sharedPreferences.getString("email", ""));
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("logOut", false);

            String personPhotoUrl = sharedPreferences.getString("photoUrl", "");
            Glide.with(getApplicationContext()).load(personPhotoUrl).into(userImage);

            editor.commit();
        } else if (signInMethod.equals("register")) {
            Toast.makeText(this, "Sign In with REGISTER", Toast.LENGTH_SHORT).show();

            SharedPreferences sharedPreferences;
            sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            userEmail.setText(sharedPreferences.getString("email", ""));
            userName.setText(sharedPreferences.getString("firstName", "") + " " + sharedPreferences.getString("lastName", ""));

            SharedPreferences preferences = getSharedPreferences("myprefs", MODE_PRIVATE);
            String img_str = preferences.getString("userphoto", "");

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
                    ((ImageView) headerView.findViewById(R.id.user_image)).setImageBitmap(profilePic);
                    setProfilePhoto(userImage);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    public void setProfilePhoto(View view) {
        //code image to string
        userImage.buildDrawingCache();
        Bitmap bitmap = userImage.getDrawingCache();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte[] image = stream.toByteArray();
        //System.out.println("byte array:"+image);
        //final String img_str = "data:image/png;base64,"+ Base64.encodeToString(image, 0);
        //System.out.println("string:"+img_str);
        String img_str = Base64.encodeToString(image, 0);
        //decode string to image
        String base = img_str;
        byte[] imageAsBytes = Base64.decode(base.getBytes(), Base64.DEFAULT);

        //save in shared preferences
        preferences = getSharedPreferences("myprefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userphoto", img_str);
        editor.commit();
    }
}

