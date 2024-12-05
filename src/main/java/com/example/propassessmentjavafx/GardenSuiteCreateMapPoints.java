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
         * 1-3 = grey
         * 4-5 = green
         * 6-7 = blue
         * 8-9 = purple
         * 10 = orange
         */
        // create different coloured diamond simple marker symbols
        SimpleMarkerSymbol commonRatingSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.DIAMOND, Color.GRAY, 10);
        SimpleMarkerSymbol uncommonRatingSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.DIAMOND, Color.LIGHTGREEN, 10);
        SimpleMarkerSymbol rareRatingSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.DIAMOND, Color.BLUE, 10);
        SimpleMarkerSymbol epicRatingSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.DIAMOND, Color.MAGENTA, 10);
        SimpleMarkerSymbol legendaryRatingSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.DIAMOND, Color.ORANGE, 10);

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

            if (grade < 4.0){graphic = new Graphic(coordinates, commonRatingSymbol);}
            else if(grade < 6.0) {graphic = new Graphic(coordinates, uncommonRatingSymbol);}
            else if(grade < 8.0) {graphic = new Graphic(coordinates, rareRatingSymbol);}
            else if(grade < 10.0) {graphic = new Graphic(coordinates, epicRatingSymbol);}
            else{graphic = new Graphic(coordinates, legendaryRatingSymbol);}

            graphicsOverlay.getGraphics().add(graphic);
        }

    }
}
