package com.example.vehicleassistance;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class RecyclerViewApapterForCar extends RecyclerView.Adapter<RecyclerViewApapterForCar.MyViewHolder> {

    private Context context;
    private List<Cars> list;
    final ArrayList<String> carNames = new ArrayList<>();
    final ArrayList<Integer> carPhotos = new ArrayList<>();
    FirebaseFirestore db;
    FirebaseAuth lAuth;

    public RecyclerViewApapterForCar(Context context, List<Cars> list) {
        this.context = context;
        this.list = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        db = FirebaseFirestore.getInstance();
        lAuth = FirebaseAuth.getInstance();
        v = LayoutInflater.from(context).inflate(R.layout.items_car_fragment, parent, false);
        final MyViewHolder vHolder = new MyViewHolder(v);

        vHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("User", lAuth.getCurrentUser().getUid());
                final String carName = list.get(vHolder.getAdapterPosition()).getCarName();
                final Map<String, Object> Cars = new HashMap<>();
                Cars.put("carName", carName);
                ((Activity)context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                new AlertDialog.Builder(context)
                        .setTitle("Add Car")
                        .setMessage("Are you sure you want to add " + carName + " ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.collection("Users")
                                        .document(lAuth.getCurrentUser().getUid())
                                        .collection("My Vehicles")
                                        .add(Cars)
                                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                Toast.makeText(context, "Vehicle added successfully", Toast.LENGTH_SHORT).show();
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

//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Intent intent = new Intent(context, EnlargeImageActivityForCars.class);
//                        carNames.add(list.get(vHolder.getAdapterPosition()).getCarName());
//                        carPhotos.add(list.get(vHolder.getAdapterPosition()).getCarPhoto());
//                        Bundle bundle = new Bundle();
//
//                        bundle.putStringArrayList("text", carNames);
//                        bundle.putIntegerArrayList("image", carPhotos);
//                        intent.putExtras(bundle);
//                        context.startActivity(intent);
//                    }
//                }, 2000);

//                Intent intent = new Intent(context, EnlargeImageActivityForCars.class);
//                carNames.add(list.get(vHolder.getAdapterPosition()).getCarName());
//                carPhotos.add(list.get(vHolder.getAdapterPosition()).getCarPhoto());
//                Bundle bundle = new Bundle();
//
//                bundle.putStringArrayList("text", carNames);
//                bundle.putIntegerArrayList("image", carPhotos);
//                intent.putExtras(bundle);
//                context.startActivity(intent);

            }
        });
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.carNameTextView.setText(list.get(position).getCarName());
        holder.carPhotoImageView.setImageResource(list.get(position).getCarPhoto());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linearLayout;
        private TextView carNameTextView;
        private ImageView carPhotoImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.Layout_carFragment);
            carNameTextView = itemView.findViewById(R.id.TextView_carFragment);
            carPhotoImageView = itemView.findViewById(R.id.image_carFragment);
        }
    }

}
