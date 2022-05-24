package com.mb.lab.banks.utils.location;

public class Location {

    public static final Location LOCATION_ZERO = new Location(0, 0);

    private double latitude;
    private double longitude;

    public Location() {
        this(-1, -1);
    }

    public Location(double[] location) {
        this(location[1], location[0]);
    }

    public Location(double latitude, double longitude) {
        super();

        this.latitude = latitude;
        this.longitude = longitude;
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

    @Override
    public int hashCode() {
        return Double.hashCode(this.longitude * 1000 + this.latitude);
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof Location) {
            Location location = (Location) o;
            return this.hashCode() == location.hashCode();
        }

        return false;
    }

    public static final Location createLocation(Double latitude, Double longitude) {
        if (latitude != null && longitude != null) {
            return new Location(latitude, longitude);
        }
        
        return null;
    }
    
}
