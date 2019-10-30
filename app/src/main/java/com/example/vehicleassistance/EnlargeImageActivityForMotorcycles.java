package com.example.vehicleassistance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class EnlargeImageActivityForMotorcycles extends AppCompatActivity {
    private ArrayList<String> motorcycleName;
    private ArrayList<Integer> motorcyclePhotos;
    private FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enlarge_image_for_motorcycles);
        floatingActionButton = findViewById(R.id.add_motorcycle_action_button);
        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null) {
            motorcycleName = bundle.getStringArrayList("text");
            motorcyclePhotos = bundle.getIntegerArrayList("image");

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            RecyclerView recyclerView = findViewById(R.id.added_motorcycle_recyclerView);
            recyclerView.setLayoutManager(linearLayoutManager);
            RecyclerViewAdapterForAddedMotorcycles adapter = new RecyclerViewAdapterForAddedMotorcycles(motorcycleName, motorcyclePhotos, this);
            recyclerView.setAdapter(adapter);
        }
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(EnlargeImageActivityForMotorcycles.this,AddVehicleActivity.class);
                    startActivity(intent);
                }
            });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem menu){
        int id = menu.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return true;
    }
}
