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

public class UpcomingNotificationFragment extends Fragment {
    View view;
    private RecyclerView recyclerView;
    private List<upcoming_notifications> list;
    public UpcomingNotificationFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list=new ArrayList<>();
        list.add(new upcoming_notifications("!st Notification","Hello this is your notif","30/04/1999",R.drawable.service_center));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_upcoming_notification,container,false);
        recyclerView = view.findViewById(R.id.RecylcerView_upcoming_notification);
         RecyclerViewAdapterForUpcomingNotifications adapter=new RecyclerViewAdapterForUpcomingNotifications(getContext(),list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        return view;
    }
}
