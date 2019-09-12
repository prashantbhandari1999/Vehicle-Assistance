package com.example.vehicleassistance;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.Menu;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private LinearLayout mRevealView;
    private boolean hidden = true;
    private ImageButton gallery_btn, photo_btn, video_btn, audio_btn, location_btn, contact_btn;

    private FloatingActionButton GPSButton;
    private boolean isMapAdded = true;

    private EditText searchEditText;
    private static final int DEFAULT_ZOOM = 15;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        BottomNavigationView BottomNavView = findViewById(R.id.bottom_nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        BottomNavView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        searchEditText = (EditText) findViewById(R.id.search_editext_map);


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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        onEnterButtonClicked();      //When Enter button is pressed in search

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
            Intent intent = new Intent(HomeScreenActivity.this, AddShop.class);
            startActivity(intent);
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
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };


    private void initView() {
        mRevealView = (LinearLayout) findViewById(R.id.reveal_items);
        mRevealView.setVisibility(View.GONE);

        gallery_btn = (ImageButton) findViewById(R.id.gallery_img_btn);
        photo_btn = (ImageButton) findViewById(R.id.photo_img_btn);
        video_btn = (ImageButton) findViewById(R.id.video_img_btn);
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
        switch (v.getId()) {

            case R.id.gallery_img_btn:
                break;
            case R.id.photo_img_btn:

                break;
            case R.id.video_img_btn:

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
}
