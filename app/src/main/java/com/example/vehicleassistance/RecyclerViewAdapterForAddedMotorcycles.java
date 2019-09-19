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

public class RecyclerViewAdapterForAddedMotorcycles extends RecyclerView.Adapter<RecyclerViewAdapterForAddedMotorcycles.ViewHolder> {
    private ArrayList<String> motorcycleName;
    private ArrayList<Integer>motorcyclePhotos;
    private Context mContext;

    public RecyclerViewAdapterForAddedMotorcycles(ArrayList<String> motorcycleName, ArrayList<Integer> motorcyclePhotos, Context mContext) {
        this.motorcycleName = motorcycleName;
        this.motorcyclePhotos = motorcyclePhotos;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.added_motorcyle_list,parent,false);
        return new ViewHolder(v);    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.text.setText(motorcycleName.get(position));
        holder.image.setImageResource(motorcyclePhotos.get(position));
    }

    @Override
    public int getItemCount() {
        return motorcycleName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.added_motorcycle_image);
            text=itemView.findViewById(R.id.added_motorcycle_text);

        }
    }
}
