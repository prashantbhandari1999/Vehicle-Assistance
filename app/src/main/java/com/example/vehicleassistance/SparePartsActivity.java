package com.example.vehicleassistance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;

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
        mExampleList.add(new ExampleSpareParts(R.drawable.ic_car_icon, "Airbag Sensors","500 Rs" ));
        mExampleList.add(new ExampleSpareParts(R.drawable.ic_car_icon, "Battery","500 Rs"));
        mExampleList.add(new ExampleSpareParts(R.drawable.ic_car_icon, "Brakes","500 Rs"));
        mExampleList.add(new ExampleSpareParts(R.drawable.ic_car_icon, "Shock Absorbers","500 Rs"));
        mExampleList.add(new ExampleSpareParts(R.drawable.ic_car_icon, "Fuel Injector","500 Rs"));
        mExampleList.add(new ExampleSpareParts(R.drawable.ic_car_icon, "Clutch","500 Rs"));
        mExampleList.add(new ExampleSpareParts(R.drawable.ic_car_icon, "Mirrors","500 Rs"));
        mExampleList.add(new ExampleSpareParts(R.drawable.ic_car_icon, "Horns","500 Rs"));
        mExampleList.add(new ExampleSpareParts(R.drawable.ic_car_icon, "Oil filter ","500 Rs"));
        mExampleList.add(new ExampleSpareParts(R.drawable.ic_car_icon, "Audio/Video Devices","500 Rs"));
        mExampleList.add(new ExampleSpareParts(R.drawable.ic_car_icon, "Spark Plugs","500 Rs"));
        mExampleList.add(new ExampleSpareParts(R.drawable.ic_car_icon, "Indicators ","500 Rs"));
        mExampleList.add(new ExampleSpareParts(R.drawable.ic_car_icon, "Interior Lights for Cars","500 Rs"));
        mExampleList.add(new ExampleSpareParts(R.drawable.ic_car_icon, "Fuel Indicators","500 Rs"));
        mExampleList.add(new ExampleSpareParts(R.drawable.ic_car_icon, "Spedometers","500 Rs"));
        mExampleList.add(new ExampleSpareParts(R.drawable.ic_car_icon, "Tyres","500 Rs"));

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
