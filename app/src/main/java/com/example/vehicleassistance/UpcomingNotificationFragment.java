package com.example.vehicleassistance;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UpcomingNotificationFragment extends Fragment {
    View view;
    private RecyclerView recyclerView;
    private List<upcoming_notifications> list = new ArrayList<>();
    RecyclerViewAdapterForUpcomingNotifications adapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseFirestore db;
    FirebaseAuth lAuth;
    upcoming_notifications up[] = new upcoming_notifications[50];
    private UpcomingNotificationFragment.OnFragmentInteractionListener mListener;
    int count = 0;

    public UpcomingNotificationFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_upcoming_notification, container, false);
        recyclerView = view.findViewById(R.id.RecylcerView_upcoming_notification);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UpcomingNotificationFragment.OnFragmentInteractionListener) {
            mListener = (UpcomingNotificationFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onStart() {
        super.onStart();
        db = FirebaseFirestore.getInstance();
        lAuth = FirebaseAuth.getInstance();
        db.collection("Users")
                .document(lAuth.getCurrentUser().getUid())
                .collection("Notifications")
                .document("Upcoming Notifications")
                .collection("C")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        list.clear();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Notifications note = documentSnapshot.toObject(Notifications.class);
                            int ID=0;
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
                            list.add(new upcoming_notifications(note.getType(), note.getMessage(), note.getDate(),ID));
                        }
                        adapter = new RecyclerViewAdapterForUpcomingNotifications(getContext(), list);
//                        adapter.notifyDataSetChanged();
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.setAdapter(adapter);
                    }
                });
    }
}

