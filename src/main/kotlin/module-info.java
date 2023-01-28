module com.example.demo2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires lettuce.core;

    opens com.example.demo2 to javafx.fxml;
    exports com.example.demo2;
}