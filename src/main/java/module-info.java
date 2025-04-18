module com.example.ap2superhexagon {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires com.google.gson;
    requires javafx.media;
    requires java.prefs;

    opens com.example.ap2superhexagon to javafx.fxml, com.google.gson;
    exports com.example.ap2superhexagon;
}