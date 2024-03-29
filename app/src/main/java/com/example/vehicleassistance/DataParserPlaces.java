package com.example.vehicleassistance;

import android.util.Log;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParserPlaces {

    private HashMap<String,String> getPlace(JSONObject googlePlaceJson){
        HashMap<String,String> googlePlacesMap = new HashMap<>();
        String placeName="-NA-";
        String place_id="";
        String latitude="";
        String longitude="";
        String reference="";

        try {
        if(!googlePlaceJson.isNull("name")){
                placeName = googlePlaceJson.getString("name");

        }
        if(!googlePlaceJson.isNull("place_id")){
            place_id = googlePlaceJson.getString("place_id");
        }
            Log.d("DataParser placeID", "getPlace: "+place_id);
        latitude=googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
        longitude=googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");

        reference=googlePlaceJson.getString("reference");

        googlePlacesMap.put("place_name",placeName);
        googlePlacesMap.put("place_id",place_id);
        googlePlacesMap.put("lat",latitude);
        googlePlacesMap.put("lng",longitude);
        googlePlacesMap.put("reference",reference);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return googlePlacesMap;
    }

    private List<HashMap<String,String>> getPlaces(JSONArray jsonArray){
        int count;
        if(jsonArray != null) {
            count = jsonArray.length();
        }
        else{
            count=0;
        }
        List<HashMap<String,String>> placesList = new ArrayList<>();
        HashMap<String,String> placeMap;

        for(int i=0;i<count;i++){
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placesList.add(placeMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return  placesList;
    }

    public List<HashMap<String,String>> parse(String jsonData){
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonData);
            if(jsonObject.get("status").equals("OK"))
                jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getPlaces(jsonArray);
    }
}
