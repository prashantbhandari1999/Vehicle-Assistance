package com.example.vehicleassistance;

public class Cars {
    private String carName;
    private int carPhoto;

    public Cars() {
    }

    public Cars(String carName, int carPhoto) {
        this.carName = carName;
        this.carPhoto = carPhoto;
    }

    public String getCarName() {
        return carName;
    }

    public int getCarPhoto() {
        return carPhoto;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public void setCarPhoto(int carPhoto) {
        this.carPhoto = carPhoto;
    }
}
