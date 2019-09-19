package com.example.vehicleassistance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class AddVehicleActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;  //object of the class ViewPagerAdapter
    private EditText editText;
    private List<Cars> carList;
    private List<Motorcycles>motorcycleList;
    View v,v1;
    RecyclerView recyclerViewForCar,recyclerViewForMotorcycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);
        tabLayout = findViewById(R.id.tabLayout_addVehicleActivity);
        viewPager = findViewById(R.id.ViewPager_addVehicleActivity);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        /*
         * Adding the fragment using the add fragment method of the
         * ViewPagerAdapter class.
         * */

        viewPagerAdapter.addFragment(new CarFragment(), "");
        viewPagerAdapter.addFragment(new MotorcycleFragment(), "");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_car_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_motorcycle);


        editText = findViewById(R.id.editText_addVehicleActivity);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.v("Location", "Inside afterTextChanged method");
                filter(editable.toString());
                filterScooter(editable.toString());
            }
        });
    }

    public void filter(String text) {
        ArrayList<Cars> filteredList = new ArrayList<>();
        for (Cars item : carList) {
            if (item.getCarName().toLowerCase().contains(text.toLowerCase().trim())) {
                filteredList.add(item);
                Log.d("Entered", "filter: 1");
            }
        }

        if (filteredList == null) {
            TextView l =findViewById(R.id.TextView_motorcycleFragment);
            l.setText("No results found");
        }
        RecyclerViewApapterForCar recyclerViewApapterForCar = new RecyclerViewApapterForCar(getApplicationContext(), filteredList);
        recyclerViewApapterForCar.notifyDataSetChanged();

        recyclerViewForCar.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewForCar.setAdapter(recyclerViewApapterForCar);

    }

    public void filterScooter(String text){
        ArrayList<Motorcycles> filterListForScooter=new ArrayList<>();
        for(Motorcycles item: motorcycleList){
            if(item.getMotorcycleName().toLowerCase().contains(text.toLowerCase().trim())){
                filterListForScooter.add(item);
            }
        }
        RecyclerViewAdapterForMotorcycle recyclerViewAdapterForMotorcycle=new RecyclerViewAdapterForMotorcycle(getApplicationContext(),filterListForScooter);
        recyclerViewAdapterForMotorcycle.notifyDataSetChanged();
        recyclerViewForMotorcycle.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewForMotorcycle.setAdapter(recyclerViewAdapterForMotorcycle);


    }
//

    public void getEditText(EditText eT) {
        editText = eT;
    }

    public void setCarList(List<Cars> cl) {
        carList = cl;
    }

    public void getViewCar(View view) {
        v = view;
        recyclerViewForCar = v.findViewById(R.id.RecylcerView_carsFragment);

    }

    public void setMotorcycleList(List<Motorcycles>l){
        motorcycleList=l;
    }

    public void getViewMotorcycle(View v){
        v1=v;
        recyclerViewForMotorcycle=v1.findViewById(R.id.RecylcerView_motorcycleFragment);

    }

}


