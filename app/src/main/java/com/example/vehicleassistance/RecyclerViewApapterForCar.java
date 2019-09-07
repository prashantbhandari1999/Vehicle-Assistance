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

public class RecyclerViewApapterForCar extends RecyclerView.Adapter<RecyclerViewApapterForCar.MyViewHolder> {

    private Context context ;
    private List<Cars> list;

    public RecyclerViewApapterForCar(Context context, List<Cars> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v= LayoutInflater.from(context).inflate(R.layout.items_car_fragment,parent,false);
        MyViewHolder vHolder=new MyViewHolder(v);

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

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView carNameTextView;
        private ImageView carPhotoImageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            carNameTextView=itemView.findViewById(R.id.TextView_carFragment);
            carPhotoImageView=itemView.findViewById(R.id.image_carFragment);
        }
    }

}
