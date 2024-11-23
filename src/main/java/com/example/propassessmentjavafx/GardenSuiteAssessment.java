package com.example.propassessmentjavafx;

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
}

