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

public class OldNotificationsFragment extends Fragment {
    View view;
    private RecyclerView recyclerView;
    private List<old_notifications>list;

    public OldNotificationsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list=new ArrayList<>();
        list.add(new old_notifications("!st Notification","Hello this is your notif","30/04/1999",R.drawable.car_wash));

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_old_notifications,container,false);
        recyclerView = view.findViewById(R.id.RecylcerView_old_notifications);
        RecyclerViewAdpaterForOldNotifications adapter=new RecyclerViewAdpaterForOldNotifications(getContext(),list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        return view;
    }
}
