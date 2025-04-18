package com.example.ap2superhexagon;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.util.Duration;


import java.io.IOException;

public class GameoverView {

    @FXML
    private ImageView backgroundImageView;

    @FXML
    private Label recordlable;

    @FXML
    private Button restartbutton;

    @FXML
    private Button backtomenubutton;

    @FXML
    private AnchorPane rootPane;


    public void setBackgroundImage() {
        Image image = new Image(getClass().getResource("/Images/Untitled-4.png").toExternalForm());
        backgroundImageView.setImage(image);
        BoxBlur blur = new BoxBlur(15, 15, 3);
        backgroundImageView.setEffect(blur);
    }



    public void setFinalScore(String playerName, long milliseconds) {
        recordlable.setText(String.format("%.1f s", milliseconds / 1000.0));
    }

    @FXML
    private void initialize() {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), rootPane);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    @FXML
    private void handleRestartButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ap2superhexagon/PreGameSetup.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backtomenubutton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackToMenuButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ap2superhexagon/main-menu-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backtomenubutton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}