module com.example.ersapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires jakarta.validation;
    requires spring.web;
    requires spring.beans;
    requires spring.data.jpa;
    requires java.net.http;
    requires org.hibernate.validator;


    opens com.example.ersapp to javafx.fxml, org.hibernate.orm.core, org.hibernate.validator;
    exports com.example.ersapp;
}