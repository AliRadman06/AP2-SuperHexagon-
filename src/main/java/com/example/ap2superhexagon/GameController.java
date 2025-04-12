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
    private ColorManager colorManager = new ColorManager();



    @FXML
    public void initialize() {
        // اینجا میتونی کارهای اولیه رو انجام بدی، مثلا مقدار اولیه امتیازها رو ست کنی
        System.out.println("GameController initialized!");
        scoreLabel.setText("0"); // مقدار اولیه امتیاز
        // bestScoreLabel رو هم می‌تونی از جایی بخونی و ست کنی
        // bestScoreLabel.setText(loadBestScore());
        currentPlayerSide = 0;

        createBackgroundSections();

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


    private void createBackgroundSections() {
        // دریافت ابعاد Pane برای محاسبه مرکز و شعاع
        // این مقادیر باید در زمان فراخوانی این متد معتبر باشند
        // معمولاً بعد از اینکه Stage نمایش داده شده یا با Listener ها
        double paneWidth = gamePane.getWidth();
        double paneHeight = gamePane.getHeight();

        // اگر ابعاد هنوز 0 است (مثلاً قبل از نمایش)، از مقادیر پیش‌فرض یا Constants استفاده کنید
        // یا بهتر است این متد بعد از مشخص شدن ابعاد فراخوانی شود.
        if (paneWidth <= 0 || paneHeight <= 0) {
            paneWidth = Constants.SCREEN_WIDTH; // یا مقدار پیش‌فرض دیگر
            paneHeight = Constants.SCREEN_HEIGHT;
            System.out.println("Warning: Pane dimensions not available, using defaults for background.");
        }


        double centerX = paneWidth / 2.0;
        double centerY = paneHeight / 2.0;

        // شعاع بزرگ برای اطمینان از پوشش کامل صفحه
        // استفاده از قطر یا حداکثر بعد ضربدر یک ضریب اطمینان
        double radius = 2000; // فاصله مرکز تا گوشه + 10% اضافه
        // یا: double radius = Math.max(paneWidth, paneHeight) * 1.1;
        // یا: double radius = 2000; // یک عدد خیلی بزرگ ثابت


        // پاک کردن بخش‌های قبلی اگر وجود دارند (برای مواقعی که ممکن است دوباره فراخوانی شود)
        gamePane.getChildren().removeIf(node -> node.getStyleClass().contains("background-section"));


        for (int i = 0; i < 6; i++) {
            // محاسبه زوایای شروع و پایان هر بخش به رادیان
            double angle1 = Math.toRadians(i * 60.0); // زاویه شروع
            double angle2 = Math.toRadians((i + 1) * 60.0); // زاویه پایان

            // محاسبه مختصات نقاط بیرونی
            double x1 = centerX + radius * Math.cos(angle1);
            double y1 = centerY + radius * Math.sin(angle1);
            double x2 = centerX + radius * Math.cos(angle2);
            double y2 = centerY + radius * Math.sin(angle2);

            // ایجاد Polygon مثلثی شکل
            Polygon section = new Polygon();
            section.getPoints().addAll(
                    centerX, centerY, // راس مرکزی
                    x1, y1,          // نقطه بیرونی اول
                    x2, y2           // نقطه بیرونی دوم
            );

            // تعیین رنگ یکی در میان از ColorManager
            Color fillColor = (i % 2 == 0) ? colorManager.getBackgroundColor1() : colorManager.getBackgroundColor2();
            section.setFill(fillColor);
            section.setStroke(null); // بدون خط دور

            // اضافه کردن کلاس استایل برای شناسایی راحت‌تر بعداً
            section.getStyleClass().add("background-section");

            // اضافه کردن بخش به Pane (زیر سایر عناصر مانند بازیکن و موانع)
            gamePane.getChildren().add(0, section); // اضافه کردن در اندیس 0 تا زیر بقیه قرار گیرد
        }
    }


}