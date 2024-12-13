module com.example.boxgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires rxcontrols;
    requires org.json;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires annotations;
    requires java.desktop;
    requires javafx.media;

    opens com.example.boxgame to javafx.fxml;
    exports com.example.boxgame;
    exports main;
    opens main to javafx.fxml;
}

