package com.example.vehicleassistance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;


public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle=intent.getExtras();
        String alarm_name=bundle.getString("Alarm Name");
        String alarm_message=bundle.getString("Message");
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(alarm_name,alarm_message);


        notificationHelper.getManager().notify(1, nb.build());
    }
}