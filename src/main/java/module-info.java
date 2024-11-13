module com.example.propassessmentjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.json;

    opens com.example.propassessmentjavafx to javafx.fxml;
    exports com.example.propassessmentjavafx;
}