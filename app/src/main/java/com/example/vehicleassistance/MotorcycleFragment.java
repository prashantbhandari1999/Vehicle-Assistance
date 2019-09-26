package com.example.vehicleassistance;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MotorcycleFragment extends Fragment {

    private View v;
    private RecyclerView recyclerView;
    private List<Motorcycles> motorcycleList;

    public MotorcycleFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        motorcycleList=new ArrayList<>();
        motorcycleList.add(new Motorcycles("Hero Splendor",R.drawable.ic_car_icon));
        motorcycleList.add(new Motorcycles("Hero Passion",R.drawable.ic_car_icon));
        motorcycleList.add(new Motorcycles("Hero Maestro",R.drawable.ic_car_icon));
        motorcycleList.add(new Motorcycles("Hero Pleasure",R.drawable.ic_car_icon));
        motorcycleList.add(new Motorcycles("Honda Shine",R.drawable.ic_car_icon));
        motorcycleList.add(new Motorcycles("Honda Hornet",R.drawable.ic_car_icon));
        motorcycleList.add(new Motorcycles("Honda Activa",R.drawable.ic_car_icon));
        motorcycleList.add(new Motorcycles("Honda Dio",R.drawable.ic_car_icon));
        motorcycleList.add(new Motorcycles("Bajaj Pulsar",R.drawable.ic_car_icon));
        motorcycleList.add(new Motorcycles("Bajaj Avenger",R.drawable.ic_car_icon));
        motorcycleList.add(new Motorcycles("TVS Apache",R.drawable.ic_car_icon));
        motorcycleList.add(new Motorcycles("TVS Wego",R.drawable.ic_car_icon));
        motorcycleList.add(new Motorcycles("TVS Jupiter",R.drawable.ic_car_icon));
        motorcycleList.add(new Motorcycles("Suzuki Access",R.drawable.ic_car_icon));
        motorcycleList.add(new Motorcycles("Royal Enfield Bullet",R.drawable.ic_car_icon));
        motorcycleList.add(new Motorcycles("Yamaha FZ",R.drawable.ic_car_icon));
        motorcycleList.add(new Motorcycles("Yamaha Fascino",R.drawable.ic_car_icon));



        ((AddVehicleActivity) getActivity()).setMotorcycleList(motorcycleList);

        v=inflater.inflate(R.layout.motorcycle_fragment,container,false);

        ((AddVehicleActivity)getActivity()).getViewMotorcycle(v);

        List list=getMotorcycleList();


        recyclerView=v.findViewById(R.id.RecylcerView_motorcycleFragment);
        RecyclerViewAdapterForMotorcycle recyclerViewApapter=new RecyclerViewAdapterForMotorcycle(getContext(),list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewApapter);

        return v;
    }

    public Context getApp(){
        return getContext();
    }

    public List<Motorcycles> getMotorcycleList() {
        return motorcycleList;
    }
}
