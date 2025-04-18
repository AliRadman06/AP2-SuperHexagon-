package com.example.ap2superhexagon;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class HistoryController {
    @FXML
    private VBox historyContainer;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    public void initialize() {
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        loadHistory();
    }

    private void loadHistory() {
        List<GameHistoryManager.GameRecord> history = GameHistoryManager.loadHistory();
        historyContainer.getChildren().clear();

        if (history.isEmpty()) {
            Label emptyLabel = new Label("No game history available");
            emptyLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20;");
            historyContainer.getChildren().add(emptyLabel);
            return;
        }

        for (GameHistoryManager.GameRecord record : history) {
            // ایجاد مستطیل برای هر رکورد
            Pane recordPane = new Pane();
            recordPane.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.2);" +
                            "-fx-background-radius: 30;" +
                            "-fx-border-radius: 30;" +
                            "-fx-border-color: rgba(255, 255, 255, 0.4);" +
                            "-fx-border-width: 2;"
            );
            recordPane.setPrefSize(460, 80);

            // محتوای رکورد
            VBox contentBox = new VBox(5);

            contentBox.setAlignment(Pos.CENTER); // این خط محتوا را در وسط قرار می‌دهد

            contentBox.setLayoutX(15);
            contentBox.setLayoutY(10);

            Label playerLabel = new Label("Player: " + record.getPlayerName());
            playerLabel.setStyle("-fx-text-fill: white; -fx-font-size: 13;");

            Label timeLabel = new Label("Time: " + record.getDuration());
            timeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 13;");

            Label dateLabel = new Label("Date: " + record.getDateTime());
            dateLabel.setStyle("-fx-text-fill: white; -fx-font-size: 13;");

            contentBox.getChildren().addAll(playerLabel, timeLabel, dateLabel);
            recordPane.getChildren().add(contentBox);

            historyContainer.getChildren().add(recordPane);
        }
    }

    @FXML
    private void handleBackButton(javafx.event.ActionEvent event) {
        AudioManager.playClickSound();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ap2superhexagon/main-menu-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}