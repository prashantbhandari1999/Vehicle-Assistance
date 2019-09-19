package com.example.vehicleassistance;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EnlargeImageActivityForMotorcycles extends AppCompatActivity {
    private ArrayList<String> motorcycleName;
    private ArrayList<Integer> motorcyclePhotos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enlarge_image_for_motorcycles);

        Bundle bundle = this.getIntent().getExtras();

        motorcycleName = bundle.getStringArrayList("text");
        motorcyclePhotos = bundle.getIntegerArrayList("image");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.added_motorcycle_recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerViewAdapterForAddedMotorcycles adapter = new RecyclerViewAdapterForAddedMotorcycles( motorcycleName, motorcyclePhotos,this);
        recyclerView.setAdapter(adapter);

    }
}
