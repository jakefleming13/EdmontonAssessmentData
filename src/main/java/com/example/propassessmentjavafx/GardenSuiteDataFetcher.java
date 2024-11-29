package com.example.propassessmentjavafx;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class GardenSuiteDataFetcher {

    /**
     * method that uses an api call to fetch all Edmonton garden suite data
     * @return list of GardenSuiteAssessment objects
     */
    public List<GardenSuiteAssessment> fetchGardenSuiteData() {
        String apiUrl = "https://data.edmonton.ca/resource/wkbv-nqd4.json";
        List<GardenSuiteAssessment> assessments = new ArrayList<>();

        try {
            //create the connection
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            //check response code
            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
            }

            //Read response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            reader.close();

            // Parse the JSON array
            JSONArray jsonArray = new JSONArray(jsonBuilder.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                //Extract the data
                String address = jsonObject.optString("address", null);
                int constructionValue = jsonObject.optInt("construction_value", 0);
                int floorArea = jsonObject.optInt("floor_area", 0);
                int unitsAdded = jsonObject.optInt("units_added", 0);

                if (address != null && constructionValue > 30000) {
                    //Reformat address to remove 'A' and 'G' from house numbers
                    String[] addressParts = address.split(" - ");
                    String houseNumberStr = addressParts[0].replaceAll("[^0-9]", ""); // Remove non-digits
                    String street = addressParts[1];

                    int houseNumber = houseNumberStr.isEmpty() ? 0 : Integer.parseInt(houseNumberStr);

                    //Create and add the assessment object
                    GardenSuiteAssessment assessment = new GardenSuiteAssessment(new Address(String.valueOf(houseNumber), street),constructionValue, floorArea, unitsAdded);
                    assessments.add(assessment);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return assessments;
    }
}

