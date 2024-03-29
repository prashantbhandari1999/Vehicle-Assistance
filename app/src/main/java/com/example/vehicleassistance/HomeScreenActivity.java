package com.example.vehicleassistance;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.provider.MediaStore;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import java.util.HashMap;
import java.util.List;

import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;

import io.opencensus.stats.MeasureMap;

public class HomeScreenActivity extends AppCompatActivity
        implements FilterDialog.FilterDialogListener, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, GetNearbyPlacesData.AsyncResponse, GetClosestCare.AsyncResponse, mapFragment.OnFragmentInteractionListener, UpcomingNotificationFragment.OnFragmentInteractionListener, MyCustomDialog.onInputListner {

    private LinearLayout mRevealView;
    public String filter;
    private boolean hidden = true, initialised = false;
    private ImageButton fuel_stations_btn, service_centres_btn, showroom_btn, washing_centers_btn, location_btn, contact_btn;
    private static final String MyPREFERENCES = "MyPrefs";
    private static final String MyGooglePREFERENCES = "googlePrefs";

    private FloatingActionButton GPSButton;
    private boolean isMapAdded = true;
    public static final int PICK_IMAGE = 1;
    private static final int PERMISSION_REQUEST_CODE = 200;
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
    GetNearbyPlacesData getNearbyPlacesData;
    private Location Last_Known_Location;
    int PROXIMITY_RADIUS = 5000;
    SharedPreferences imagePreferences, preferences, googlePreferences;

    Boolean firstTimeLoad = true, flag = false;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    View headerView;
    private Fragment currentFragment;
    String placeId = "";
    GetClosestCare getClosestCare;
    notificationFragment mnotificationFragment;
    SettingsActivity settingsActivity;

    private static final int Permission_All = 1;

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

        mnotificationFragment = new notificationFragment();
        settingsActivity = new SettingsActivity();
        setSupportActionBar(toolbar);

        if (isMapAdded) {
            Fragment fragment = new mapFragment();          //Here Map is added
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_frame_container,
                    fragment).commit();
            isMapAdded = false;
            currentFragment = fragment;
            flag = true;
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
            mRevealView.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
                getClosestCare = new GetClosestCare();
                getClosestCare.delegate = HomeScreenActivity.this;
                String snippet = marker.getSnippet();
                Object data[] = new Object[1];
                data[0] = "https://maps.googleapis.com/maps/api/place/details/json?place_id=" + snippet + "&fields=name,rating,formatted_phone_number,vicinity,opening_hours,type&key=" + BuildConfig.google_maps_key;
                Log.d("url:", "onCreate: " + data[0]);
                getClosestCare.execute(data);
            }
        });

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        searchView =
                (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search Here");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchString) {
                Geocoder geocoder = new Geocoder(HomeScreenActivity.this);
                List<Address> list = new ArrayList<>();
                try {
                    list = geocoder.getFromLocationName(searchString, 1);

                } catch (IOException e) {
                }
                if (list.size() > 0) {
                    Address address = list.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    Location location = new Location("");
                    location.setLatitude(address.getLatitude());
                    location.setLongitude(address.getLongitude());

                    ((mapFragment) currentFragment).setLast_Known_Location(location);
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

//        if (id == R.id.nav_home) {
//            Intent intent = new Intent(HomeScreenActivity.this, Dialog.class);
//            startActivity(intent);
//
//        }
//
        if (id == R.id.nav_closest_care) {
            getLastKnownLocation();
            getNearbyPlacesData = new GetNearbyPlacesData();
            getNearbyPlacesData.asyncResponse = this;
            Object dataTransfer[] = new Object[2];
//        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
            getNearbyPlacesData.setUserLocation(Last_Known_Location);
            String url = "";
            mMap.clear();
            String serviceCentre = "";
            url = getURL(Last_Known_Location.getLatitude(), Last_Known_Location.getLongitude(), "car_repair", serviceCentre);
            dataTransfer[0] = mMap;
            dataTransfer[1] = url;

            getNearbyPlacesData.execute(dataTransfer);
            initialised = true;

        } else if (id == R.id.nav_spareparts) {
            Intent intent = new Intent(HomeScreenActivity.this, SparePartsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {
            Intent shareIntent =   new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Insert Subject here");
            String app_url = " https://play.google.com/store/apps/details?id=my.example.javatpoint";
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,app_url);
            startActivity(Intent.createChooser(shareIntent, "Share via"));

        } else if (id == R.id.nav_addshop) {
            Intent intent = new Intent(HomeScreenActivity.this, AddShopActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_addmotorcycle) {
            Intent intent = new Intent(HomeScreenActivity.this, EnlargeImageActivityForMotorcycles.class);
            startActivity(intent);
        } else if (id == R.id.nav_addReminder) {
            Intent intent = new Intent(HomeScreenActivity.this, AddReminderActivity.class);
            startActivity(intent);
            Animatoo.animateZoom(this);
        } else if (id == R.id.nav_bookappointment) {
            final AlertDialog d = new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_bookmark_black_24dp)
                    .setTitle("Book Appointment")
                    .setMessage(Html.fromHtml("<ul>" +
                            "<hr>" +
                            "<br>" +
                            "<li><a href=https://service.tatamotors.com/content/online-service-booking>Tata Services</a></li>" +
                            "<li><a href=https://www.bajajauto.com/service/owner-manual>Bajaj Services</a></li>" +
                            "<li><a href=https://bookonline.hyundai.co.in>Hyundai Services</a></li>" +
                            "<li><a href=https://www.tvsmotor.com/servicebooking/2016/service-booking-home>TVS Services</a></li>" +
                            "<li><a href=https://www.hondacarindia.com/honda-services/owners/bookaservice>Honda Services</a></li>" +
                            "<li><a href=https://myautoserviceappointments.com>My Auto Services</a></li>" +
                            "<li><a href=https://gomechanic.in/car-enquiry/pune?utm_source=google_Pune&utm_medium=cpc&gclid=EAIaIQobChMIyaWNgqKh5QIVgpKPCh0IPASYEAAYASAAEgKkY_D_BwE>Go Mechanic Services</a></li>" +
                            "</ul>"
                    ))
                    .create();
            d.show();


            ((TextView) d.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());


        }else if(id == R.id.nav_book_rides){
            final AlertDialog d = new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_local_taxi_black_24dp)
                    .setTitle("Book Rides Online")
                    .setMessage(Html.fromHtml("<ul>" +
                            "<hr>" +
                            "<br>" +
                            "<li><a href=https://www.olacabs.com>Ola Cabs</a></li>" +
                            "<li><a href=https://www.uber.com/in/en/ride/>Uber Cabs</a></li>" +
                            "<li><a href=https://www.redbus.in>RedBus</a></li>" +
                            "<li><a href=https://www.goibibo.com>Go Ibibo</a></li>" +
                            "<li><a href=https://www.makemytrip.com>Make My Trip</a></li>" +
                            "</ul>"
                    ))
                    .create();
            d.show();


            ((TextView) d.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());

        } else if (id == R.id.nav_log_out) {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.log_out_new)
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
                    // Animation();
                    if (firstTimeLoad) {
                        mRevealView.setVisibility(View.GONE);
                        GPSButton.show();
                        firstTimeLoad = false;
                    } else {
                        GPSButton.hide();
                        if (mRevealView.getVisibility() == View.VISIBLE) {
                            mRevealView.setVisibility(View.GONE);
                            GPSButton.show();
                        }
                        else
                            mRevealView.setVisibility(View.VISIBLE);
                    }
                    if (flag) {
                        flag = false;
                        GPSButton.hide();
                        mRevealView.setVisibility(View.VISIBLE);
                    }
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_right);
                    fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                    fragmentTransaction.replace(R.id.nav_frame_container, currentFragment).commit();
                    return true;
                case R.id.navigation_settings:
                    flag = false;
                    hideGPS();
                    hideRevealView();
                    firstTimeLoad = true;
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_right);
                    fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                    fragmentTransaction.replace(R.id.nav_frame_container, settingsActivity).commit();
                    return true;
                case R.id.navigation_notifications:
                    flag = false;
                    hideGPS();
                    hideRevealView();
                    firstTimeLoad = true;
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_right);
                    fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                    fragmentTransaction.replace(R.id.nav_frame_container, mnotificationFragment).commit();
                    return true;
            }
            return false;
        }
    };


    private void initView() {
        mRevealView = findViewById(R.id.reveal_items);
        mRevealView.setVisibility(View.GONE);

        fuel_stations_btn = findViewById(R.id.filter_fuel_stations_button);
        service_centres_btn = findViewById(R.id.filter_service_centres_button);
        showroom_btn = findViewById(R.id.filter_showrooms_button);
        washing_centers_btn = findViewById(R.id.filter_washing_centres_button);
        location_btn = findViewById(R.id.location_img_btn);
        contact_btn = findViewById(R.id.contact_img_btn);

        fuel_stations_btn.setOnClickListener(this);
        service_centres_btn.setOnClickListener(this);
        showroom_btn.setOnClickListener(this);
        washing_centers_btn.setOnClickListener(this);
        location_btn.setOnClickListener(this);
        contact_btn.setOnClickListener(this);
//        mMap.clear();
    }

    @Override
    public void onClick(View v) {
        hideRevealView();
        Object dataTransfer[] = new Object[2];
        GetNearbyPlacesData getNearbyPlacesData1 = new GetNearbyPlacesData();
        getNearbyPlacesData1.asyncResponse = this;
        getLastKnownLocation();
        getNearbyPlacesData1.setUserLocation(Last_Known_Location);
        String url = "";
        GPSButton.show();
        switch (v.getId()) {

            case R.id.filter_fuel_stations_button:
                mMap.clear();
                url = getURL(Last_Known_Location.getLatitude(), Last_Known_Location.getLongitude(), "gas_station", "");
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData1.execute(dataTransfer);
                Toast.makeText(this, "Showing nearby Fuel Stations", Toast.LENGTH_LONG).show();

                break;
            case R.id.filter_service_centres_button:
                FilterDialog filterDialog = new FilterDialog();
                filterDialog.show(getSupportFragmentManager(), "Filter Dialog");

                Log.d("filter", "onClick: " + filter);


                break;
            case R.id.filter_showrooms_button:
                mMap.clear();
                url = getURL(Last_Known_Location.getLatitude(), Last_Known_Location.getLongitude(), "car_dealer", "");
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData1.execute(dataTransfer);
                Toast.makeText(this, "Showing nearby Showrooms", Toast.LENGTH_LONG).show();
                break;
            case R.id.filter_washing_centres_button:
                mMap.clear();
                url = getURL(Last_Known_Location.getLatitude(), Last_Known_Location.getLongitude(), "car_wash", "");
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData1.execute(dataTransfer);
                Toast.makeText(this, "Showing nearby Washing Centres", Toast.LENGTH_LONG).show();
                break;
            case R.id.location_img_btn:
                mMap.clear();
                url = getURL(Last_Known_Location.getLatitude(), Last_Known_Location.getLongitude(), "car_repair", "Towing");
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData1.execute(dataTransfer);
                Toast.makeText(this, "Showing nearby Towing Centres", Toast.LENGTH_LONG).show();
                break;
            case R.id.contact_img_btn:
                mMap.clear();
                url = getURL(Last_Known_Location.getLatitude(), Last_Known_Location.getLongitude(), "car_repair", "Puncture");
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData1.execute(dataTransfer);
                Toast.makeText(this, "Showing nearby Puncture Shops", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void hideRevealView() {
        if (mRevealView.getVisibility() == View.VISIBLE) {
            mRevealView.setVisibility(View.GONE);
            hidden = true;
        }
    }

    private void hideGPS() {
//        GPSButton.setVisibility(View.GONE);
        GPSButton.hide();

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
            GPSButton.hide();
        } else {
            Animator anim = android.view.ViewAnimationUtils.createCircularReveal(mRevealView, cy, cx, radius, 0);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mRevealView.setVisibility(View.INVISIBLE);
                    hidden = true;
                    GPSButton.show();
                }
            });
            anim.start();
        }
    }

    public void getMapObject(GoogleMap googleMap) {
        //Get the object of google map from fragment
        mMap = googleMap;
    }

    public void getLastKnownLocation() {
        Last_Known_Location = ((mapFragment) currentFragment).getLast_Known_Location();

//        Last_Known_Location = location;
    }

    private String getURL(double latitude, double longitude, String nearbyplaces, String name) {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location=" + latitude + "," + longitude);
        googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type=" + nearbyplaces);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&name=" + name);
        googlePlaceUrl.append("&key=" + BuildConfig.google_maps_key);
        return googlePlaceUrl.toString();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show();
    }

    public void addUserData() {
//        TextView nav_user = (TextView) headerView.findViewById(R.id.user_name);
//        nav_user.setText("hello user");
        userImage = headerView.findViewById(R.id.user_image);
        userName = headerView.findViewById(R.id.user_name);
        userEmail = headerView.findViewById(R.id.user_email);

        Bundle bundle = getIntent().getExtras();
        String signInMethod = bundle.getString("signInMethod");
        if (signInMethod.equals("google")) {
            SharedPreferences sharedPreferences = getSharedPreferences(MyGooglePREFERENCES, Context.MODE_PRIVATE);
            userName.setText(sharedPreferences.getString("name", ""));
            userEmail.setText(sharedPreferences.getString("email", ""));
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("logOut", false);

            String personPhotoUrl = sharedPreferences.getString("photoUrl", "");
            Glide.with(getApplicationContext()).load(personPhotoUrl).into(userImage);

            editor.apply();
        } else if (signInMethod.equals("register")) {

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

        if (checkPermission()) {
            try {
                Intent getIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(getIntent, "Select a photo"), PICK_IMAGE);
            } catch (Exception e) {
            }
        } else {
            requestPermission();
        }
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                CropImage.activity(uri)
                        .setAllowFlipping(true)
                        .setAllowRotation(true)
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent getIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(Intent.createChooser(getIntent, "Select a photo"), PICK_IMAGE);

                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                        }
                    }
                }
                break;
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
        editor.apply();
    }

    @Override
    public void processFinish(String output) {
        if (initialised) {
            if (!output.isEmpty()) {
                placeId = output;
            }
            mMap.clear();
            initialised = false;
            Intent intent = new Intent(HomeScreenActivity.this, ClosestCareActivity.class);
            intent.putExtra("place_id", placeId);
            startActivity(intent);
        } else {
            if (output.isEmpty()) {
                Toast.makeText(HomeScreenActivity.this, "Network error! Please try again later", Toast.LENGTH_LONG);
            }
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void processFinish(HashMap<String, String> output) {
        //Of GetClosestCare
        Log.d("output", "processFinish: " + output);
        if (output != null) {
            Log.e("window", "Name: " + output.get("place_name") + "\nContact: " + output.get("contact") + "\nAddress: " + output.get("Address") + "\nRating: " + output.get("rating") + "\nOpening Hours:" + output.get("Opening hours"));
            LayoutInflater inflater = HomeScreenActivity.this.getLayoutInflater();
            View view = inflater.inflate(R.layout.decribe_location, null);
            MyCustomDialog dialog = new MyCustomDialog(output.get("place_name"), output.get("contact"), output.get("Opening hours"), output.get("Address"),output.get("rating"));
            dialog.show(getSupportFragmentManager(), "MyCustomDialog");
        } else {
            Toast.makeText(this, "Network Error! Please try again later!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void sendInput(String input) {

    }

    @Override
    public void applyfilter(String Filter) {
        if (!Filter.equals("General")) {
            filter = Filter;
        } else {
            filter = "";
        }
        mMap.clear();
        String url = getURL(Last_Known_Location.getLatitude(), Last_Known_Location.getLongitude(), "car_repair", filter);
        Object dataTransfer[] = new Object[2];
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;

        GetNearbyPlacesData getNearbyPlacesData1 = new GetNearbyPlacesData();
        getNearbyPlacesData1.asyncResponse = this;
        getNearbyPlacesData1.setUserLocation(Last_Known_Location);
        getNearbyPlacesData1.execute(dataTransfer);
        Toast.makeText(this, "Showing nearby service centres", Toast.LENGTH_LONG).show();
        Log.d("applyfilter", "onClick: " + filter);
    }

    public static void afterfilter() {

    }


}

