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
        // create a red circle simple marker symbol
        SimpleMarkerSymbol redCircleSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 10);
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
            graphic = new Graphic(coordinates, redCircleSymbol);
            graphicsOverlay.getGraphics().add(graphic);
        }

        /*
        Graphic pointGraphic = new Graphic(point, pointAttributes, simpleMarkerSymbol);
        SimpleMarkerSymbol simpleMarkerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.TRIANGLE, 0xFF000000, 10);
        SimpleLineSymbol simpleMarkerSymbolOutline = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, 0xFFFFFFFF, 2);
        simpleMarkerSymbol.setOutline(simpleMarkerSymbolOutline);
         */


    }
}
