package com.example.vehicleassistance;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OldNotificationsFragment extends Fragment {
    View view;
    private RecyclerView recyclerView;
    private List<old_notifications> list = new ArrayList<>();
    RecyclerViewAdpaterForOldNotifications adapter;
    FirebaseFirestore db;
    FirebaseAuth lAuth;

    public OldNotificationsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
        list.add(new old_notifications("!st Notification", "Hello this is your notif", "30/04/1999", R.drawable.car_wash));

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_old_notifications, container, false);
        recyclerView = view.findViewById(R.id.RecylcerView_old_notifications);
        RecyclerViewAdpaterForOldNotifications adapter = new RecyclerViewAdpaterForOldNotifications(getContext(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void onStart() {
        super.onStart();
        db = FirebaseFirestore.getInstance();
        lAuth = FirebaseAuth.getInstance();
        db.collection("Users")
                .document(lAuth.getCurrentUser().getUid())
                .collection("Notifications")
                .document("Previous Notifications")
                .collection("C")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        list.clear();
                        if (queryDocumentSnapshots != null) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Notifications note = documentSnapshot.toObject(Notifications.class);
                                int ID = 0;
                                switch (note.getType()) {
                                    case "PUC":
                                        ID = R.drawable.puc;
                                        break;
                                    case "OIL":
                                        ID = R.drawable.oil;
                                        break;
                                    case "INSURANCE":
                                        ID = R.drawable.insurance;
                                        break;
                                    case "AIR":
                                        ID = R.drawable.air;
                                        break;
                                }
                                list.add(new old_notifications(note.getType(), note.getMessage(), note.getDate(), ID));
                                Log.d("JAN", note.getType() + " " + note.getMessage() + " " + note.getDate() + " " + note.getRemaining());
                                Log.d("UP", "Added");

                            }
                            Log.d("Size", " " + list.size());

                            adapter = new RecyclerViewAdpaterForOldNotifications(getContext(), list);
                            adapter.notifyDataSetChanged();
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(adapter);
                        }
                    }
                });
        adapter = new RecyclerViewAdpaterForOldNotifications(getContext(), list);
        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

}
