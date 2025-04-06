package com.example.ap2superhexagon;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class GameController {
    @FXML
    private Pane gamePane; // برای قرار دادن عناصر بازی

    @FXML
    private Label scoreLabel; // برای نمایش امتیاز فعلی

    @FXML
    private Label bestScoreLabel; // برای نمایش بهترین رکورد

    @FXML
    private Button pauseButton; // برای دکمه Pause

    // این متد به صورت خودکار بعد از لود شدن FXML اجرا میشه
    @FXML
    public void initialize() {
        // اینجا میتونی کارهای اولیه رو انجام بدی، مثلا مقدار اولیه امتیازها رو ست کنی
        System.out.println("GameController initialized!");
        scoreLabel.setText("0"); // مقدار اولیه امتیاز
        // bestScoreLabel رو هم می‌تونی از جایی بخونی و ست کنی
        // bestScoreLabel.setText(loadBestScore());
    }

    // *** این متد برای onAction دکمه Pause لازمه ***
    // اسم متد باید دقیقا همونی باشه که در FXML گذاشتی (handlePauseButton)
    // میتونه پارامتر ActionEvent بگیره یا نگیره، ولی باید public باشه
    @FXML
    public void handlePauseButton(ActionEvent event) {
        // فعلا فقط یک پیغام چاپ می‌کنیم تا مطمئن شیم کار می‌کنه
        System.out.println("Pause Button Clicked!");

        // TODO: منطق Pause/Resume بازی رو اینجا پیاده‌سازی کن
        // مثلا:
        // if (game.isPlaying()) {
        //     game.pause();
        //     // شاید بخوای یک صفحه Pause نشون بدی یا دکمه رو عوض کنی
        // } else {
        //     game.resume();
        //     // برگردوندن به حالت بازی
        // }
    }

}
