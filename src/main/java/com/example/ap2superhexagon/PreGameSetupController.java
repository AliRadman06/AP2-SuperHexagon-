package com.example.ap2superhexagon;

import javafx.event.ActionEvent; // برای مدیریت رویداد کلیک
import javafx.fxml.FXML;        // برای اتصال به عناصر FXML
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class PreGameSetupController {

    @FXML
    private TextField playerNameField;

    @FXML
    private Button startButton;

    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        // playerNameField.requestFocus(); // (فعلا کامنت شده)

        System.out.println("PreGameSetupController initialized.");
    }


    @FXML
    void handleStartButton(ActionEvent event) {
        String playerName = playerNameField.getText().trim();

        if (playerName.isEmpty()) {
            System.out.println("Start button clicked, but player name is empty!");

        } else {
            System.out.println("Start button clicked! Player name: " + playerName);
        }
    }

    @FXML
    void handleBackButton(ActionEvent event) {
        System.out.println("Back button clicked!");
        }

}
