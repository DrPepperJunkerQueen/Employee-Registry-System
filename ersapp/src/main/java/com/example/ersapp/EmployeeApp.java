package com.example.ersapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EmployeeApp extends Application
{
    private EmployeeAppController controller;

    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(EmployeeApp.class.getResource("hello-view.fxml"));
        int windowWidth = 1280;
        int windowHeight = 720;
        Scene scene = new Scene(fxmlLoader.load(), windowWidth, windowHeight);
        stage.setTitle("Employee App");
        stage.setScene(scene);
        stage.show();
        controller = fxmlLoader.getController();
    }

    @Override
    public void stop() {
        if (controller != null) {
            controller.shutdown(); // zamyka EntityManager itp.
        }
    }

    public static void main(String[] args) {
        launch();
    }
}