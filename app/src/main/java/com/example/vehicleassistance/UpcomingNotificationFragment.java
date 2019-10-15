package com.example.vehicleassistance;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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
    private List<upcoming_notifications> list;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseFirestore db;
    FirebaseAuth lAuth;
    private UpcomingNotificationFragment.OnFragmentInteractionListener mListener;

    public UpcomingNotificationFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
        list.add(new upcoming_notifications("!st Notification", "Hello this is your notif", "30/04/1999", R.drawable.service_center));
        db=FirebaseFirestore.getInstance();
        lAuth = FirebaseAuth.getInstance();
        db.collection("Users")
                .document(lAuth.getCurrentUser().getUid())
                .collection("Notifications")
                .document("Upcoming Notifications")
                .collection("C")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : documentSnapshots) {
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
                            Toast.makeText(getContext(), "T:"+note.getType()+ " :-"+note.getMessage(), Toast.LENGTH_SHORT).show();
//                            Toast.makeText(getContext(), "Inside", Toast.LENGTH_SHORT).show();
                            list.add(new upcoming_notifications(note.getType(), note.getMessage(), note.getDate(), R.drawable.service_center));

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error getting data!!!", Toast.LENGTH_LONG).show();

                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_upcoming_notification, container, false);
        recyclerView = view.findViewById(R.id.RecylcerView_upcoming_notification);
        RecyclerViewAdapterForUpcomingNotifications adapter = new RecyclerViewAdapterForUpcomingNotifications(getContext(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
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

    /*@Override
    public void onStart() {
        super.onStart();
        db=FirebaseFirestore.getInstance();
        lAuth = FirebaseAuth.getInstance();
        db.collection("Users")
                .document(lAuth.getCurrentUser().getUid())
                .collection("Notifications")
                .document("Upcoming Notifications")
                .collection("C")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : documentSnapshots) {
                            Notifications note = documentSnapshot.toObject(Notifications.class);
                            list = new ArrayList<>();
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

                            list.add(new upcoming_notifications(note.getType(), note.getMessage(), note.getDate(), ID));

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error getting data!!!", Toast.LENGTH_LONG).show();

                    }
                });
    }*/
}

