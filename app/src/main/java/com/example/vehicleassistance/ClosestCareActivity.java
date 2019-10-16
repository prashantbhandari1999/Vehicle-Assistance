package com.example.vehicleassistance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class ClosestCareActivity extends AppCompatActivity implements GetClosestCare.AsyncResponse {
    String placeId = "";
    String contact = null;
    GetClosestCare getClosestCare = new GetClosestCare();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closest_care);
        Intent intent = getIntent();
        placeId = intent.getStringExtra("place_id");
        getClosestCare.delegate = this;
        Object data[] = new Object[1];
        data[0] = "https://maps.googleapis.com/maps/api/place/details/json?place_id=" + placeId + "&fields=name,rating,formatted_phone_number&key=" + BuildConfig.google_maps_key;
        Log.d("url:", "onCreate: "+data[0]);
        getClosestCare.execute(data);
        if (!(ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED)) {
            getCallPermissions();
        }
    }

    public void getCallPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CALL_PHONE},
                1);
    }

    @Override
    public void processFinish(HashMap<String,String> output) {
        try {
            contact = output.get("contact");
        }
        catch (Exception e){

        }
        if (contact == null) {
            Log.d("contact", "processFinish: "+contact);
            Toast.makeText(this, "Network Error!! Try Again After Some Time", Toast.LENGTH_LONG).show();
            finish();
        }
        else {
            TextView contact_text = findViewById(R.id.textView_number_1);
            contact_text.setText(contact);

            if((ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED)) {
                new AlertDialog.Builder(this)
                        .setIcon(null)
                        .setTitle("Call Closest Care")
                        .setMessage("Are you sure you want to call ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel: " + contact));
                                if((ContextCompat.checkSelfPermission(ClosestCareActivity.this,Manifest.permission.CALL_PHONE)
                                        == PackageManager.PERMISSION_GRANTED)) {
                                    startActivity(intent);
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .show();
            }
        }
    }
}
