package com.example.vehicleassistance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SparePartsActivity extends AppCompatActivity {

    private ArrayList<ExampleSpareParts> mExampleList;
    private Spare_Parts_Adapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spare_parts);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        createExampleList();
        buildRecyclerView();


        EditText editText = findViewById(R.id.editText_spareparts);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

    }




    private void filter(String text) {
        ArrayList<ExampleSpareParts> filteredList = new ArrayList<>();

        for (ExampleSpareParts item : mExampleList) {
            if (item.getText1().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        mAdapter.filterList(filteredList);
    }

    private void createExampleList() {
        mExampleList = new ArrayList<>();
        mExampleList.add(new ExampleSpareParts(R.drawable.airbag, "Airbag Sensors" ));
        mExampleList.add(new ExampleSpareParts(R.drawable.battery, "Battery"));
        mExampleList.add(new ExampleSpareParts(R.drawable.brakes, "Brakes"));
        mExampleList.add(new ExampleSpareParts(R.drawable.shock, "Shock Absorbers"));
        mExampleList.add(new ExampleSpareParts(R.drawable.fuel_injector, "Fuel Injector"));
        mExampleList.add(new ExampleSpareParts(R.drawable.clutch, "Clutch"));
        mExampleList.add(new ExampleSpareParts(R.drawable.mirror, "Mirrors"));
        mExampleList.add(new ExampleSpareParts(R.drawable.horn, "Horns"));
        mExampleList.add(new ExampleSpareParts(R.drawable.oil, "Oil filter "));
        mExampleList.add(new ExampleSpareParts(R.drawable.radio, "Audio/Video Devices"));
        mExampleList.add(new ExampleSpareParts(R.drawable.spark, "Spark Plugs"));
        mExampleList.add(new ExampleSpareParts(R.drawable.bulb, "Interior Lights for Cars"));
        mExampleList.add(new ExampleSpareParts(R.drawable.fuel_indicator, "Fuel Indicators"));
        mExampleList.add(new ExampleSpareParts(R.drawable.speed, "Speedometers"));
        mExampleList.add(new ExampleSpareParts(R.drawable.tyres, "Tyres"));

    }

    private void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView_spareparts);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new Spare_Parts_Adapter(mExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    public boolean onOptionsItemSelected(MenuItem menu) {
        int id = menu.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

}
