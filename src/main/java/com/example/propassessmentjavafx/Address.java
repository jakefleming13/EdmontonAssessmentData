package com.example.propassessmentjavafx;

import java.util.Objects;

public class Address {
    private String houseNumber;
    private String streetName;

    /**
     * Constructor for the Address support class
     * @param houseNumber as type String
     * @param streetName as type String
     */
    public Address(String houseNumber, String streetName) {
        this.houseNumber = houseNumber;
        this.streetName = streetName;
    }

    /**
     * Method that overrides the toString method
     * @return String
     */
    @Override
    public String toString() {
        return houseNumber + " " + streetName;
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
        Address address = (Address) o;
        return Objects.equals(houseNumber, address.houseNumber) && Objects.equals(streetName, address.streetName);
    }

    /**
     * Method that produces a hash of the address object
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(houseNumber, streetName);
    }
}
