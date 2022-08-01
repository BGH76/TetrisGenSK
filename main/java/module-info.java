module bgps.tetrisgensk {
    requires javafx.controls;
    requires javafx.fxml;


    opens bgps.tetrisgensk to javafx.fxml;
    exports bgps.tetrisgensk;
}