package com.example.smartparking;

public class LocationSlot {

        private String address;
        private String name;
        private double latitude;
        private double longitude;

        // Constructor, getters, and setters
    public LocationSlot(){}

    public LocationSlot(String address, String name, double latitude, double longitude) {
        this.address = address;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setId(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
