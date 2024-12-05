module com.example.propassessmentjavafx {

    requires org.controlsfx.controls;
    requires org.json;
    requires com.esri.arcgisruntime;

    opens com.example.propassessmentjavafx to javafx.fxml;
    exports com.example.propassessmentjavafx;
}