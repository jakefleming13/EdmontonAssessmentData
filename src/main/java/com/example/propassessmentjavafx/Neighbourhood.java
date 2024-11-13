package com.example.propassessmentjavafx;

import java.util.Objects;

public class Neighbourhood {
    private String area;
    private String ward;

    /**
     * Constructor for the Neighbourhood support class
     * @param area of type String
     * @param ward of type String
     */
    public Neighbourhood(String area, String ward) {
        this.area = area;
        this.ward = ward;
    }

    /**
     * Method that overrides the toString method
     * @return String
     */
    @Override
    public String toString() {
        return area;
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
        Neighbourhood that = (Neighbourhood) o;
        return Objects.equals(area, that.area) && Objects.equals(ward, that.ward);
    }

    /**
     * Method that produces a hash of the address object
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(area, ward);
    }
}
