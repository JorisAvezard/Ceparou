package metier.entities;

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
