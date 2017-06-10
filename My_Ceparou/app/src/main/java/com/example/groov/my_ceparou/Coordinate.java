package com.example.groov.my_ceparou;

/**
 * Created by Joris on 08/06/2017.
 */

public class Coordinate {
    public double latitude;
    public double longitude;

    public Coordinate(double x, double y) {
        this.latitude = x;
        this.longitude = y;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double x) {
        latitude = x;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double y) {
        longitude = y;
    }

}
