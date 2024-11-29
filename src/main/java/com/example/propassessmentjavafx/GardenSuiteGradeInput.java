package com.example.propassessmentjavafx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GardenSuiteGradeInput {

    /**
     * method that displays the user input popup
     */
    public void display(Stage primaryStage) {
        //create input fields
        Label propertyValueLabel = new Label("Current Property Value:");
        TextField propertyValueField = new TextField();

        Label constructionValueLabel = new Label("Construction Value:");
        TextField constructionValueField = new TextField();

        Label floorAreaLabel = new Label("Floor Area (sq ft):");
        TextField floorAreaField = new TextField();

        //create dropdown
        Label unitsAddedLabel = new Label("Units Added:");
        ComboBox<Integer> unitsAddedComboBox = new ComboBox<>();
        unitsAddedComboBox.getItems().addAll(1, 2);

        Label gradeResultLabel = new Label("Grade:");
        Label gradeOutputLabel = new Label();

        // Submit button
        Button submitButton = new Button("Calculate Grade");
        submitButton.setOnAction(event -> {
            try {
                //get user input
                double propertyValue = Double.parseDouble(propertyValueField.getText());
                double constructionValue = Double.parseDouble(constructionValueField.getText());
                double floorArea = Double.parseDouble(floorAreaField.getText());
                int unitsAdded = unitsAddedComboBox.getValue();

                //get grade
                double grade = PropertyAssessmentsJavaFX.calculateGrade(constructionValue, propertyValue, floorArea, unitsAdded);

                //display grade, rounded to one decimal place
                gradeOutputLabel.setText(String.format("%.1f", grade));
            } catch (Exception e) {
                gradeOutputLabel.setText("Error in input");
            }
        });

        //layout setup
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(propertyValueLabel, 0, 0);
        grid.add(propertyValueField, 1, 0);
        grid.add(constructionValueLabel, 0, 1);
        grid.add(constructionValueField, 1, 1);
        grid.add(floorAreaLabel, 0, 2);
        grid.add(floorAreaField, 1, 2);
        grid.add(unitsAddedLabel, 0, 3);
        grid.add(unitsAddedComboBox, 1, 3);
        grid.add(gradeResultLabel, 0, 4);
        grid.add(gradeOutputLabel, 1, 4);
        grid.add(submitButton, 0, 5, 2, 1);

        grid.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10, grid);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Garden Suite Grade Input");
        primaryStage.show();
    }
}

