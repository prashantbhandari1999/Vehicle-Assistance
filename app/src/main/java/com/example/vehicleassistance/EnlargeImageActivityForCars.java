package com.example.vehicleassistance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class EnlargeImageActivityForCars extends AppCompatActivity {
    private ArrayList<String> carNames;
    private ArrayList<Integer> carPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enlarge_image_for_cars);
        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null) {
            carNames = bundle.getStringArrayList("text");
            carPhotos = bundle.getIntegerArrayList("image");

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            RecyclerView recyclerView = findViewById(R.id.added_car_recyclerView);
            recyclerView.setLayoutManager(linearLayoutManager);
            RecyclerViewAdpaterForAddedCars adapter = new RecyclerViewAdpaterForAddedCars(this, carNames, carPhotos);
            recyclerView.setAdapter(adapter);
        }

        FloatingActionButton floatingActionButton =findViewById(R.id.add_car_action_button);
        floatingActionButton.hide();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EnlargeImageActivityForCars.this,AddVehicleActivity.class);
                startActivity(intent);
            }
        });
    }
}
