package com.example.ap2superhexagon;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class SettingsController {
    @FXML private ToggleButton musicToggle;
    @FXML private ToggleButton historyToggle;
    @FXML private ImageView backgroundImage;

    @FXML
    public void initialize() {

        backgroundImage.setImage(new Image(getClass().getResource("/Images/Untitled-4.png").toString()));
        musicToggle.setSelected(AudioManager.isMusicEnabled());
        historyToggle.setSelected(GameHistoryManager.isHistoryEnabled());
        updateToggleButtons();
    }

    @FXML
    void toggleMusic() {
        AudioManager.playClickSound();
        AudioManager.setMusicEnabled(musicToggle.isSelected());
        updateToggleButtons();
    }

    @FXML
    void toggleHistory() {
        AudioManager.playClickSound();
        boolean enabled = historyToggle.isSelected();
        GameHistoryManager.setHistoryEnabled(enabled);
        updateToggleButtons();

        // نمایش پیام وضعیت
        System.out.println("Game history saving is now " + (enabled ? "ENABLED" : "DISABLED"));
    }

    @FXML
    void handleBackButton(ActionEvent event) {
        AudioManager.playClickSound();

        // Save settings when exiting
        GameHistoryManager.saveSettings();
        AudioManager.saveSettings();

        // Return to previous screen
        try {
            System.out.println("Back button clicked!");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/ap2superhexagon/main-menu-view.fxml"));
            Parent mainMenuRoot = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(mainMenuRoot);
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e) {
            System.err.println("Failed to load main-menu-view.fxml");
            e.printStackTrace();
        }
    }

    private void updateToggleButtons() {
        musicToggle.setText(musicToggle.isSelected() ? "ON" : "OFF");
        historyToggle.setText(historyToggle.isSelected() ? "ON" : "OFF");

        // Visual feedback
        musicToggle.setStyle(musicToggle.isSelected() ?
                "-fx-base: #00ff0d;" : "-fx-base: #ff1200;");
        historyToggle.setStyle(historyToggle.isSelected() ?
                "-fx-base: #00ff09;" : "-fx-base: #ff0f00;");
    }
}