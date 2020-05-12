package com.cs.mapstutorial;

public class LocationHelper {

    private double currentLat;
    private double currentLng;
    private double destinationLat;
    private double destinationLng;


    public LocationHelper(double currentLat, double currentLng, double destinationLat, double destinationLng) {
        this.currentLat = currentLat;
        this.currentLng = currentLng;
        this.destinationLat = destinationLat;
        this.destinationLng = destinationLng;
    }

    public double getCurrentLat() {
        return currentLat;
    }

    public void setCurrentLat(double currentLat) {
        this.currentLat = currentLat;
    }

    public double getCurrentLng() {
        return currentLng;
    }

    public void setCurrentLng(double currentLng) {
        this.currentLng = currentLng;
    }

    public double getDestinationLat() {
        return destinationLat;
    }

    public void setDestinationLat(double destinationLat) {
        this.destinationLat = destinationLat;
    }

    public double getDestinationLng() {
        return destinationLng;
    }

    public void setDestinationLng(double destinationLng) {
        this.destinationLng = destinationLng;
    }
}
