module com.example.pettimer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires java.sql;


    opens com.example.pettimer to javafx.fxml;
    exports com.example.pettimer;
}