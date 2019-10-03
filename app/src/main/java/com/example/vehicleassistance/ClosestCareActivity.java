package com.example.vehicleassistance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
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
    public void processFinish(String output) {
        contact = getClosestCare.contact;
        if (contact == null) {
            Toast.makeText(this, "Network Error!! Try Again After Some Time", Toast.LENGTH_LONG);
        }
        TextView contact_text = findViewById(R.id.textView_number_1);
        contact_text.setText(contact);
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel: " + contact));
        startActivity(intent);
    }
}
