package com.example.vehicleassistance;

import android.util.Log;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactParser {

    private HashMap<String,String> getPlace(JSONObject googlePlaceJson){
        HashMap<String,String> googlePlacesMap = new HashMap<>();
        String placeName = null ,Address = null ,contact = null ,rating = null ,opening_hours = null;

        try {
            if(!googlePlaceJson.isNull("name")){
                placeName = googlePlaceJson.getString("name");

            }
            if(!googlePlaceJson.isNull("formatted_phone_number")){
                contact = googlePlaceJson.getString("formatted_phone_number");
            }
            rating=String.valueOf(googlePlaceJson.getDouble("rating"));
            try {
                Address = googlePlaceJson.getString("vicinity");
            }
            catch (Exception e){

            }
            try {
                JSONObject open = googlePlaceJson.getJSONObject("opening_hours");
                opening_hours = open.getString("weekday_text");

            }
            catch (Exception e){

            }
            googlePlacesMap.put("place_name",placeName);
            googlePlacesMap.put("contact",contact);
            googlePlacesMap.put("rating",rating);
            googlePlacesMap.put("Address",Address);
            googlePlacesMap.put("Opening hours",opening_hours);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return googlePlacesMap;
    }

    private HashMap<String,String> getPlaces(JSONObject jsonArray){
        int count;
        if(jsonArray != null) {
            count = jsonArray.length();
        }
        else{
            count=0;
        }
        HashMap<String,String> placeMap=null;

        for(int i=0;i<count;i++){
            try {
                placeMap = getPlace(jsonArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return  placeMap;
    }

    public HashMap<String,String> parse(String jsonData){
        JSONObject jsonObject,jsonArray=null;

        try {
            jsonObject = new JSONObject(jsonData);
            Log.d("JSONObject ", "parse: "+jsonObject);
            if(jsonObject.get("status").equals("OK"))
            jsonArray = jsonObject.getJSONObject("result");
            Log.d("JsonArray:", "parse: "+jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getPlaces(jsonArray);
    }
}
