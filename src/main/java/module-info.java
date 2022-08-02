module bgps.tetrisgensk {
    requires javafx.graphics;
    requires javafx.base;

    opens bgps.tetrisgensk to javafx.fxml;
    exports bgps.tetrisgensk;
}