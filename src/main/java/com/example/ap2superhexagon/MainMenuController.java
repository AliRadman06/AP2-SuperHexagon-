package com.example.ap2superhexagon;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {
    @FXML
    private Button startButton;
    @FXML private Button historyButton;
    @FXML private Button settingButton;
    @FXML private Button exitButton;
    @FXML private Label bestRecordLabel;

    // این متد وقتی فایل FXML لود میشه صدا زده میشه
    @FXML
    public void initialize() {

        AudioManager.playMenuTheme();

//        // اینجا میتونی مقدار اولیه bestRecordLabel رو از فایل یا جای دیگه بخونی و ست کنی
//        long currentHighScore = loadHighScore(); // متد فرضی
//        bestRecordLabel.setText(String.valueOf(currentHighScore));

        long bestMillis = HighScoreManager.loadHighScore();
        bestRecordLabel.setText(String.format("%.1f", bestMillis / 1000.0) + " s");


        SceneManager.setMainMenuScene(startButton.getScene());
        AudioManager.loadSettings();
        GameHistoryManager.loadSettings();


    }

    @FXML
    void handleStartButton(ActionEvent event) {

        AudioManager.playClickSound();
        AudioManager.playGameTheme();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/ap2superhexagon/PreGameSetup.fxml"));
            Parent perGameRoot = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(perGameRoot);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            System.err.println("Failed to load PreGameSetup.fxml");
            e.printStackTrace();
        }
        // کد رفتن به صفحه بازی
    }

    @FXML
    void handleHistoryButton(ActionEvent event) {
        AudioManager.playClickSound();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/ap2superhexagon/history-view.fxml"));
            Parent historyRoot = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(historyRoot);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load history-view.fxml");
            e.printStackTrace();
        }
    }

    @FXML
    void handleSettingButton(ActionEvent event) {
        AudioManager.playClickSound();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ap2superhexagon/settings-view.fxml"));
            Scene settingsScene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(settingsScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleExitButton(ActionEvent event) {
        AudioManager.playClickSound();
        javafx.application.Platform.exit();
    }

}
