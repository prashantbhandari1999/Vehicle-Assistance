package com.example.vehicleassistance;

public class Notifications {
    String Date;
    String Message;
    String Type;
    String Remaining;

    public String getRemaining() {
        return Remaining;
    }

    public void setRemaining(String remaining) {
        Remaining = remaining;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getDate() {

        return Date;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setDate(String date) {
        Date = date;
    }
    public Notifications(){

    }
}
