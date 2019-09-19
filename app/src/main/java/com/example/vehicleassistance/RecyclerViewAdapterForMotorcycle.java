package com.example.vehicleassistance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterForMotorcycle extends RecyclerView.Adapter<RecyclerViewAdapterForMotorcycle.MyViewHolderForMotorcycles> {

    private Context context;
    private List<Motorcycles> list;
    private ArrayList<Integer> motorcyclePhotos=new ArrayList<>();
    private ArrayList<String> motorcycleName=new ArrayList<>();

    public RecyclerViewAdapterForMotorcycle(Context context, List<Motorcycles> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolderForMotorcycles onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_motorcyle_fragment, parent, false);
        final MyViewHolderForMotorcycles vHolder = new MyViewHolderForMotorcycles(v);
        vHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EnlargeImageActivityForMotorcycles.class);
                motorcycleName.add(list.get(vHolder.getAdapterPosition()).getMotorcycleName());
                motorcyclePhotos.add(list.get(vHolder.getAdapterPosition()).getMotorcyclePhoto());
                Bundle bundle = new Bundle();

                bundle.putStringArrayList("text", motorcycleName);
                bundle.putIntegerArrayList("image", motorcyclePhotos);
                intent.putExtras(bundle);
                context.startActivity(intent);
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
