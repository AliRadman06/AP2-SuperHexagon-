package com.example.ap2superhexagon;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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
        // اینجا میتونی مقدار اولیه bestRecordLabel رو از فایل یا جای دیگه بخونی و ست کنی
        long currentHighScore = loadHighScore(); // متد فرضی
        bestRecordLabel.setText(String.valueOf(currentHighScore));
    }

    @FXML
    void handleStartButton(ActionEvent event) {
        System.out.println("Start button clicked!");
        // کد رفتن به صفحه بازی
    }

    @FXML
    void handleHistoryButton(ActionEvent event) {
        System.out.println("History button clicked!");
        // کد رفتن به صفحه تاریخچه
    }

    @FXML
    void handleSettingButton(ActionEvent event) {
        System.out.println("Setting button clicked!");
        // کد رفتن به صفحه تنظیمات
    }

    @FXML
    void handleExitButton(ActionEvent event) {
        System.out.println("Exit button clicked!");
        // کد خروج از برنامه
        javafx.application.Platform.exit();
    }

    private long loadHighScore() {
        // فعلا یک مقدار ثابت برمیگردونیم
        // بعدا باید کد خواندن از فایل رو اینجا بنویسی
        return 12345; // مثال
    }
}
