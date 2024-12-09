package com.example.propassessmentjavafx;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import javafx.scene.paint.Color;

import java.util.List;

public class GardenSuiteCreateMapPoints {

    /* public function that creates points on map graphics layer for each garden suite */
    public static void createMapPoints(List<PropertyAssessment> assessments, GraphicsOverlay graphicsOverlay){
        /**
         * Colour scheme for markers:
         * 0-3 red
         * 3-5 yellow
         * 5-7 lightgreen
         * 7-10 green
         */
        // create different coloured diamond simple marker symbols
        SimpleMarkerSymbol commonRatingSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.DIAMOND, Color.RED, 8);
        SimpleMarkerSymbol uncommonRatingSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.DIAMOND, Color.YELLOW, 8);
        SimpleMarkerSymbol rareRatingSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.DIAMOND, Color.LIGHTGREEN, 8);
        SimpleMarkerSymbol epicRatingSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.DIAMOND, Color.GREEN, 8);
        SimpleMarkerSymbol legendaryRatingSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.DIAMOND, Color.GREEN, 8);

        // Spatial reference
        final SpatialReference spatialReference = SpatialReference.create(4326);

        for (PropertyAssessment assessment : assessments){

            // Create point coordinates
            Location location = assessment.getLocation();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            Point coordinates = new Point(longitude, latitude, spatialReference);

            // create graphics and add to graphics overlay
            Graphic graphic;

            // If ladder
            double grade = assessment.getGardenSuiteGrade();

            if (grade < 3.0){graphic = new Graphic(coordinates, commonRatingSymbol);}
            else if(grade < 5.0) {graphic = new Graphic(coordinates, uncommonRatingSymbol);}
            else if(grade < 7.0) {graphic = new Graphic(coordinates, rareRatingSymbol);}
            else if(grade < 10.0) {graphic = new Graphic(coordinates, epicRatingSymbol);}
            else{graphic = new Graphic(coordinates, legendaryRatingSymbol);}

            // Attach garden suite attributes to graphic
            graphic.getAttributes().put("ADDRESS", String.valueOf(assessment.getAddress()));
            graphic.getAttributes().put("ASSESSED_VALUE", String.valueOf(assessment.getAssessedValue()));
            graphic.getAttributes().put("CONSTRUCTION_VALUE", String.valueOf(assessment.getConstructionValue()));
            graphic.getAttributes().put("GRADE", String.valueOf(grade));

            graphicsOverlay.getGraphics().add(graphic);
        }

    }
}
