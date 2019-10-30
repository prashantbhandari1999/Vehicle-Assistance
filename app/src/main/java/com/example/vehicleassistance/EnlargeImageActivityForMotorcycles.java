package com.example.vehicleassistance;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class EnlargeImageActivityForMotorcycles extends AppCompatActivity {
    private ArrayList<String> motorcycleName = new ArrayList<>();
    private ArrayList<Integer> motorcyclePhotos = new ArrayList<>();
    private FloatingActionButton floatingActionButton;
    FirebaseFirestore db;
    FirebaseAuth lAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enlarge_image_for_motorcycles);
        floatingActionButton = findViewById(R.id.add_motorcycle_action_button);
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
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
                Intent intent = new Intent(EnlargeImageActivityForMotorcycles.this, AddVehicleActivity.class);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem menu) {
        int id = menu.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        db = FirebaseFirestore.getInstance();
        lAuth = FirebaseAuth.getInstance();
        db.collection("Users")
                .document(lAuth.getCurrentUser().getUid())
                .collection("My Vehicles")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        motorcyclePhotos.clear();
                        motorcyclePhotos.clear();
                        if (queryDocumentSnapshots != null) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                int image = 0;
                                CarsFirebase cars = documentSnapshot.toObject(CarsFirebase.class);
                                Log.d("CARS", cars.getCarName());
                                switch (cars.getCarName()) {
                                    case "Splendor":
                                        image = R.drawable.hero_splendor;
                                        break;
                                    case "Passion":
                                        image = R.drawable.hero_passion;
                                        break;
                                    case "Maestro":
                                        image = R.drawable.hero_maestro;
                                        break;
                                    case "Pleasure":
                                        image = R.drawable.hero_pleasure;
                                        break;
                                    case "Shine":
                                        image = R.drawable.honda_shine;
                                        break;
                                    case "Hornet":
                                        image = R.drawable.honda_hornet;
                                        break;
                                    case "Activa":
                                        image = R.drawable.honda_activa;
                                        break;
                                    case "Dio":
                                        image = R.drawable.honda_dio;
                                        break;
                                    case "Pulsar":
                                        image = R.drawable.bajaj_pulsar;
                                        break;
                                    case "Apache":
                                        image = R.drawable.tvs_apache;
                                        break;
                                    case "Avenger":
                                        image = R.drawable.bajaj_avenger;
                                        break;
                                    case "Wego":
                                        image = R.drawable.tvs_wego;
                                        break;
                                    case "Bullet":
                                        image = R.drawable.royalenfield_bullet;
                                        break;
                                    case "FZ":
                                        image = R.drawable.yamaha_fz;
                                        break;
                                    case "Fascino":
                                        image = R.drawable.yamaha_fascino;
                                        break;
                                    case "Jupiter":
                                        image = R.drawable.tvs_jupiter;
                                        break;
                                    case "Access":
                                        image = R.drawable.suzuki_access;
                                        break;
                                    case "Dzire":
                                        image = R.drawable.maruti_dzire;
                                        break;
                                    case "Wagon R":
                                        image = R.drawable.maruti_wagonr;
                                        break;
                                    case "Santro":
                                        image = R.drawable.hyundai_santro;
                                        break;
                                    case "Xcent":
                                        image = R.drawable.hyundai_xcent;
                                        break;
                                    case "Amaze":
                                        image = R.drawable.honda_amaze;
                                        break;
                                    case "City":
                                        image = R.drawable.honda_city;
                                        break;
                                    case "Xylo":
                                        image = R.drawable.mahindra_xylo;
                                        break;
                                    case "Polo":
                                        image = R.drawable.volkswagen_polo;
                                        break;
                                    case "Vento":
                                        image = R.drawable.volkswagen_vento;
                                        break;
                                    case "Figo":
                                        image = R.drawable.ford_figo;
                                        break;
                                    case "Tiago":
                                        image = R.drawable.tata_tiago;
                                        break;
                                    case "Nano":
                                        image = R.drawable.tata_nano;
                                        break;
                                    case "Duster":
                                        image = R.drawable.renault_duster;
                                        break;
                                    case "Corolla Altis":
                                        image = R.drawable.toyota_corolla_altis;
                                        break;
                                    case "Fortuner":
                                        image = R.drawable.toyota_fortuner;
                                        break;
                                    default:
                                        image = R.drawable.tata_tiago;
                                }
                                motorcycleName.add(cars.getCarName());
                                motorcyclePhotos.add(image);
                            }
                        }
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                        RecyclerView recyclerView = findViewById(R.id.added_motorcycle_recyclerView);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        RecyclerViewAdapterForAddedMotorcycles adapter = new RecyclerViewAdapterForAddedMotorcycles(motorcycleName, motorcyclePhotos, getApplicationContext());
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                    }
                });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.added_motorcycle_recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerViewAdapterForAddedMotorcycles adapter = new RecyclerViewAdapterForAddedMotorcycles(motorcycleName, motorcyclePhotos, getApplicationContext());
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}
