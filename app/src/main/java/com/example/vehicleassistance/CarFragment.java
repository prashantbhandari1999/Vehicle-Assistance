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

public class CarFragment extends Fragment {
    View v;
    private RecyclerView recyclerView;
    private List<Cars> carList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        carList=new ArrayList<>();
        carList.add(new Cars("car1",R.drawable.ic_car_icon));
        carList.add(new Cars("car2",R.drawable.ic_car_icon));
        carList.add(new Cars("car3",R.drawable.ic_car_icon));
        carList.add(new Cars("car4",R.drawable.ic_car_icon));
        carList.add(new Cars("car5",R.drawable.ic_car_icon));
        carList.add(new Cars("car6",R.drawable.ic_car_icon));
        carList.add(new Cars("car7",R.drawable.ic_car_icon));
        carList.add(new Cars("car8",R.drawable.ic_car_icon));
        carList.add(new Cars("car9",R.drawable.ic_car_icon));
        carList.add(new Cars("car10",R.drawable.ic_car_icon));
        carList.add(new Cars("car11",R.drawable.ic_car_icon));
        carList.add(new Cars("car12",R.drawable.ic_car_icon));

    }

    public CarFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_car,container,false);
        recyclerView=v.findViewById(R.id.RecylcerView_carsFragment);
        RecyclerViewApapterForCar recyclerViewApapter=new RecyclerViewApapterForCar(getContext(),carList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewApapter);

        return v;
    }
}
