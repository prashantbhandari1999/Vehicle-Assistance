package com.example.vehicleassistance;

public class Motorcycles {
    String motorcycleName;
    int motorcyclePhoto;

    public Motorcycles() {
    }

    public Motorcycles(String motorcycleName, int motorcyclePhoto) {
        this.motorcycleName = motorcycleName;
        this.motorcyclePhoto = motorcyclePhoto;
    }

    public String getMotorcycleName() {
        return motorcycleName;
    }

    public int getMotorcyclePhoto() {
        return motorcyclePhoto;
    }

    public void setMotorcycleName(String motorcycleName) {
        this.motorcycleName = motorcycleName;
    }

    public void setMotorcyclePhoto(int motorcyclePhoto) {
        this.motorcyclePhoto = motorcyclePhoto;
    }
}
