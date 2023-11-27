module com.example.socialmedia {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;

    opens com.example.socialmedia.ro.ubbcluj.map to javafx.fxml;
    opens com.example.socialmedia.ui to javafx.fxml;
    opens com.example.socialmedia.controllers to javafx.fxml;

    exports com.example.socialmedia.ro.ubbcluj.map;
    exports com.example.socialmedia.ui;
    exports com.example.socialmedia.controllers;
}

