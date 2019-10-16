package com.example.vehicleassistance;

public class upcoming_notifications {
    private String header;
    private String subtitle;
    private String date;
    private int image;

    public void setImage(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public upcoming_notifications(String header, String subtitle, String date , int image) {
        this.header = header;
        this.subtitle = subtitle;
        this.date = date;
        this.image=image;
    }
    public upcoming_notifications(){

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
