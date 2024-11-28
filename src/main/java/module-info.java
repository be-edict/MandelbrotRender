module de.osfalia.reinhard.pixeldrawtest {
    requires javafx.controls;
    requires javafx.fxml;


    opens de.osfalia.reinhard.pixeldrawtest to javafx.fxml;
    exports de.osfalia.reinhard.pixeldrawtest;
}