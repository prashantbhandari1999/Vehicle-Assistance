package com.example.vehicleassistance;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class upcoming_notifications {
    private String header;
    private String subtitle;
    private String date;
    private String remaining;
    private int image;

    public void setImage(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public String getRemaining() {
        return remaining;
    }

    public void setRemaining(String remaining) {
        this.remaining = remaining;
    }

    public upcoming_notifications(String header, String subtitle, String date, String remaining, int image) {

        int array[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        boolean flag = false;
        try {
            Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
            Date date2 = new Date();
            long diff = date1.getTime() - date2.getTime();
            long day = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            Log.d("Days", "" + day);

            int days = (int) day;
            int month = 0;
            int i = date1.getMonth();
            Log.d("JAN", "" + i);
            if (days == 0)
                date = "Today";
            else if (days == 1)
                date = "Tomorrow";
            else {
                while (true) {
                    if (array[i] <= days) {
                        days -= array[i];
                        month++;
                        flag = true;
                    } else {
                        break;
                    }
                    i++;
                    if (i > 11)
                        i = 0;
                }
                if (month != 0) {
                    date = month + " months ";
                }
                if (days != 0) {
                    if (flag)
                        date += days + " days to go";
                    else
                        date = days + " days to go";
                }
            }
        } catch (Exception e) {

        }
        this.header = header;
        this.subtitle = subtitle;
        this.date = date;
        this.image = image;
        this.remaining = remaining;
    }

    public upcoming_notifications() {

    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeader() {
        return header;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getDate() {
        return date;
    }
}
