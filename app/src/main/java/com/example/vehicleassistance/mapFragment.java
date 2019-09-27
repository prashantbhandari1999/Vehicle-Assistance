package com.example.vehicleassistance;


import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class mapFragment extends Fragment implements OnMapReadyCallback {

    protected GoogleMap mMap;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private Location Last_Known_Location;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted = false;
    int PROXIMITY_RADIUS = 10000;
    double latitude, longitude;

    SupportMapFragment mapFragment;
    private FloatingActionButton GPSButton;

    public mapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mGeoDataClient = Places.getGeoDataClient(getContext(), null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(getContext(), null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());


        if (mapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

//        View mapView = super.onCreateView(inflater, container, savedInstanceState);
//
//        // Get the button view
//        View locationButton = ((View) mapView.findViewById(Integer.parseInt(String.valueOf(1))).getParent()).findViewById(Integer.parseInt(String.valueOf(2)));
//
//        // and next place it, for exemple, on bottom right (as Google Maps app)
//        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
//        // position on right bottom
//        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
//        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//        rlp.setMargins(0, 0, 30, 30);

        /*GPSButton=(FloatingActionButton)findViewById(R.id.myLocationButton);

        GPSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        */
        return view;
    }

    public void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
//        Toast.makeText(getContext(), "get device location", Toast.LENGTH_SHORT).show();

        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            Last_Known_Location = task.getResult();
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(

                                    new LatLng(Last_Known_Location.getLatitude(),
                                            Last_Known_Location.getLongitude()), DEFAULT_ZOOM));
                            ((HomeScreenActivity) getActivity()).getLastKnownLocation(Last_Known_Location);
                            mMap.setMyLocationEnabled(true);
                        } else {
                            mMap.animateCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(true);
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


    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
//            Toast.makeText(getContext(), "getLocationPermission:mLocationPermissionGranted:-"+mLocationPermissionGranted, Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
//            Toast.makeText(getContext(), "getLocationPermission:mLocationPermissionGranted:-"+mLocationPermissionGranted, Toast.LENGTH_SHORT).show();
        }
        updateLocationUI();
//        onRequestPermissionsResult(PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION,PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }

    /**
     * Handles the result of the request for location permissions.
     */
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
//                    Toast.makeText(getContext(), "onRequestPermissionsResult:-", Toast.LENGTH_SHORT).show();
                }
            }
//            Toast.makeText(getContext(), "onRequestPermissionsResult:-"+mLocationPermissionGranted, Toast.LENGTH_SHORT).show();
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            Toast.makeText(getContext(), "Map is null in updateUI", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                Last_Known_Location = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        Toast.makeText(getContext(), "In map ready", Toast.LENGTH_SHORT).show();
        mMap.clear();

        // Add a marker in Sydney and move the camera
        LatLng pune = new LatLng(18.5204, 73.8567);
        mMap.addMarker(new MarkerOptions().position(pune).title("Marker in Pune"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pune));
        if (mMap.isMyLocationEnabled() == false) {
            getLocationPermission();
            getDeviceLocation();
        }
        ((HomeScreenActivity) getActivity()).getMapObject(mMap);

    }
}
