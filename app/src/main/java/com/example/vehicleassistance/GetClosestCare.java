package com.example.vehicleassistance;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import java.io.IOException;
import java.util.HashMap;

public class GetClosestCare extends AsyncTask<Object, String, String> {
    String googlePlacesData;
    GoogleMap mMap;
    String url;
    Location userLocation;
    String contact = null;
    public AsyncResponse delegate = null;


    @Override
    protected String doInBackground(Object... objects) {
        url = (String) objects[0];

        DownloadURLPlaces downloadURLPlaces = new DownloadURLPlaces();
        try {
            googlePlacesData = downloadURLPlaces.readURL(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        HashMap<String, String> nearbyPlaceList = null;
        ContactParser parser = new ContactParser();
        nearbyPlaceList = parser.parse(s);
        try {
            contact = nearbyPlaceList.get("contact");
        }
        catch (Exception e){

        }
        delegate.processFinish(contact);
    }

    public interface AsyncResponse {
        void processFinish(String output);
    }

}