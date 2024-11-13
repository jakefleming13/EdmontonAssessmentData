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
        comboBox3.getItems().addAll("", "Yes", "No");
        comboBox4.getItems().addAll("");

        //placeholders
        comboBox1.setPromptText("Select an option");
        comboBox2.setPromptText("Select an option");
        comboBox3.setPromptText("Select an option");
        comboBox4.setPromptText("Select an option");

        //Labels
        Label label1 = new Label("AVG Property Value:");
        Label label2 = new Label("Neighbourhood:");
        Label label3 = new Label("Garage:");
        Label label4 = new Label("------:");

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
        primaryStage.setTitle("Edmonton Property Assessment Data");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeTable() {
        //define columns with cell value factories
        TableColumn<PropertyAssessment, Integer> accountNumberCol = new TableColumn<>("Account Number");
        accountNumberCol.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));

        TableColumn<PropertyAssessment, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<PropertyAssessment, String> garageCol = new TableColumn<>("Garage");
        garageCol.setCellValueFactory(new PropertyValueFactory<>("garage"));

        TableColumn<PropertyAssessment, String> neighbourhoodCol = new TableColumn<>("Neighbourhood");
        neighbourhoodCol.setCellValueFactory(new PropertyValueFactory<>("neighbourhood"));

        TableColumn<PropertyAssessment, Integer> assessedValueCol = new TableColumn<>("Assessed Value");
        assessedValueCol.setCellValueFactory(new PropertyValueFactory<>("assessedValue"));

        table.getColumns().addAll(accountNumberCol, addressCol, garageCol, neighbourhoodCol, assessedValueCol);
    }

    private List<PropertyAssessment> fetchPropertyData() {
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

            //filter by Garage
            String garageOption = comboBox3.getValue();
            String garage = property.getGarage();
            if (garageOption != null && !garageOption.isEmpty()) {
                if (garageOption.equals("Yes") && !garage.equalsIgnoreCase("Y")) matches = false;
                else if (garageOption.equals("No") && !garage.equalsIgnoreCase("N")) matches = false;
            }

            //add the property if it matches all criteria
            if (matches) filteredData.add(property);
        }

        //update table with filtered data
        table.getItems().clear();
        table.getItems().addAll(filteredData);
    }


    public static void main(String[] args) {
        launch();
    }
}
