package com.example.propassessmentjavafx;

import java.util.Objects;

public class Location {
    private double latitude;
    private double longitude;

    /**
     * Constructor for the Location support class
     * @param latitude of type double
     * @param longitude of type double
     */
    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Method that overrides the toString class
     * @return String
     */
    @Override
    public String toString() {
        return "(" + latitude + ", " + longitude + ")";
    }

    /**
     *
     * @param o Object that is being compared
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Double.compare(latitude, location.latitude) == 0 && Double.compare(longitude, location.longitude) == 0;
    }

    /**
     * Method that produces a hash of the address object
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
}
