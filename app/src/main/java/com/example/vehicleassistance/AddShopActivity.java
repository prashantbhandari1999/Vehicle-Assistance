package com.example.vehicleassistance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.inputmethodservice.Keyboard;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class AddShopActivity extends AppCompatActivity implements OnMapReadyCallback {


    private BottomSheetBehavior mBottomSheetBehavior;
    private GoogleMap mmMap;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private Location Last_Known_Location;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted = false;

    //    For_Camera
    private static final int CAMERA_PIC_REQUEST = 22;
    Uri cameraUri;
    private ImageView ImgPhoto;
    private String Camerapath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shop);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
//        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

        init();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_1);
        mapFragment.getMapAsync(this);


        mGeoDataClient = Places.getGeoDataClient(this, null);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //    For_Camera
        ImgPhoto = (ImageView) findViewById(R.id.ImageView_addshop);
        ImgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                } catch (Exception e) {
                    Toast.makeText(AddShopActivity.this, "Not able to Upload", Toast.LENGTH_SHORT).show();
                    Toast.makeText(AddShopActivity.this, "Please Try Later", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void init() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mmMap = googleMap;
        if (mmMap.isMyLocationEnabled() == false) {
            getLocationPermission();
            getDeviceLocation();
        }
        mmMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getDeviceLocation();
                    }
                }, 5000);
            }
        });
    }

    private void getDeviceLocation() {

        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            Last_Known_Location = task.getResult();
                            mmMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Last_Known_Location.getLatitude(), Last_Known_Location.getLongitude()))
                                    .title("Your Location"));
                            mmMap.moveCamera(CameraUpdateFactory.newLatLngZoom(

                                    new LatLng(Last_Known_Location.getLatitude(),
                                            Last_Known_Location.getLongitude()), DEFAULT_ZOOM));
                            mmMap.setMyLocationEnabled(true);
                        } else {
                            mmMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mmMap.getUiSettings().setMyLocationButtonEnabled(true);
                        }
                    }
                });
            } else {
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        updateLocationUI();
    }

    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mmMap == null) {
            Toast.makeText(this, "Map is null in updateUI", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mmMap.setMyLocationEnabled(true);
                mmMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mmMap.setMyLocationEnabled(false);
                mmMap.getUiSettings().setMyLocationButtonEnabled(false);
                Last_Known_Location = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public boolean onOptionsItemSelected(MenuItem menu) {
        int id = menu.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }


    //For_Camera
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        try {
            switch (requestCode) {
                case CAMERA_PIC_REQUEST:
                    if (resultCode == RESULT_OK) {
                        try {
                            Bitmap photo = (Bitmap) data.getExtras().get("data");

                            ImgPhoto.setImageBitmap(photo);

                        } catch (Exception e) {
                            Toast.makeText(AddShopActivity.this, "Not able to Upload", Toast.LENGTH_SHORT).show();
                            Toast.makeText(AddShopActivity.this, "Please Try Later", Toast.LENGTH_SHORT).show();                        }
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
        }
    }
}

