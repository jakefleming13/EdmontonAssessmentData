package com.example.propassessmentjavafx;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyGraphicsOverlayResult;
import com.esri.arcgisruntime.mapping.view.MapView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PropertyAssessmentsJavaFX extends Application {
    private final TableView<PropertyAssessment> table = new TableView<>();
    private MapView mapView;
    private GraphicsOverlay graphicsOverlay;
    private ListenableFuture<IdentifyGraphicsOverlayResult> identifyGraphics;

    public void start(Stage primaryStage) {
        //save a reference to the original scene
        Scene startScene = createStartScene(primaryStage);

        //set the initial scene
        primaryStage.setScene(startScene);
        primaryStage.setTitle("Edmonton Garden Suite Data");
        primaryStage.show();
    }

    /**
     * Method that allows us to init our initial Scene
     */
    private Scene createStartScene(Stage primaryStage) {
        //init TableView
        initializeTable();

        //get garden suite data
        GardenSuiteDataFetcher fetcher = new GardenSuiteDataFetcher();
        List<GardenSuiteAssessment> assessments = fetcher.fetchGardenSuiteData();

        //fetch and display data
        List<PropertyAssessment> propertyData = fetchPropertyData(assessments);
        table.getItems().addAll(propertyData);

        //authenticate ArcGIS with API key
        String apiKey = System.getenv("ARCGIS_KEY");
        ArcGISRuntimeEnvironment.setApiKey(apiKey);

        //button to allow user to input info about a garden suite
        Button openGradeInputButton = new Button("Garden Suite Calculator");
        openGradeInputButton.setOnAction(event -> {
            GardenSuiteGradeInput gradeInput = new GardenSuiteGradeInput();
            gradeInput.display(new Stage());
        });

        //map button to nav to map
        Button openMapButton = getMapButton(primaryStage, propertyData);

        //dropdown and Labels setup
        ComboBox<String> comboBox1 = new ComboBox<>();
        ComboBox<String> comboBox2 = new ComboBox<>();
        ComboBox<String> comboBox3 = new ComboBox<>();
        ComboBox<String> comboBox4 = new ComboBox<>();
        comboBox1.getItems().addAll("", "<$200,000", "$200,000-$400,000", ">$400,000");
        comboBox2.getItems().addAll("", "Downtown", "Oliver", "Bearspaw");
        comboBox3.getItems().addAll("", "<$75,000", "$75,000-$100,000", ">$100,000");
        comboBox4.getItems().addAll("", "<650 sq ft", "650 sq ft - 1200 sq ft", ">1200 sq ft");

        //dropdown placeholders
        comboBox1.setPromptText("Select an option");
        comboBox2.setPromptText("Select an option");
        comboBox3.setPromptText("Select an option");
        comboBox4.setPromptText("Select an option");

        //dropdown titles
        Label label1 = new Label("Property Value:");
        Label label2 = new Label("Neighbourhood:");
        Label label3 = new Label("Construction Value:");
        Label label4 = new Label("Garden Suite sq ft:");

        //group dropdowns and labels
        VBox vBox1 = new VBox(5, label1, comboBox1);
        VBox vBox2 = new VBox(5, label2, comboBox2);
        VBox vBox3 = new VBox(5, label3, comboBox3);
        VBox vBox4 = new VBox(5, label4, comboBox4);

        HBox comboBoxLayout = new HBox(20, vBox1, vBox2, vBox3, vBox4);
        comboBoxLayout.setAlignment(Pos.CENTER);

        HBox groupButtons = new HBox(20, openGradeInputButton, openMapButton);
        groupButtons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10, comboBoxLayout, table, groupButtons);
        layout.setAlignment(Pos.CENTER);

        //call filterData on selection
        comboBox1.setOnAction(event -> filterData(assessments, comboBox1, comboBox2, comboBox3, comboBox4));
        comboBox2.setOnAction(event -> filterData(assessments, comboBox1, comboBox2, comboBox3, comboBox4));
        comboBox3.setOnAction(event -> filterData(assessments, comboBox1, comboBox2, comboBox3, comboBox4));
        comboBox4.setOnAction(event -> filterData(assessments, comboBox1, comboBox2, comboBox3, comboBox4));

        //width x height
        return new Scene(layout, 825, 550);
    }

    /**
     * private method that allows us to display a map of all garden suites
     */
    private Button getMapButton(Stage primaryStage, List<PropertyAssessment> propertyData) {
        Button openMapButton = new Button("Garden Suite Map");

        //mapView for ArcGIS
        final ArcGISMap map = new ArcGISMap(BasemapStyle.ARCGIS_TOPOGRAPHIC);

        //create a spatial reference using EPSG:4326 (WGS84)
        SpatialReference spatialReference = SpatialReference.create(4326);

        //create an initial extent envelope
        Point leftPoint = new Point(-113.698640, 53.645195, spatialReference);
        Point rightPoint = new Point(-113.308707, 53.428465, spatialReference);
        Envelope initialExtent = new Envelope(leftPoint, rightPoint);

        //create a viewpoint from envelope
        Viewpoint viewPoint = new Viewpoint(initialExtent);

        //set an initial extent on the map
        map.setInitialViewpoint(viewPoint);

        //create a map view and set the map to it
        mapView = new MapView();
        mapView.setMap(map);

        // Create graphic pins
        // create the graphics overlay
        graphicsOverlay = new GraphicsOverlay();

        // add the graphic overlay to the map view
        mapView.getGraphicsOverlays().add(graphicsOverlay);

        // Function to add graphics points to map
        GardenSuiteCreateMapPoints.createMapPoints(propertyData, graphicsOverlay);

        // Set up event handler to lisen for clicks on map
        mapView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.isStillSincePress()){
                // create a point from location clicked
                Point2D mapViewPoint = new Point2D(event.getX(), event.getY());

                // identify graphics on the graphics overlay
                identifyGraphics = mapView.identifyGraphicsOverlayAsync(graphicsOverlay, mapViewPoint, 10, false);

                identifyGraphics.addDoneListener(() -> Platform.runLater(this::createGraphicDialog));
            }
        });

        //Add back button to allow for navigation
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            primaryStage.close();
            Platform.runLater(() -> {
                try {
                    new PropertyAssessmentsJavaFX().start(new Stage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        });
        StackPane.setAlignment(backButton, Pos.TOP_LEFT);
        backButton.setPadding(new Insets(10));

        // Create a label for the title
        Label titleLabel = new Label("Grade");
        titleLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 5px;"); // Style the title

        // Create a small legend bar
        VBox legend = new VBox(5); // Vertical layout for legend
        legend.setPadding(new Insets(20));
        legend.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-border-color: black; -fx-border-width: 1;");
        legend.setAlignment(Pos.TOP_LEFT);
        // Set the legend size
        // Limit the width of the legend to 30% of the window width
        legend.setMaxWidth(80); // Assuming the window is 800px wide; 800 * 0.3 = 240
        legend.setMaxHeight(100); // Assuming the window is 800px wide; 800 * 0.3 = 240

        // Add the title label to the VBox
        legend.getChildren().add(titleLabel);

        // Add items to the legend
        legend.getChildren().addAll(
                createLegendItem(Color.RED, "0-3"),
                createLegendItem(Color.YELLOW, "3-5"),
                createLegendItem(Color.LIGHTGREEN, "5-7"),
                createLegendItem(Color.GREEN, "7-10")
        );

        // Align the legend in the top-right corner
        StackPane.setAlignment(legend, Pos.TOP_RIGHT);
        legend.setPadding(new Insets(10));

        // Add mapView, back button, and legend to the layout
        StackPane mapLayout = new StackPane(mapView, backButton, legend);

        // Create the scene
        Scene mapScene = new Scene(mapLayout, 800, 600);

        //switch scenes by restarting the app
        openMapButton.setOnAction(e -> primaryStage.setScene(mapScene));

        return openMapButton;
    }

    /**
     * Indicates when a graphic is clicked by showing an Alert.
     */
    private void createGraphicDialog() {

        try {
            // get the list of graphics returned by identify
            IdentifyGraphicsOverlayResult result = identifyGraphics.get();
            Graphic graphic = result.getGraphics().get(0);

            if (!(graphic == null)) {
                // show an alert dialog box if a graphic was returned
                var dialog = new Alert(Alert.AlertType.INFORMATION);
                dialog.initOwner(mapView.getScene().getWindow());
                dialog.setHeaderText(null);
                dialog.setTitle("Garden Suite Information");
                dialog.setContentText("Address: " + graphic.getAttributes().get("ADDRESS") +
                        "\nAssessed Value of parent: $" + graphic.getAttributes().get("ASSESSED_VALUE") +
                        "\nConstruction value of garden suite: $" + graphic.getAttributes().get("CONSTRUCTION_VALUE") +
                        "\nGrade: " + graphic.getAttributes().get("GRADE"));
                dialog.showAndWait();
            }
        } catch (Exception e) {
            // on any error, display the stack trace
            e.printStackTrace();
        }
    }

    /**
     * Stops and releases all resources used in application.
     */
    @Override
    public void stop() {

        if (mapView != null) {
            mapView.dispose();
        }
    }

    /**
     * Method that creates Hboxes in legend bar
     * @param color
     * @return
     */
    private HBox createLegendItem(Color color, String description) {
        Rectangle colorBox = new Rectangle(15, 15, color); // Small square for color
        Label label = new Label(description);
        label.setStyle("-fx-font-size: 12;");

        HBox legendItem = new HBox(10, colorBox, label);
        legendItem.setAlignment(Pos.TOP_LEFT);
        legendItem.setPadding(new Insets(2)); // Minimal padding
        return legendItem;
    }

    /**
     * method that creates our data table through javaFX
     */
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

    /**
     * method that retrieves property assessment data from the provided csv file and combines garden suite data
     * @param gardenSuites all garden suite data
     * @return list of property assessments
     */
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
                double latitude = Double.parseDouble(columns[9].trim());
                double longitude = Double.parseDouble(columns[10].trim());
                Location location = new Location(latitude, longitude);

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
                        location,
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

    /**
     * method that allows our dropdowns to filter garden suite data
     * @param gardenSuites list of all gardenSuites
     * @param comboBox1 dropdown 1
     * @param comboBox2 dropdown 2
     * @param comboBox3 dropdown 3
     * @param comboBox4 dropdown 4
     */
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
