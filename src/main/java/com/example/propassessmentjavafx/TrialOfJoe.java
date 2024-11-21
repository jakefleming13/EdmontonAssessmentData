package com.example.propassessmentjavafx;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TrialOfJoe {
    private static List<PropertyAssessment> fetchPropertyData() {
        List<PropertyAssessment> propertyAssessments = new ArrayList<>();
        try {
            URL url = new URL("https://data.edmonton.ca/resource/q7d6-ambg.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONArray jsonArray = new JSONArray(response.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    int accountNumber = jsonObject.getInt("account_number");
                    String houseNumber = jsonObject.optString("house_number", "");
                    String streetName = jsonObject.optString("street_name", "");
                    String garage = jsonObject.optString("garage", "");
                    String neighbourhood = jsonObject.optString("neighbourhood", "");
                    int assessedValue = jsonObject.optInt("assessed_value", 0);

                    PropertyAssessment propertyAssessment = new PropertyAssessment(
                            accountNumber,
                            new Address(houseNumber, streetName),
                            assessedValue,
                            null,  // Assessment Classes
                            new Neighbourhood(neighbourhood, ""),
                            null,   // Location
                            garage
                    );

                    propertyAssessments.add(propertyAssessment);
                }
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return propertyAssessments;
    }


    private static List<PropertyAssessment> fetchGardenPropertyData() {
        List<PropertyAssessment> propertyAssessments = new ArrayList<>();
        try {
            URL url = new URL("https://data.edmonton.ca/resource/wkbv-nqd4.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONArray jsonArray = new JSONArray(response.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    //int accountNumber = jsonObject.getInt("row_id");
                    String address = jsonObject.optString("address", "");
                    // Check if a comma exists
                    int commaIndex = address.indexOf(',');
                    // If a comma exists, remove everything before it and the comma itself
                    address = (commaIndex != -1) ? address.substring(commaIndex + 1) : address;

                    // Separate house number and street name
                    int houseNumberIndex = address.indexOf(' ');
                    String houseNumber = (houseNumberIndex != 1) ? address.substring(0, houseNumberIndex) : address;
                    // Remove any remaining alpha characters
                    houseNumber = houseNumber.replaceAll("[a-zA-Z]", "");

                    int dashIndex = address.indexOf("-");
                    String streetName = (dashIndex != 1) ? address.substring(dashIndex+2) : address;
                    //System.out.println(streetName);

                    String neighbourhood = jsonObject.optString("neighbourhood", "");
                    int assessedValue = jsonObject.optInt("construction_value", 0);

                    PropertyAssessment propertyAssessment = new PropertyAssessment(
                            0,
                            new Address(houseNumber, streetName),
                            assessedValue,
                            null,  // Assessment Classes
                            new Neighbourhood(neighbourhood, ""),
                            null,   // Location
                            null
                    );

                    propertyAssessments.add(propertyAssessment);
                }
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return propertyAssessments;
    }
    public static void main(String[] args){
        List<PropertyAssessment> gardenData = fetchGardenPropertyData();
        List<PropertyAssessment> propertyData = fetchPropertyData();
        System.out.println("Size of garden: " + gardenData.size() + "\n" + "Size of property: " + propertyData.size());
        int equalCount = 0;
        for (int i = 0; i < propertyData.size(); i++){
            for(int j = 0; j < gardenData.size(); j++) {
                //System.out.println(propertyData.get(i).getAddress() + "  " + gardenData.get(j).getAddress());
                if (propertyData.get(i).getAddress().equals(gardenData.get(j).getAddress())) {
                    equalCount += 1;
                    System.out.println(propertyData.get(i).getAddress() + "  " + gardenData.get(j).getAddress());
                }
            }
        }
        System.out.println("Total count: " + equalCount);
    }
}
