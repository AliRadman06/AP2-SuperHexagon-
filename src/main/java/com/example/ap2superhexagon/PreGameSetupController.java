package com.example.ap2superhexagon;

import javafx.event.ActionEvent; // برای مدیریت رویداد کلیک
import javafx.fxml.FXML;        // برای اتصال به عناصر FXML
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
            playerNameField.setStyle("-fx-border-color: red; -fx-background-radius: 30; -fx-border-radius: 30;");
        } else {
            System.out.println("Start button clicked! Player name: " + playerName);
            playerNameField.setStyle("-fx-background-radius: 30;");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ap2superhexagon/game-view.fxml"));
                Parent gameRoot = loader.load();
                Scene gameScene = new Scene(gameRoot, 1024, 576);
                String cssPath = getClass().getResource("/css/game-style.css").toExternalForm();
                if (cssPath != null) {
                    gameScene.getStylesheets().add(cssPath);
                    System.out.println("Game CSS loaded.");
                } else {
                    System.err.println("Could not find game-styles.css!");
                }
                Stage stage = (Stage) startButton.getScene().getWindow();
                stage.setScene(gameScene);
                stage.show();
            }
            catch (Exception e) {
                System.err.println("Error loading game or CSS: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    void handleBackButton(ActionEvent event) {
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
}
