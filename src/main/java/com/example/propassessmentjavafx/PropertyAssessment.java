package com.example.propassessmentjavafx;

import java.util.List;
import java.util.Objects;

public class PropertyAssessment implements Comparable<PropertyAssessment> {
    private int accountNumber;
    private Address address;
    private int assessedValue;
    private List<String> assessmentClasses;
    private Neighbourhood neighbourhood;
    private Location location;
    private String garage;

    /**
     * PropertyAssessment constructor
     * @param accountNumber of type int
     * @param address of type Address
     * @param assessedValue of type int
     * @param assessmentClasses of type String
     * @param neighbourhood of type Neighbourhood
     * @param location of type Location
     */
    public PropertyAssessment(int accountNumber, Address address, int assessedValue,
                              List<String> assessmentClasses, Neighbourhood neighbourhood, Location location, String garage) {
        this.accountNumber = accountNumber;
        this.address = address;
        this.assessedValue = assessedValue;
        this.assessmentClasses = assessmentClasses;
        this.neighbourhood = neighbourhood;
        this.location = location;
        this.garage = garage;
    }

    /**
     * getter to return the account number
     * @return int
     */
    public int getAccountNumber() {
        return accountNumber;
    }

    /**
     * getter to return the garage
     * @return String
     */
    public String getGarage() {
        return garage;
    }

    /**
     * getter to return the address
     * @return Address object
     */
    public Address getAddress() {
        return address;
    }

    /**
     * getter to return the assessed value
     * @return int
     */
    public int getAssessedValue() {
        return assessedValue;
    }

    /**
     * getter to return the assessment classes
     * @return List of Strings
     */
    public List<String> getAssessmentClasses() {
        return assessmentClasses;
    }

    /**
     * getter to return the neighbourhood
     * @return Neighbourhood object
     */
    public Neighbourhood getNeighbourhood() {
        return neighbourhood;
    }

    /**
     * getter to return the location
     * @return location object
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Method that overrides the compareTo method
     * @param other the object to be compared.
     * @return int
     */
    @Override
    public int compareTo(PropertyAssessment other) {
        return Integer.compare(this.assessedValue, other.assessedValue);
    }

    /**
     * This method overrides the toString() method
     * @return String that formats the data
     */
    @Override
    public String toString() {
        return "Account number = " + accountNumber +
                "\nAddress = " + address +
                "\nAssessed value = $" + assessedValue +
                "\nAssessment class = " + assessmentClasses +
                "\nNeighbourhood = " + neighbourhood +
                "\nLocation = " + location +
                "\nGarage = " + garage;
    }

    /**
     * Method that allows for comparison of PropertyAssessment objects
     * @param o Object of class PropertyAssessment
     * @return boolean value
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyAssessment that = (PropertyAssessment) o;
        return accountNumber == that.accountNumber && assessedValue == that.assessedValue && Objects.equals(address, that.address) && Objects.equals(assessmentClasses, that.assessmentClasses) && Objects.equals(neighbourhood, that.neighbourhood) && Objects.equals(location, that.location);
    }

    /**
     * Method that overrides the hashCode method
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, address, assessedValue, assessmentClasses, neighbourhood, location);
    }
}

