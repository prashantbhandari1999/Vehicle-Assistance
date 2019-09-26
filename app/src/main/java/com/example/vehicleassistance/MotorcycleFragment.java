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
        motorcycleList.add(new Motorcycles("Splendor",R.drawable.ic_car_icon));
        motorcycleList.add(new Motorcycles("Passion",R.drawable.ic_car_icon));
        motorcycleList.add(new Motorcycles("Maestro",R.drawable.ic_car_icon));
        motorcycleList.add(new Motorcycles("Pleasure",R.drawable.ic_car_icon));
        motorcycleList.add(new Motorcycles("Shine",R.drawable.ic_car_icon));
        motorcycleList.add(new Motorcycles("Hornet",R.drawable.ic_car_icon));
        motorcycleList.add(new Motorcycles("Activa",R.drawable.ic_car_icon));
        motorcycleList.add(new Motorcycles("Dio",R.drawable.ic_car_icon));
        motorcycleList.add(new Motorcycles("Pulsar",R.drawable.ic_car_icon));
        motorcycleList.add(new Motorcycles("Avenger",R.drawable.ic_car_icon));
        motorcycleList.add(new Motorcycles("Apache",R.drawable.ic_car_icon));
        motorcycleList.add(new Motorcycles("Wego",R.drawable.ic_car_icon));
        motorcycleList.add(new Motorcycles("Jupiter",R.drawable.ic_car_icon));
        motorcycleList.add(new Motorcycles("Access",R.drawable.ic_car_icon));
        motorcycleList.add(new Motorcycles("Bullet",R.drawable.ic_car_icon));
        motorcycleList.add(new Motorcycles("FZ",R.drawable.ic_car_icon));
        motorcycleList.add(new Motorcycles("Fascino",R.drawable.ic_car_icon));



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
