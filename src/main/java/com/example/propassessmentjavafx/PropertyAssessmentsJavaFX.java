package com.example.propassessmentjavafx;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PropertyAssessmentsJavaFX extends Application {
    private TableView<PropertyAssessment> table = new TableView<>();

    @Override
    public void start(Stage primaryStage) {
        //Init TableView and columns
        initializeTable();

        //fetch and display data
        List<PropertyAssessment> propertyData = fetchPropertyData();
        table.getItems().addAll(propertyData);

        //ComboBoxes to display dropdowns
        ComboBox<String> comboBox1 = new ComboBox<>();
        ComboBox<String> comboBox2 = new ComboBox<>();
        ComboBox<String> comboBox3 = new ComboBox<>();
        ComboBox<String> comboBox4 = new ComboBox<>();
        comboBox1.getItems().addAll("", "<$200,000", "$200,000-$400,000", ">$400,000");
        comboBox2.getItems().addAll("", "Downtown", "Oliver", "Bearspaw");
        comboBox3.getItems().addAll("", "<$75,000", "$75,000-$100,000", ">$100,000");
        comboBox4.getItems().addAll("");

        //placeholders
        comboBox1.setPromptText("Select an option");
        comboBox2.setPromptText("Select an option");
        comboBox3.setPromptText("Select an option");
        comboBox4.setPromptText("Select an option");

        //Labels
        Label label1 = new Label("Property Value:");
        Label label2 = new Label("Neighbourhood:");
        Label label3 = new Label("Construction Value:");
        Label label4 = new Label("Garden Suite sq ft:");

        //layout for title and combobox
        VBox vBox1 = new VBox(5, label1, comboBox1);
        VBox vBox2 = new VBox(5, label2, comboBox2);
        VBox vBox3 = new VBox(5, label3, comboBox3);
        VBox vBox4 = new VBox(5, label4, comboBox4);

        //HBox to display the groupings vertically together
        HBox comboBoxLayout = new HBox(20, vBox1, vBox2, vBox3, vBox4);
        comboBoxLayout.setAlignment(Pos.CENTER);

        //overall Layout
        VBox layout = new VBox(10, comboBoxLayout, table);


        comboBox1.setOnAction(event -> filterData(comboBox1, comboBox2, comboBox3, comboBox4));
        comboBox2.setOnAction(event -> filterData(comboBox1, comboBox2, comboBox3, comboBox4));
        comboBox3.setOnAction(event -> filterData(comboBox1, comboBox2, comboBox3, comboBox4));
        comboBox4.setOnAction(event -> filterData(comboBox1, comboBox2, comboBox3, comboBox4));

        //set scene
        Scene scene = new Scene(layout, 700, 500);
        primaryStage.setTitle("Edmonton Garden Suite Data");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeTable() {
        //define columns with cell value factories
        TableColumn<PropertyAssessment, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<PropertyAssessment, String> neighbourhoodCol = new TableColumn<>("Neighbourhood");
        neighbourhoodCol.setCellValueFactory(new PropertyValueFactory<>("neighbourhood"));

        TableColumn<PropertyAssessment, Integer> assessedValueCol = new TableColumn<>("Property Value");
        assessedValueCol.setCellValueFactory(new PropertyValueFactory<>("assessedValue"));

        TableColumn<PropertyAssessment, String> constructionValueCol = new TableColumn<>("Construction Value");
        constructionValueCol.setCellValueFactory(new PropertyValueFactory<>("constructionValue"));

        TableColumn<PropertyAssessment, String> floorAreaCol = new TableColumn<>("Garden Suite sq ft");
        floorAreaCol.setCellValueFactory(new PropertyValueFactory<>("floorArea"));

        TableColumn<PropertyAssessment, String> unitsAddedCol = new TableColumn<>("Units Added");
        unitsAddedCol.setCellValueFactory(new PropertyValueFactory<>("unitsAdded"));

        TableColumn<PropertyAssessment, String> gradeCol = new TableColumn<>("Garden Suite Grade");
        gradeCol.setCellValueFactory(new PropertyValueFactory<>("gardenSuiteGrade"));

        table.getColumns().addAll(addressCol, neighbourhoodCol, assessedValueCol, constructionValueCol, floorAreaCol, unitsAddedCol, gradeCol);
    }

    private List<PropertyAssessment> fetchPropertyData() {
        List<PropertyAssessment> propertyAssessments = new ArrayList<>();
        int limit = 1000;  //Maximum number of records per request
        int offset = 0;    //Starting point for fetching data
        int count = 0;

        try {
            while (true) {
                URL url = new URL("https://data.edmonton.ca/resource/q7d6-ambg.json?$limit=" + limit + "&$offset=" + offset);
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
                        if(houseNumber.isEmpty() && streetName.isEmpty()) {
                            continue;
                        }
                        String garage = jsonObject.optString("garage", "");
                        String neighbourhood = jsonObject.optString("neighbourhood", "");
                        int assessedValue = jsonObject.optInt("assessed_value", 0);

//                  Will need to list every neighbourhood and assign values:
//                        Map<String, Double> neighborhoodAdjustments = Map.of(
//                                "GREISBACH", 0.2,
//                                "DOWNTOWN", 0.2,
//                                "OLIVER", -0.2
//                        );

//                  min/max are from the max/min construction values
//                  and max/min property values:
//                        double minConstructionEfficiency = -80;
//                        double maxConstructionEfficiency = -20;
//                        double minPropertyValueEfficiency = 0.002;
//                        double maxPropertyValueEfficiency = 0.01;

//                  Call the calculate grade method to receive a grade:
//                        double grade = calculateGrade(neighborhood, constructionValue, propertyValue, floorArea, unitsAdded,
//                                minConstructionEfficiency, maxConstructionEfficiency,
//                                minPropertyValueEfficiency, maxPropertyValueEfficiency,
//                                neighborhoodAdjustments);

                        PropertyAssessment propertyAssessment = new PropertyAssessment(
                                accountNumber,
                                new Address(houseNumber, streetName),
                                assessedValue,
                                null,  // Set to null
                                new Neighbourhood(neighbourhood, ""),
                                null,   // set to null
                                garage,
                                0,
                                0,
                                0,
                                0
                        );

                        propertyAssessments.add(propertyAssessment);
                    }

                    //increase the offset for the next set of 1000
                    offset += limit;
                    count += 1000;
                    if(count >= 1200){
                        break;
                    }
                }
                conn.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return propertyAssessments;
    }

    private void filterData(ComboBox<String> comboBox1, ComboBox<String> comboBox2, ComboBox<String> comboBox3, ComboBox<String> comboBox4) {
        List<PropertyAssessment> filteredData = new ArrayList<>();

        for (PropertyAssessment property : fetchPropertyData()) {
            boolean matches = true;

            //filter by Assessed Value
            String valueRange = comboBox1.getValue();
            int assessedValue = property.getAssessedValue();
            if (valueRange != null && !valueRange.isEmpty()) {
                switch (valueRange) {
                    case "<$200,000":
                        if (assessedValue >= 200000) matches = false;
                        break;
                    case "$200,000-$400,000":
                        if (assessedValue < 200000 || assessedValue > 400000) matches = false;
                        break;
                    case ">$400,000":
                        if (assessedValue <= 400000) matches = false;
                        break;
                }
            }

            //filter by Neighbourhood
            String neighbourhood = comboBox2.getValue();
            String val = String.valueOf(property.getNeighbourhood());
            if (neighbourhood != null && !neighbourhood.isEmpty() && !neighbourhood.equalsIgnoreCase(val)) {
                matches = false;
            }

            //filter by Construction Value
            String constructionValueOption = comboBox3.getValue();
            int constructVal = property.getConstructionValue();
            if (constructionValueOption != null && !constructionValueOption.isEmpty()) {
                switch (constructionValueOption) {
                    case "<$75,000":
                        if (constructVal >= 75000) matches = false;
                        break;
                    case "$75,000-$100,000":
                        if (constructVal < 75000 || constructVal > 100000) matches = false;
                        break;
                    case ">$100,000":
                        if (constructVal <= 100000) matches = false;
                        break;
                }
            }

            //add the property if it matches all criteria
            if (matches) filteredData.add(property);
        }

        //update table with filtered data
        table.getItems().clear();
        table.getItems().addAll(filteredData);
    }

    /**
     * Construction Efficiency Score: Penalize high construction costs relative to floor area
     *   score = 1 - (construction value / floor area)
     *
     * Property Value Efficiency Score: Reward properties that add significant floor area relative to prop value
     *   score = floor area / property value
     *
     * Floor Area Score: Basic normalization calculation
     *   score = (floor area - min floor area) / (max floor area - min floor area)
     *
     * Units Added Score: 1 unit = 0.5, 2 units = 1.0
     *
     * Neighbourhood Adjustment: Give neighbourhoods values of 0.2, 0.0, or -0.2 based on desirability
     *
     * Weights: give each score a weighted grade
     *  construction Efficiency = 30%, prop value efficiency = 30%
     *  floor area = 25%, units added = 15%
     *
     * Normalize scores across the dataset to ensure comparability, improves data quality
     *
     * @return garden suite grade
     */

    public static double calculateGrade(String neighborhood, double constructionValue, double propertyValue,
                                        double floorArea, int unitsAdded,
                                        double minConstructionEfficiency, double maxConstructionEfficiency,
                                        double minPropertyValueEfficiency, double maxPropertyValueEfficiency,
                                        Map<String, Double> neighborhoodAdjustments) {
        //calculate and normalize construction value score
        double constructionEfficiency = 1 - (constructionValue / floorArea);
        double normalizedConstructionEfficiency = normalize(constructionEfficiency, minConstructionEfficiency, maxConstructionEfficiency);

        //calculate and normalize property value score
        double propertyValueEfficiency = floorArea / propertyValue;
        double normalizedPropertyValueEfficiency = normalize(propertyValueEfficiency, minPropertyValueEfficiency, maxPropertyValueEfficiency);

        //floor Area Score: lowest set to 500, highest set to 1850
        double floorAreaScore = (floorArea - 500) / (1850 - 500);

        //Units added score
        double unitsAddedScore = (unitsAdded == 1) ? 0.5 : 1.0;

        //neighborhood Adjustment, if no value for the neighbourhood was assigned just use 0
        double neighborhoodAdjustment = neighborhoodAdjustments.getOrDefault(neighborhood, 0.0);

        //Construction efficiency, prop value efficiency, floor area, units added
        double w1 = 0.3, w2 = 0.3, w3 = 0.25, w4 = 0.15;

        //calculate grade with weights
        return (w1 * normalizedConstructionEfficiency) +
                (w2 * normalizedPropertyValueEfficiency) +
                (w3 * floorAreaScore) +
                (w4 * unitsAddedScore) +
                neighborhoodAdjustment;
    }

    /**
     * function that can be used to normalize data
     * @param value what is being normalized
     * @param min value in the data set
     * @param max value in the data set
     */
    private static double normalize(double value, double min, double max) {
        return (value - min) / (max - min);
    }

    public static void main(String[] args) {
        launch();
    }
}
