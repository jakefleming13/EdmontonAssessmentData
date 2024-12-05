package com.example.propassessmentjavafx;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;

import java.util.Objects;

public class GardenSuiteAssessment {
    private Address address;
    private int constructionValue;
    private int floorArea;
    private int unitsAdded;

    public GardenSuiteAssessment(Address address, int constructionValue, int floorArea, int unitsAdded) {
        this.address = address;
        this.constructionValue = constructionValue;
        this.floorArea = floorArea;
        this.unitsAdded = unitsAdded;
    }

    /**
     * getter to return the construction value
     * @return int
     */
    public int getConstructionValue() {
        return constructionValue;
    }

    /**
     * getter to return the square footage
     * @return int
     */
    public int getFloorArea() {
        return floorArea;
    }

    /**
     * getter to return the units added
     * @return int
     */
    public int getUnitsAdded() {
        return unitsAdded;
    }

    /**
     * getter to return the address
     * @return Address object
     */
    public Address getAddress() {
        return address;
    }

    /**
     * to string method that allows you to print in a single line
     * @return formatted String
     */
    @Override
    public String toString() {
        return "GardenSuiteAssessment(" +
                ", address=" + address +
                ", constructionValue=" + constructionValue +
                ", floorArea=" + floorArea +
                ", unitsAdded=" + unitsAdded +
                ')';
    }

    /**
     * method that overrides the equals method
     * @param o which is a GardenSuiteAssessment object
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GardenSuiteAssessment that = (GardenSuiteAssessment) o;
        return constructionValue == that.constructionValue && floorArea == that.floorArea && unitsAdded == that.unitsAdded && Objects.equals(address, that.address);
    }

    /**
     * method that Overrides the hashcode method
     * @return hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(address, constructionValue, floorArea, unitsAdded);
    }
}

