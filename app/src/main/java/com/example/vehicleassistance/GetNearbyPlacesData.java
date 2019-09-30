package com.example.vehicleassistance;

import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetNearbyPlacesData extends AsyncTask<Object,String,String> {
    String googlePlacesData;
    GoogleMap mMap;
    String url;
    Location userLocation;
    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap)objects[0];
        url= (String) objects[1];

        DownloadURLPlaces downloadURLPlaces = new DownloadURLPlaces();
        try {
            googlePlacesData=downloadURLPlaces.readURL(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String,String>> nearbyPlaceList=null;
        DataParserPlaces parser = new DataParserPlaces();
        nearbyPlaceList=parser.parse(s);
        showNearbyPlaces(nearbyPlaceList);
    }

    private void showNearbyPlaces(List<HashMap<String,String>> nearbyPlacesList){



        for (int i=0;i<nearbyPlacesList.size();i++){
            MarkerOptions markerOptions =new MarkerOptions();
            HashMap<String,String> googlePlace = nearbyPlacesList.get(i);

            String placeName = googlePlace.get("place_name");
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));

            float results[] = new float[10];
            Location.distanceBetween(lat,lng,userLocation.getLatitude(),userLocation.getLongitude(),results);

            LatLng latLng = new LatLng(lat,lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName+" : "+results[0]/1000+"km away");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
        }

    }

    public void setUserLocation(Location location) {
        userLocation = location;
    }
}
