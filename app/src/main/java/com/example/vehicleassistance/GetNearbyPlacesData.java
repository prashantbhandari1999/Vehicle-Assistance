package com.example.vehicleassistance;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetNearbyPlacesData extends AsyncTask<Object,String,String> implements GoogleMap.InfoWindowAdapter {
    String googlePlacesData;
    String placeID="";
    GoogleMap mMap;
    String url;
    Location userLocation;
    AsyncResponse asyncResponse = null;
    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap)objects[0];
        url= (String) objects[1];

        Log.d("urlL", "doInBackground: "+url);

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

        float distances[] = new float[nearbyPlacesList.size()];

        for (int i=0;i<nearbyPlacesList.size();i++){
            MarkerOptions markerOptions =new MarkerOptions();
            HashMap<String,String> googlePlace = nearbyPlacesList.get(i);

            String placeName = googlePlace.get("place_name");
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));

            float results[] = new float[10];
            Location.distanceBetween(lat,lng,userLocation.getLatitude(),userLocation.getLongitude(),results);
            distances[i]=results[0];
            LatLng latLng = new LatLng(lat,lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName+" : "+results[0]/1000+"km away");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            markerOptions.snippet(googlePlace.get("place_id"));

            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
        }

        int index = 0;
        float minimum=0;
        if(distances.length>0)
         minimum = distances[0];
        for(int i=0;i<distances.length;i++){
            if(minimum>distances[i]){
               minimum=distances[i];
               index=i;
            }
        }
        if(minimum!=0) {
            placeID = nearbyPlacesList.get(index).get("place_id");
            Log.d("GetNearby placeID", "showNearbyPlaces: " + placeID);
        }
        asyncResponse.processFinish(placeID);
    }

        @Override
        public View getInfoWindow(Marker arg0) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {

////            Context mContext = H;
//            LinearLayout info = new LinearLayout(mContext);
////            info.setOrientation(LinearLayout.VERTICAL);
////
////            TextView title = new TextView(mContext);
////            title.setTextColor(Color.BLACK);
////            title.setGravity(Gravity.CENTER);
////            title.setTypeface(null, Typeface.BOLD);
////            title.setText(marker.getTitle());
////
////            TextView snippet = new TextView(mContext);
////            snippet.setTextColor(Color.GRAY);
////            snippet.setText(marker.getSnippet());
////
////            info.addView(title);
////            info.addView(snippet);
//
//            return info;
            return  null;
        }
    public interface AsyncResponse {
        void processFinish(String output);
    }


    public void setUserLocation(Location location) {
        userLocation = location;
    }
}
