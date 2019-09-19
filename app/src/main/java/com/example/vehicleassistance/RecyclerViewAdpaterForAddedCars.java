package com.example.vehicleassistance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdpaterForAddedCars extends RecyclerView.Adapter<RecyclerViewAdpaterForAddedCars.ViewHolder> {

    private ArrayList<String>carNames;
    private ArrayList<Integer>carPhotos;
    private Context mContext;

    public RecyclerViewAdpaterForAddedCars(Context mContext, ArrayList<String> carNames, ArrayList<Integer> carPhotos ) {
        this.carNames = carNames;
        this.carPhotos = carPhotos;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.added_car_list,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.text.setText(carNames.get(position));
        holder.image.setImageResource(carPhotos.get(position));
    }

    @Override
    public int getItemCount() {
        return carNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.added_car_image);
            text=itemView.findViewById(R.id.added_car_text);

        }
    }
}
