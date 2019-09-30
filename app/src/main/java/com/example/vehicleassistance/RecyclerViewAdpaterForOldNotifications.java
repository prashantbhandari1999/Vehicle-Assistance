package com.example.vehicleassistance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdpaterForOldNotifications extends RecyclerView.Adapter<RecyclerViewAdpaterForOldNotifications.myViewHolder> {

    private View view;
    private Context context;
    private List<old_notifications> list;

    public RecyclerViewAdpaterForOldNotifications(Context context, List<old_notifications> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(context).inflate(R.layout.item_old_notifications,parent,false);
        myViewHolder vHolder=new myViewHolder(view);
        return vHolder  ;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.title.setText(list.get(position).getHeader());
        holder.subtitle.setText(list.get(position).getSubtitle());
        holder.date.setText(list.get(position).getDate());
        holder.imageView.setImageResource(list.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class myViewHolder extends RecyclerView.ViewHolder{
        private TextView title,subtitle,date;
        private ImageView imageView;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.header_1_old_notification);
            subtitle=itemView.findViewById(R.id.header_2_old_notifications);
            date=itemView.findViewById(R.id.date_old_notifications);
            imageView=itemView.findViewById(R.id.ImageView_old_notifications);
        }
    }
}
