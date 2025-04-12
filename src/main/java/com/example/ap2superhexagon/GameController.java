package com.example.ap2superhexagon;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class GameController {
    @FXML
    private Pane gamePane; // برای قرار دادن عناصر بازی
    @FXML
    private Label scoreLabel; // برای نمایش امتیاز فعلی
    @FXML
    private Label bestScoreLabel; // برای نمایش بهترین رکورد
    @FXML
    private Button pauseButton; // برای دکمه Pause
//    private double currentPlayerAngleDegrees = 90.0;
    private Polygon centralHexagon;
    private Polygon playerTriangle;
    private int currentPlayerSide = 0;
    private String currentPlayerName;


    @FXML
    public void initialize() {
        // اینجا میتونی کارهای اولیه رو انجام بدی، مثلا مقدار اولیه امتیازها رو ست کنی
        System.out.println("GameController initialized!");
        scoreLabel.setText("0"); // مقدار اولیه امتیاز
        // bestScoreLabel رو هم می‌تونی از جایی بخونی و ست کنی
        // bestScoreLabel.setText(loadBestScore());
        currentPlayerSide = 0;

        centralHexagon = createHexagon(Constants.CENTER_X, Constants.CENTER_Y, Constants.HEXAGON_RADIUS);
        centralHexagon.setFill(null);
        centralHexagon.setStroke(Color.WHITE);
        centralHexagon.setStrokeWidth(3.0);
        gamePane.getChildren().add(centralHexagon);

        playerTriangle = createPlayerTriangle(Constants.PLAYER_SIZE);
        playerTriangle.setFill(Color.CYAN);
        updatePlayerTrianglePosition();
        gamePane.getChildren().add(playerTriangle);

        gamePane.setFocusTraversable(true);
        gamePane.setOnKeyPressed(this::handleKeyPress);

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

    private Polygon createHexagon(double centerX, double centerY, double radius) {
        Polygon hexagon = new Polygon();
        for (int i = 0; i < Constants.SIDES; i++) {
            double angle = Math.toRadians((360.0 / Constants.SIDES) * i -(90.0 - (180 / Constants.SIDES)));
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);
            hexagon.getPoints().addAll(x, y);
        }
        return hexagon;
    }



    private Polygon createPlayerTriangle(double size) {
        Polygon playerTriangle = new Polygon();
        playerTriangle.getPoints().addAll(
                0.0, -size * Math.sqrt(3) / 3.0,
                -size / 2.0, size * Math.sqrt(3) / 6.0,
                size / 2.0, size * Math.sqrt(3) / 6.0
        );
        return playerTriangle;
    }

    private void updatePlayerTrianglePosition() {
//        double angle = Math.toRadians((360.0 / Constants.SIDES) * currentPlayerSide + 90);
//        double angleRadians = Math.toRadians(currentPlayerAngleDegrees);

        double angleStep = 360.0 / Constants.SIDES;
        double baseAngleDegrees = -90.0;
        double targetAngleDegrees = baseAngleDegrees + angleStep * currentPlayerSide;
        double targetAngleRadians = Math.toRadians(targetAngleDegrees);


        double playerX = Constants.CENTER_X + Constants.PLAYER_DISTANCE_FROM_CENTER * Math.cos(targetAngleRadians);
        double playerY = Constants.CENTER_Y + Constants.PLAYER_DISTANCE_FROM_CENTER * Math.sin(targetAngleRadians);

        playerTriangle.setTranslateX(playerX);
        playerTriangle.setTranslateY(playerY);

        playerTriangle.setRotate(targetAngleDegrees + 90);
        System.out.println("Moved to Side: " + currentPlayerSide + ", Calculated Angle: " + targetAngleDegrees);

    }



    private void handleKeyPress(KeyEvent event) {
        System.out.println("Key pressed: " + event.getCode());
        boolean moved = false;

        if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
            currentPlayerSide = (currentPlayerSide - 1 + Constants.SIDES) % Constants.SIDES;
            moved = true;
//            rotatePlayer(false);
        }
        else if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
            currentPlayerSide = (currentPlayerSide + 1) % Constants.SIDES;
            moved = true;
//            rotatePlayer(true);
        }

        if (moved) {
            updatePlayerTrianglePosition();
        }
    }

//    private void rotatePlayer(boolean clockwise) {
//
//        final double angleStep = 360.0 / Constants.SIDES;
//        if (clockwise) {
//            currentPlayerAngleDegrees -= angleStep;
//        }
//        else {
//            currentPlayerAngleDegrees += angleStep;
//        }
//
//        while (currentPlayerAngleDegrees < 0) {
//            currentPlayerAngleDegrees += 360;
//        }
//        currentPlayerAngleDegrees %= 360;
//
//        updatePlayerTrianglePosition();
//    }



}