package com.example.vehicleassistance;

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

    View v;
    private RecyclerView recyclerView;
    private List<Motorcycles> motorcycleList;

    public MotorcycleFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        motorcycleList=new ArrayList<>();
        motorcycleList.add(new Motorcycles("scooter1",R.drawable.ic_motorcycle));
        motorcycleList.add(new Motorcycles("scooter2",R.drawable.ic_motorcycle));
        motorcycleList.add(new Motorcycles("scooter3",R.drawable.ic_motorcycle));
        motorcycleList.add(new Motorcycles("scooter4",R.drawable.ic_motorcycle));
        motorcycleList.add(new Motorcycles("scooter5",R.drawable.ic_motorcycle));
        motorcycleList.add(new Motorcycles("scooter6",R.drawable.ic_motorcycle));
        motorcycleList.add(new Motorcycles("scooter7",R.drawable.ic_motorcycle));
        motorcycleList.add(new Motorcycles("scooter8",R.drawable.ic_motorcycle));
        motorcycleList.add(new Motorcycles("scooter9",R.drawable.ic_motorcycle));
        motorcycleList.add(new Motorcycles("scooter10",R.drawable.ic_motorcycle));
        motorcycleList.add(new Motorcycles("scooter11",R.drawable.ic_motorcycle));
        motorcycleList.add(new Motorcycles("scooter12",R.drawable.ic_motorcycle));

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.motorcycle_fragment,container,false);
        recyclerView=v.findViewById(R.id.RecylcerView_motorcycleFragment);
        RecyclerViewAdapterForMotorcycle recyclerViewApapter=new RecyclerViewAdapterForMotorcycle(getContext(),motorcycleList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewApapter);

        return v;
    }
}
