package com.example.vehicleassistance;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerViewAdapterForMotorcycle extends RecyclerView.Adapter<RecyclerViewAdapterForMotorcycle.MyViewHolderForMotorcycles> {

    private Context context;
    private List<Motorcycles> list;
    private ArrayList<Integer> motorcyclePhotos = new ArrayList<>();
    private ArrayList<String> motorcycleName = new ArrayList<>();
    FirebaseFirestore db;
    FirebaseAuth lAuth;

    public RecyclerViewAdapterForMotorcycle(Context context, List<Motorcycles> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolderForMotorcycles onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        db = FirebaseFirestore.getInstance();
        lAuth = FirebaseAuth.getInstance();
        v = LayoutInflater.from(context).inflate(R.layout.item_motorcyle_fragment, parent, false);
        final MyViewHolderForMotorcycles vHolder = new MyViewHolderForMotorcycles(v);
        vHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("User", lAuth.getCurrentUser().getUid());
                final String carName = list.get(vHolder.getAdapterPosition()).getMotorcycleName();
                final Map<String, Object> Cars = new HashMap<>();
                Cars.put("carName", carName);
                new AlertDialog.Builder(context)
                        .setTitle("Add Motorcycle")
                        .setMessage("Are you sure you want to add " + carName + " ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((Activity)context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                                db.collection("Users")
                                        .document(lAuth.getCurrentUser().getUid())
                                        .collection("My Vehicles")
                                        .add(Cars)
                                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                Toast.makeText(context, "Vehicle added successfully", Toast.LENGTH_LONG).show();
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        ((Activity)context).finish();
                                                    }
                                                }, 2000);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
//                                                Toast.makeText(context, "Vehicle not added", Toast.LENGTH_SHORT).show();
                                                Log.d("Car", "Not added");
                                            }
                                        });

                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderForMotorcycles holder, int position) {
        holder.motorcycleNameTextView.setText(list.get(position).getMotorcycleName());
        holder.motorcyclePhotoImageView.setImageResource(list.get(position).getMotorcyclePhoto());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolderForMotorcycles extends RecyclerView.ViewHolder {
        private TextView motorcycleNameTextView;
        private ImageView motorcyclePhotoImageView;
        private LinearLayout linearLayout;

        public MyViewHolderForMotorcycles(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.layout_motorcycleFragment);
            motorcycleNameTextView = itemView.findViewById(R.id.TextView_motorcycleFragment);
            motorcyclePhotoImageView = itemView.findViewById(R.id.image_motorcycleFragment);
        }
    }

}
