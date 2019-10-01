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
        motorcycleList.add(new Motorcycles("Splendor",R.drawable.hero_splendor));
        motorcycleList.add(new Motorcycles("Passion",R.drawable.hero_passion));
        motorcycleList.add(new Motorcycles("Maestro",R.drawable.hero_maestro));
        motorcycleList.add(new Motorcycles("Pleasure",R.drawable.hero_pleasure));
        motorcycleList.add(new Motorcycles("Shine",R.drawable.honda_shine));
        motorcycleList.add(new Motorcycles("Hornet",R.drawable.honda_hornet));
        motorcycleList.add(new Motorcycles("Activa",R.drawable.honda_activa));
        motorcycleList.add(new Motorcycles("Dio",R.drawable.honda_dio));
        motorcycleList.add(new Motorcycles("Pulsar",R.drawable.bajaj_pulsar));
        motorcycleList.add(new Motorcycles("Avenger",R.drawable.bajaj_avenger));
        motorcycleList.add(new Motorcycles("Apache",R.drawable.tvs_apache));
        motorcycleList.add(new Motorcycles("Wego",R.drawable.tvs_wego));
        motorcycleList.add(new Motorcycles("Jupiter",R.drawable.tvs_jupiter));
        motorcycleList.add(new Motorcycles("Access",R.drawable.suzuki_access));
        motorcycleList.add(new Motorcycles("Bullet",R.drawable.royalenfield_bullet));
        motorcycleList.add(new Motorcycles("FZ",R.drawable.yamaha_fz));
        motorcycleList.add(new Motorcycles("Fascino",R.drawable.yamaha_fascino));

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
