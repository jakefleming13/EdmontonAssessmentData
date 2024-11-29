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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.scene.control.Button;

public class PropertyAssessmentsJavaFX extends Application {
    private final TableView<PropertyAssessment> table = new TableView<>();

    @Override
    public void start(Stage primaryStage) {
        //Init TableView and columns
        initializeTable();

        //Test garden suite data
        GardenSuiteDataFetcher fetcher = new GardenSuiteDataFetcher();
        List<GardenSuiteAssessment> assessments = fetcher.fetchGardenSuiteData();

        //fetch and display data
        List<PropertyAssessment> propertyData = fetchPropertyData(assessments);
        table.getItems().addAll(propertyData);

        //Button to allow user to input info about a garden suite
        Button openGradeInputButton = new Button("Input Garden Suite Data");
        openGradeInputButton.setOnAction(event -> {
            GardenSuiteGradeInput gradeInput = new GardenSuiteGradeInput();
            gradeInput.display(new Stage());
        });

        //ComboBoxes to display dropdowns
        ComboBox<String> comboBox1 = new ComboBox<>();
        ComboBox<String> comboBox2 = new ComboBox<>();
        ComboBox<String> comboBox3 = new ComboBox<>();
        ComboBox<String> comboBox4 = new ComboBox<>();
        comboBox1.getItems().addAll("", "<$200,000", "$200,000-$400,000", ">$400,000");
        comboBox2.getItems().addAll("", "Downtown", "Oliver", "Bearspaw");
        comboBox3.getItems().addAll("", "<$75,000", "$75,000-$100,000", ">$100,000");
        comboBox4.getItems().addAll("", "<650 sq ft", "650 sq ft - 1200 sq ft", ">1200 sq ft");

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
        //VBox layout = new VBox(10, comboBoxLayout, table);
        VBox layout = new VBox(10, comboBoxLayout, table, openGradeInputButton);
        layout.setAlignment(Pos.CENTER);

        comboBox1.setOnAction(event -> filterData(assessments, comboBox1, comboBox2, comboBox3, comboBox4));
        comboBox2.setOnAction(event -> filterData(assessments, comboBox1, comboBox2, comboBox3, comboBox4));
        comboBox3.setOnAction(event -> filterData(assessments, comboBox1, comboBox2, comboBox3, comboBox4));
        comboBox4.setOnAction(event -> filterData(assessments, comboBox1, comboBox2, comboBox3, comboBox4));

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

    private List<PropertyAssessment> fetchPropertyData(List<GardenSuiteAssessment> gardenSuites) {
        List<PropertyAssessment> propertyAssessments = new ArrayList<>();
        int count = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream("/Property_Assessment_Data_2024.csv"))))) {
            String line = reader.readLine(); //skip header
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(","); // Assuming CSV is comma-separated

                //Get all fields
                int accountNumber = Integer.parseInt(columns[0].trim());
                String houseNumber = columns[2].trim();
                String streetName = columns[3].trim();
                Address propAddress = new Address(houseNumber, streetName);
                String garage = columns[4].trim();
                String neighbourhood = columns[6].trim();
                int assessedValue = Integer.parseInt(columns[8].trim());

                //Only look at properties over $50,000 in value and under $2,000,000 in value
                if(assessedValue < 50000 || assessedValue > 2000000){
                    continue;
                }

                //if address is empty continue
                if (houseNumber.isEmpty() || streetName.isEmpty()) {
                    continue;
                }

                //match the garden suite data
                int constructionVal = 0;
                int floorA = 0;
                int unitsAdd = 0;
                for (GardenSuiteAssessment gardenSuite : gardenSuites) {
                    if (gardenSuite.getAddress().equals(propAddress)) {
                        constructionVal = (int) gardenSuite.getConstructionValue();
                        floorA = (int) gardenSuite.getFloorArea();
                        unitsAdd = gardenSuite.getUnitsAdded();
                        break;
                    }
                }

                //skip property if no garden suite data is matched
                if (constructionVal == 0 || floorA == 0 || unitsAdd == 0) {
                    continue;
                }

                //call the calculate grade method to receive a grade
                double grade = calculateGrade(constructionVal, assessedValue, floorA, unitsAdd);

                //create the PropertyAssessment object
                PropertyAssessment propertyAssessment = new PropertyAssessment(
                        accountNumber,
                        new Address(houseNumber, streetName),
                        assessedValue,
                        null, // Set to null for assessment classes
                        new Neighbourhood(neighbourhood, ""), // ignore ward
                        null, // Set to null for Latitude and Longitude
                        garage,
                        constructionVal,
                        floorA,
                        unitsAdd,
                        grade
                );
                count += 1;

                propertyAssessments.add(propertyAssessment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(count);

        return propertyAssessments;
    }


    private void filterData(List<GardenSuiteAssessment> gardenSuites, ComboBox<String> comboBox1, ComboBox<String> comboBox2, ComboBox<String> comboBox3, ComboBox<String> comboBox4) {
        List<PropertyAssessment> filteredData = new ArrayList<>();

        for (PropertyAssessment property : fetchPropertyData(gardenSuites)) {
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

            //filter by floor area Value
            String florAreaOption = comboBox4.getValue();
            int floorAreaVal = property.getFloorArea();
            if (florAreaOption != null && !florAreaOption.isEmpty()) {
                switch (florAreaOption) {
                    case "<650 sq ft":
                        if (floorAreaVal >= 650) matches = false;
                        break;
                    case "650 sq ft - 1200 sq ft":
                        if (floorAreaVal < 650 || floorAreaVal > 1200) matches = false;
                        break;
                    case ">1200 sq ft":
                        if (floorAreaVal <= 1200) matches = false;
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
     * Property Value Efficiency Score: Reward properties that add significant floor area relative to prop value
     *   score = floor area / property value
     * Floor Area Score: Basic normalization calculation
     *   score = (floor area - min floor area) / (max floor area - min floor area)
     * Units Added Score: 1 unit = 0.5, 2 units = 1.0
     * Neighbourhood Adjustment: Give neighbourhoods values of 0.2, 0.0, or -0.2 based on desirability
     * Weights: give each score a weighted grade
     *  construction Efficiency = 30%, prop value efficiency = 30%
     *  floor area = 25%, units added = 15%
     * Normalize scores across the dataset to ensure comparability, improves data quality
     *
     * @return garden suite grade
     */

    public static double calculateGrade(double constructionValue, double propertyValue,
                                        double floorArea, int unitsAdded) {

        //Set the min/max's that will be used to normalize
        double minConstructionEfficiency = 0;
        double maxConstructionEfficiency = 60;
        double minPropertyValueEfficiency = 0.002;
        double maxPropertyValueEfficiency = 0.01;

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

        //Construction efficiency, prop value efficiency, floor area, units added
        double w1 = 0.3, w2 = 0.3, w3 = 0.25, w4 = 0.15;

        //calculate grade with weights
        double calc = (w1 * normalizedConstructionEfficiency) +
                (w2 * normalizedPropertyValueEfficiency) +
                (w3 * floorAreaScore) +
                (w4 * unitsAddedScore);

        //Ensure that all grades become a value out of 10
        double mappedGrade = mapGradeToScale(calc, -1.3, 0.04, 0, 10);

        // round the mapped grade to 1 decimal places
        BigDecimal roundedValue = new BigDecimal(mappedGrade).setScale(1, RoundingMode.HALF_UP);

        //return
        return roundedValue.doubleValue();
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

    /**
     * function that can be used to ensure that all grades are represented in a value out of 10
     * @param grade what is being normalized
     */
    public static double mapGradeToScale(double grade, double minOldGrade, double maxOldGrade, double minNewGrade, double maxNewGrade) {
        return ((grade - minOldGrade) * (maxNewGrade - minNewGrade)) / (maxOldGrade - minOldGrade) + minNewGrade;
    }

    public static void main(String[] args) {
        launch();
    }
}
