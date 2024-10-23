module com.example.serverlicensecheck {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.rmi;
    requires java.desktop;
    requires java.logging;

    opens com.example.serverlicensecheck to javafx.fxml;
    exports com.example.serverlicensecheck;
    exports com.example.serverlicensecheck.UI;
    exports com.example.serverlicensecheck.exception;
    opens com.example.serverlicensecheck.UI to javafx.fxml;
}