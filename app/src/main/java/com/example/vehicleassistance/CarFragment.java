package com.example.vehicleassistance;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CarFragment extends Fragment {
    View v,v1;
    private RecyclerView recyclerView;
    private List<Cars> carList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public List<Cars> getCarList() {
        return carList;
    }

    public CarFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        carList = new ArrayList<>();
        carList.add(new Cars("car1", R.drawable.ic_car_icon));
        carList.add(new Cars("car2", R.drawable.ic_car_icon));
        carList.add(new Cars("car3", R.drawable.ic_car_icon));
        carList.add(new Cars("car4", R.drawable.ic_car_icon));
        carList.add(new Cars("car5", R.drawable.ic_car_icon));
        carList.add(new Cars("car6", R.drawable.ic_car_icon));


        ((AddVehicleActivity) getActivity()).setCarList(carList);

        v = inflater.inflate(R.layout.fragment_car, container, false);

        ((AddVehicleActivity)getActivity()).getViewCar(v);


        recyclerView = v.findViewById(R.id.RecylcerView_carsFragment);
        v1=inflater.inflate(R.layout.activity_main,container,false);
        EditText editText = v1.findViewById(R.id.editText_addVehicleActivity);
        ((AddVehicleActivity) getActivity()).getEditText(editText);
        List list=getCarList();

        RecyclerViewApapterForCar recyclerViewApapter = new RecyclerViewApapterForCar(getContext(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewApapter);
        return v;
    }

    public Context getApp(){
        return getContext();
    }
}
