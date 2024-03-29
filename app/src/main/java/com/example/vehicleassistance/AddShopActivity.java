package com.example.vehicleassistance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddShopActivity extends AppCompatActivity implements OnMapReadyCallback {


    Button submitButton;
    EditText shopNameEditText, ownerNameEditText, phoneNumberEditText,addressEditText;
    TextView locationTextView;
    CheckBox acServicesCheckBox, cleaningCheckBox, towingCheckBox, airFillingCheckBox, doorstepCheckBox, paintingCheckBox, wheelcareCheckBox;
    ProgressDialog mProgressDialog;
    String shopName, ownerName, mobileNumber;

    BottomSheetBehavior mBottomSheetBehavior;
    private GoogleMap mmMap;
    GeoDataClient mGeoDataClient;
    PlaceDetectionClient mPlaceDetectionClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int PERMISSION_REQUEST_CODE = 200;

    private Location Last_Known_Location;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted = false;

    //    For_Camera
    private static final int CAMERA_PIC_REQUEST = 22;
    Uri cameraUri;
    Upload upload;
    String uploadId;
    private String imageURL;
    private ImageView ImgPhoto;
    private String Camerapath;
    private StorageReference mStorageReference;
    DatabaseReference mDatabaseReference;
    private FirebaseFirestore db;
    private StorageTask mUploadTask;
    private EditText mEditTextFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shop);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
//        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

        db = FirebaseFirestore.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference("shops");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("shops");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_1);
        mapFragment.getMapAsync(this);
        init();


        mGeoDataClient = Places.getGeoDataClient(this, null);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //    For_Camera
        ImgPhoto = findViewById(R.id.ImageView_addshop);
        ImgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    try {
                        Intent getIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(Intent.createChooser(getIntent, "Select a photo"), CAMERA_PIC_REQUEST);
                    } catch (Exception e) {
                        Toast.makeText(AddShopActivity.this, "Not able to Upload", Toast.LENGTH_SHORT).show();
                        Toast.makeText(AddShopActivity.this, "Please Try Later", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    requestPermission();
                }

            }
        });

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
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent getIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(Intent.createChooser(getIntent, "Select a photo"), CAMERA_PIC_REQUEST);

                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
            updateLocationUI();
            break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(AddShopActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public void init() {
        submitButton = findViewById(R.id.button_submit);
        shopNameEditText = findViewById(R.id.editText_enter_shop_name);
        ownerNameEditText = findViewById(R.id.editText_enter_name_of_person);
        phoneNumberEditText = findViewById(R.id.editText_enter_mobileno);
        addressEditText=findViewById(R.id.address_editText);
        locationTextView = findViewById(R.id.editText_enter_location);
        locationTextView.setEnabled(false);
        addressEditText.setEnabled(false);

        acServicesCheckBox = findViewById(R.id.CheckBox_ac_services);
        cleaningCheckBox = findViewById(R.id.CheckBox_cleaning);
        towingCheckBox = findViewById(R.id.CheckBox_towing);
        airFillingCheckBox = findViewById(R.id.CheckBox_air_filling);
        doorstepCheckBox = findViewById(R.id.CheckBox_doorstep_service);
        paintingCheckBox = findViewById(R.id.CheckBox_painting);
        wheelcareCheckBox = findViewById(R.id.CheckBox_Wwheel_care);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkIfEmpty()) {
                    if (mUploadTask != null && mUploadTask.isInProgress()) {
                        Toast.makeText(AddShopActivity.this, "Uploading Progress", Toast.LENGTH_SHORT).show();
                    } else {
                        UploadData();
                    }
                }
            }
        });
    }

    private boolean checkIfEmpty() {
        shopName = shopNameEditText.getText().toString();
        ownerName = ownerNameEditText.getText().toString();
        mobileNumber = phoneNumberEditText.getText().toString();
        if (shopName.isEmpty()) {
            Toast.makeText(this, "Enter Shop Name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (ownerName.isEmpty()) {
            Toast.makeText(this, "Enter Owner Name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mobileNumber.isEmpty()) {
            Toast.makeText(this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mobileNumber.length() < 10) {
            Toast.makeText(this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (wheelcareCheckBox.isChecked() || acServicesCheckBox.isChecked() || cleaningCheckBox.isChecked() || towingCheckBox.isChecked() || airFillingCheckBox.isChecked() || doorstepCheckBox.isChecked() || paintingCheckBox.isChecked() || paintingCheckBox.isChecked()) {
                return true;
            } else{
                Toast.makeText(this, "Select at least one of the services", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void UploadData() {
        if (cameraUri != null) {
            mProgressDialog=new ProgressDialog(AddShopActivity.this);
            mProgressDialog.setMessage("Uploading ...");
            mProgressDialog.show();
            StorageReference fileReference = mStorageReference.child(shopNameEditText.getText().toString() + "." + getFileExtension(cameraUri));
            mUploadTask = fileReference.putFile(cameraUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                }
                            }, 500);

                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageURL = uri.toString();
                                    uploadShopData();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mProgressDialog.hide();
                            Toast.makeText(AddShopActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    });
        } else {
            Toast.makeText(this, "Please Choose Photo", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadShopData() {
        Map<String, Object> shop = new HashMap<>();
        List<String> list=new ArrayList<>();
        shop.put("Shop Name", shopNameEditText.getText().toString());
        shop.put("ImageURL", imageURL);
        shop.put("Owner Name:", ownerName);
        shop.put("Phone Number", mobileNumber);
        shop.put("Location", locationTextView.getText().toString());
        if (acServicesCheckBox.isChecked())
            list.add("AC Services");
        if (wheelcareCheckBox.isChecked())
            list.add("Wheel Care");
        if (cleaningCheckBox.isChecked())
            list.add("Cleaning");
        if (towingCheckBox.isChecked())
            list.add("Towing");
        if (airFillingCheckBox.isChecked())
            list.add("Air Filling");
        if (doorstepCheckBox.isChecked())
            list.add("Door step Service");
        if (paintingCheckBox.isChecked())
            list.add("Painting");
        shop.put("Services",list);
        db.collection("Shops").document(shopNameEditText.getText().toString()).set(shop)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mProgressDialog.hide();
                        CoordinatorLayout cord=findViewById(R.id.coordinator_add_shop);
                        Snackbar snackbar = Snackbar
                                .make(cord, "Shop has been Added", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mProgressDialog.hide();
                        CoordinatorLayout cord=findViewById(R.id.coordinator_add_shop);
                        Snackbar snackbar = Snackbar
                                .make(cord, "Shop is not Added", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });
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
                            Geocoder geocoder;
                            List<Address> addresses=null;
                            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                            Last_Known_Location = task.getResult();
                            try {
                                addresses = geocoder.getFromLocation(Last_Known_Location.getLatitude() , Last_Known_Location.getLongitude(), 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            String latitude = String.valueOf(Last_Known_Location.getLatitude());
                            String longitude = String.valueOf(Last_Known_Location.getLongitude());
                            String latlng = latitude + "\n" + longitude;
                            String address = addresses.get(0).getAddressLine(0);
                            locationTextView.setText(latlng);
                            addressEditText.setText(address);
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
                            mmMap.getUiSettings().setMyLocationButtonEnabled(false);
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

    private void updateLocationUI() {
        if (mmMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mmMap.setMyLocationEnabled(true);
                mmMap.getUiSettings().setMyLocationButtonEnabled(false);
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
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case CAMERA_PIC_REQUEST:
                    if (resultCode == RESULT_OK) {
                        try {
                            cameraUri = data.getData();
                            ImgPhoto.setImageURI(cameraUri);

                        } catch (Exception e) {
                            Toast.makeText(AddShopActivity.this, "Not able to Upload", Toast.LENGTH_SHORT).show();
                            Toast.makeText(AddShopActivity.this, "Please Try Later", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
        }
    }
}