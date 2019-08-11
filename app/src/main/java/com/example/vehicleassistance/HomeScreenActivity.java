package com.example.vehicleassistance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class HomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Toast.makeText(this, "Welcome to Vehicle Assistance", Toast.LENGTH_LONG).show();
    }
}
