package com.example.vehicleassistance;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RecyclerViewAdapterForUpcomingNotifications extends RecyclerView.Adapter<RecyclerViewAdapterForUpcomingNotifications.myViewHolder> {

    private View view;
    private Context context;
    private List<upcoming_notifications> list;
    FirebaseFirestore db;
    FirebaseAuth lAuth;

    public RecyclerViewAdapterForUpcomingNotifications(Context context, List<upcoming_notifications> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.items_upcoming_notifications, parent, false);
        myViewHolder vHolder = new myViewHolder(view);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder holder, final int position) {
        holder.title.setText(list.get(position).getHeader());
        holder.subtitle.setText(list.get(position).getSubtitle());
        holder.date.setText(list.get(position).getDate());
        holder.imageView.setImageResource(list.get(position).getImage());

        Date date1=null, date2=null;
        String date=list.get(position).getRemaining();
        Log.d("RDate",date);

        String type=list.get(position).getSubtitle();
        String message="";
        switch (type){
            case "PUC":
                message="Have you renewed your Vehicle's PUC ?";
                break;
            case "INSURANCE":
                message="Have you renewed your Vehicle's INSURANCE ?";
                break;
            case "AIR":
                message="Have you check checked your Vehicle Tyre's Air Pressure ?";
                break;
            case "OIL":
                message="Have you checked your Vehicle's Oil quality ?";

        }
        try {
            date1 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
            date2 = new Date();
            long diff = date1.getTime() - date2.getTime();
            long day = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            Log.d("RDiff", "" + diff);
            Log.d("RDays", "" + day);

            int days = (int) day;
            diff /= 1000;
            int month = 0;
            int i = date1.getMonth();
            Date today = new SimpleDateFormat("dd/MM/yyyy").parse(date2.getDate() + "/" + (date2.getMonth() + 1) + "/" + (date2.getYear() + 1900));
            Date tomorrow = new SimpleDateFormat("dd/MM/yyyy").parse((date2.getDate() + 1) + "/" + (date2.getMonth() + 1) + "/" + (date2.getYear() + 1900));
            Date yesterday = new SimpleDateFormat("dd/MM/yyyy").parse((date2.getDate() - 1) + "/" + (date2.getMonth() + 1) + "/" + (date2.getYear() + 1900));
            long todayDifference = (date2.getTime() - today.getTime()) / 1000;
            long tomorrowDifference = (tomorrow.getTime() - date2.getTime()) / 1000;
            long yesterdayDifference = (date2.getTime() - yesterday.getTime()) / 1000;

            Log.d("tomorrow", "Date: " + date1 + "diff: " + diff + "\ttoday: " + todayDifference + "\ttomorrow: " + tomorrowDifference);
            if ((diff < tomorrowDifference && diff > 0) || (diff < 0 && diff >= -todayDifference))
                holder.checkbox.setVisibility(View.VISIBLE);
            else if (diff <= 0) {
                holder.checkbox.setVisibility(View.VISIBLE);
            }
            else
                holder.checkbox.setVisibility(View.GONE);

        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db=FirebaseFirestore.getInstance();
                lAuth=FirebaseAuth.getInstance();
//                holder.checkbox.setVisibility(View.GONE);
                final String type=list.get(position).getHeader();
                final String deleteMessage=list.get(position).getSubtitle();
                String message="";
                switch (type){
                    case "PUC":
                        message="Have you renewed your Vehicle's PUC ?";
                        break;
                    case "INSURANCE":
                        message="Have you renewed your Vehicle's INSURANCE ?";
                        break;
                    case "AIR":
                        message="Have you check checked your Vehicle Tyre's Air Pressure ?";
                        break;
                    case "OIL":
                        message="Have you checked your Vehicle's Oil quality ?";

                }
                new AlertDialog.Builder(context)
                        .setTitle(type)
                        .setMessage(message)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Map<String, Object> notification = new HashMap<>();
                                String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                                String[] split_date1 = date.split("-");
                                String new_date1="";            //22/2/2000
                                for (int i=0;i<3;i++) {
                                    new_date1 += split_date1[i];
                                    if (i!=2)
                                        new_date1+="/";
                                };
                                final String new_date=new_date1;
                                String message = "";
                                switch (type) {
                                    case "PUC":
                                        message = "You have renewed your PUC on " + new_date;
                                        break;
                                    case "OIL":
                                        message = "You have verified your vehicle oil quality on " + new_date;
                                        break;
                                    case "INSURANCE":
                                        message = "You have renewed your INSURANCE on" + new_date;
                                        break;
                                    case "AIR":
                                        message = "You have verified your tyre's air pressure on " + new_date;
                                        break;
                                }
                                notification.put("Type", type);
                                notification.put("Message", message);
                                notification.put("Date", date);
                                notification.put("Remaining", date);
                                db.collection("Users")
                                        .document(lAuth.getCurrentUser().getUid())
                                        .collection("Notifications")
                                        .document("Previous Notifications")
                                        .collection("C")
                                        .add(notification)
                                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                Toast.makeText(context, "New Reminder has been set", Toast.LENGTH_SHORT).show();
                                                setReminder(type,new_date);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                            }
                                        });

                                db.collection("Users")
                                        .document(lAuth.getCurrentUser().getUid())
                                        .collection("Notifications")
                                        .document("Upcoming Notifications")
                                        .collection("C")
                                        .whereEqualTo("Message",deleteMessage)
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                WriteBatch batch=FirebaseFirestore.getInstance().batch();
                                                List<DocumentSnapshot> snapshotList=queryDocumentSnapshots.getDocuments();
                                                for (DocumentSnapshot snapshot:snapshotList){
                                                    batch.delete(snapshot.getReference());
                                                }

                                                batch.commit()
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.d("Deletion","Deleted all");
                                                            }
                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });



                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }

    private void setReminder(String type,String date) {
        Log.d("NR",type+" "+date);
        String message="";
        Calendar c=Calendar.getInstance();
        String[] split_date1 = date.split("/");    //22/2/2000
        int[] date_arr= new int[3];
        for (int k = 0;k < 3; k++)
            Log.d("DT",split_date1[k]);
        for (int k = 0;k < 3; k++)
            date_arr[k] = Integer.parseInt(split_date1[k]);

        c.set(Calendar.YEAR,date_arr[2]);
        c.set(Calendar.MONTH,date_arr[1]);
        c.set(Calendar.DAY_OF_MONTH,date_arr[0]);

        switch (type) {
            case "PUC":
                message = "Your PUC renewal date is on ";
                c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 6);
                c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + 1);
                break;
            case "OIL":
                message = "Your OIL checking date is on ";
                c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 3);
                c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + 2);
                break;
            case "INSURANCE":
                message = "Your INSURANCE renewal date is on ";
                c.set(Calendar.YEAR, c.get(Calendar.YEAR) + 1);
                c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + 3);
                break;
            case "AIR":
                message = "Your AIR checking date is on ";
                c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 15);
                c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + 4);
                break;
        }
        int day_of_month, month, year;
        String date1;
        day_of_month = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH) + 1;
        year = c.get(Calendar.YEAR);
        date1 = day_of_month + "/" + month + "/" + year;
        message+=date1;
        Log.d("NEW",message);
        Map<String, Object> notification = new HashMap<>();
        notification.put("Type", type);
        notification.put("Message", message);
        notification.put("Date", date1);
        notification.put("Remaining", date1);
        db.collection("Users")
                .document(lAuth.getCurrentUser().getUid())
                .collection("Notifications")
                .document("Upcoming Notifications")
                .collection("C")
                .add(notification)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Log.d("Y","Y");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class myViewHolder extends RecyclerView.ViewHolder {
        private TextView title, subtitle, date;
        private ImageView imageView, checkbox;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.header_1_upcoming_notification);
            subtitle = itemView.findViewById(R.id.header_2_upcoming_notifications);
            date = itemView.findViewById(R.id.date_upcoming_notifications);
            imageView = itemView.findViewById(R.id.ImageView_upcoming_notifications);
            checkbox = itemView.findViewById(R.id.imageview_checkbox);
        }
    }

}
