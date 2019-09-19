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

public class RecyclerViewApapterForCar extends RecyclerView.Adapter<RecyclerViewApapterForCar.MyViewHolder> {

    private Context context;
    private List<Cars> list;
    final ArrayList<String> carNames = new ArrayList<>();
    final ArrayList<Integer> carPhotos = new ArrayList<>();


    public RecyclerViewApapterForCar(Context context, List<Cars> list) {
        this.context = context;
        this.list = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;

        v = LayoutInflater.from(context).inflate(R.layout.items_car_fragment, parent, false);
        final MyViewHolder vHolder = new MyViewHolder(v);
        vHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EnlargeImageActivityForCars.class);
                carNames.add(list.get(vHolder.getAdapterPosition()).getCarName());
                carPhotos.add(list.get(vHolder.getAdapterPosition()).getCarPhoto());
                Bundle bundle = new Bundle();

                bundle.putStringArrayList("text", carNames);
                bundle.putIntegerArrayList("image", carPhotos);
                intent.putExtras(bundle);
                context.startActivity(intent);

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
