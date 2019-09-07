package com.example.vehicleassistance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapterForMotorcycle extends RecyclerView.Adapter<RecyclerViewAdapterForMotorcycle.MyViewHolderForMotorcycles> {

    private Context context ;
    private List<Motorcycles> list;

    public RecyclerViewAdapterForMotorcycle(Context context, List<Motorcycles> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolderForMotorcycles onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v= LayoutInflater.from(context).inflate(R.layout.item_motorcyle_fragment,parent,false);
        MyViewHolderForMotorcycles vHolder=new MyViewHolderForMotorcycles(v);

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


    public static class MyViewHolderForMotorcycles extends RecyclerView.ViewHolder{
        private TextView motorcycleNameTextView;
        private ImageView motorcyclePhotoImageView;
        public MyViewHolderForMotorcycles(@NonNull View itemView) {
            super(itemView);

            motorcycleNameTextView=itemView.findViewById(R.id.TextView_motorcycleFragment);
            motorcyclePhotoImageView=itemView.findViewById(R.id.image_motorcycleFragment);
        }
    }

}
