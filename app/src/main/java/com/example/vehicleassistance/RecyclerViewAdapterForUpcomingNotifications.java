package com.example.vehicleassistance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecyclerViewAdapterForUpcomingNotifications extends RecyclerView.Adapter<RecyclerViewAdapterForUpcomingNotifications.myViewHolder>{

    private View view;
    private Context context;
    private List<upcoming_notifications> list;

    public RecyclerViewAdapterForUpcomingNotifications(Context context, List<upcoming_notifications> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(context).inflate(R.layout.items_upcoming_notifications,parent,false);
        myViewHolder vHolder=new myViewHolder(view);
        return vHolder  ;
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder holder, int position) {
        holder.title.setText(list.get(position).getHeader());
        holder.subtitle.setText(list.get(position).getSubtitle());
        holder.date.setText(list.get(position).getDate());
        holder.imageView.setImageResource(list.get(position).getImage());
        DateFormat df=new SimpleDateFormat("dd/MM/yyyy");
        Date date=new Date();
        if(!list.get(position).getDate().equals(df.format(date))){
            holder.checkbox.setVisibility(View.VISIBLE);
        }
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.checkbox.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class myViewHolder extends RecyclerView.ViewHolder{
        private TextView title,subtitle,date;
        private ImageView imageView,checkbox;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.header_1_upcoming_notification);
            subtitle=itemView.findViewById(R.id.header_2_upcoming_notifications);
            date=itemView.findViewById(R.id.date_upcoming_notifications);
            imageView=itemView.findViewById(R.id.ImageView_upcoming_notifications);
            checkbox=itemView.findViewById(R.id.imageview_checkbox);
        }
    }
}
