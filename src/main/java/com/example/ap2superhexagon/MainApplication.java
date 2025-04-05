package com.example.ap2superhexagon;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) {
        try {
            URL fxmlLocation = getClass().getResource("/com/example/ap2superhexagon/main-menu-view.fxml");
            if (fxmlLocation == null) {
                System.err.println("Could not find main-menu-view.fxml");
                return;
            }

            FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("SuperHexagon-");
            stage.show();
        }

        catch (IOException e) {
            System.err.println("Could not load main-menu-view.fxml");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}