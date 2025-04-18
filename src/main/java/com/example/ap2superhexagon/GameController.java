package com.example.ap2superhexagon;

import com.almasb.fxgl.animation.Animatable;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameController {
    @FXML
    private Pane gamePane; // برای قرار دادن عناصر بازی
    @FXML
    private Label scoreLabel; // برای نمایش امتیاز فعلی
    @FXML
    private Label bestScoreLabel; // برای نمایش بهترین رکورد
    @FXML
    private Button pauseButton; // برای دکمه Pause

    @FXML
    private Label bestRecordLabel; // برای نمایش امتیاز فعلی
    @FXML
    private Label yourRecordLabel; // برای نمایش بهترین رکورد



    // --- متغیرهای جدید برای مدیریت موانع ---
    private List<ObstacleWave> activeWaves; // لیستی برای نگهداری موج‌های فعال
    private Random randomGenerator;        // برای تولید الگوهای تصادفی
    private double obstacleSpawnTimer;     // تایمر برای زمان‌بندی ایجاد موج بعدی
    private double obstacleSpawnInterval = Constants.INITIAL_OBSTACLE_SPAWN_INTERVAL; // زمان بین ایجاد دو موج (ثانیه) - از Constants بخوانید

    private double currentObstacleSpeed = Constants.INITIAL_OBSTACLE_SPEED; // سرعت حرکت موج‌ها (پیکسل بر ثانیه) - از Constants بخوانید
    private boolean gameOver = false; // وضعیت پایان بازی (اگر از قبل ندارید)

    // --- متغیر برای محاسبه deltaTime ---
    private long lastUpdateTimeNanos = 0; // برای محاسبه دقیق زمان بین فریم ها

    private Polygon centralHexagon;
    private Polygon playerTriangle;
    private int currentPlayerSide = 0;
    private String currentPlayerName;
    private ColorManager colorManager = new ColorManager();
    private GameLoop gameLoop;
    private long lastPaletteSwitchTime = 0;
    private final long paletteSwitchInterval = Constants.PALETTE_SWITCH_INTERVAL_SECONDS ;
//    private PreGameSetupController preGameSetupController = new PreGameSetupController();


    private final List<int[]> predefinedPatterns = List.of(
            new int[]{1,0,1,1,0,1},
            new int[]{1,1,0,1,1,0},
            new int[]{0,1,1,0,1,1},
            new int[]{0,1,1,1,1,1},
            new int[]{1,0,1,1,1,1},
            new int[]{1,1,0,1,1,1},
            new int[]{1,1,1,0,1,1},
            new int[]{1,1,1,1,0,1},
            new int[]{1,1,1,1,1,0}
    );

    private double timeSinceGameStart = 0.0;



    @FXML
    public void initialize() {
        AudioManager.playGameTheme(); // شروع موزیک گیم‌پلی

        // اینجا میتونی کارهای اولیه رو انجام بدی، مثلا مقدار اولیه امتیازها رو ست کنی
//        System.out.println("GameController initialized!");
        scoreLabel.setText("0"); // مقدار اولیه امتیاز
        // bestScoreLabel رو هم می‌تونی از جایی بخونی و ست کنی
        // bestScoreLabel.setText(loadBestScore());
        currentPlayerSide = 0;

        this.activeWaves = new ArrayList<>();
        this.randomGenerator = new Random();
        this.obstacleSpawnTimer = this.obstacleSpawnInterval;
        this.currentObstacleSpeed = Constants.INITIAL_OBSTACLE_SPEED; // سرعت اولیه
        this.gameOver = false; // اطمینان از شروع بازی با gameOver = false
//        this.score = 0; // اگر متغیر score دارید، ریست کنید





        createBackgroundSections();

        centralHexagon = createHexagon(Constants.CENTER_X, Constants.CENTER_Y, Constants.HEXAGON_RADIUS);
        centralHexagon.setFill(colorManager.getBackgroundColor1());
        centralHexagon.setStroke(colorManager.getHexagonColor());
        centralHexagon.setStrokeWidth(3.0);
        centralHexagon.getStyleClass().add("central-hexagon");
        gamePane.getChildren().add(centralHexagon);

        playerTriangle = createPlayerTriangle(Constants.PLAYER_SIZE);
        playerTriangle.setFill(colorManager.getPlayerColor());
        playerTriangle.getStyleClass().add("player-triangle");
        updatePlayerTrianglePosition();
        gamePane.getChildren().add(playerTriangle);

        gameLoop = new GameLoop(this::update);

        gamePane.setFocusTraversable(true);
        gamePane.setOnKeyPressed(this::handleKeyPress);

        gameLoop.start();

    }

    private void update (long now) {

        // 1. محاسبه deltaTime (بر حسب ثانیه)
        if (lastUpdateTimeNanos == 0) {
            lastUpdateTimeNanos = now;
            return;
        }
        double deltaTime = (now - lastUpdateTimeNanos) / 1_000_000_000.0;
        lastUpdateTimeNanos = now;

        timeSinceGameStart += deltaTime;


        // افزایش سرعت

        if ((int)(timeSinceGameStart / Constants.SPEED_INCREASE_INTERVAL) >
                (int)((timeSinceGameStart - deltaTime) / Constants.SPEED_INCREASE_INTERVAL)) {

            currentObstacleSpeed += Constants.SPEED_INCREMENT;
            obstacleSpawnInterval = Math.max(Constants.MIN_OBSTACLE_SPAWN_INTERVAL, obstacleSpawnInterval - Constants.SPAWN_INTERVAL_DECREMENT);

            System.out.println("🎯 Speed increased! Now: " + currentObstacleSpeed +
                    " | 🌀 SpawnInterval = " + obstacleSpawnInterval);
        }


// 2. آپدیت رنگ
        long intervalNanos = paletteSwitchInterval * 1_000_000_000L;
        if (now - lastPaletteSwitchTime > intervalNanos) {
            colorManager.nextPalette();
            updateElementColors();
            lastPaletteSwitchTime = now;
        }

        // 3. اسپاون مانع اگر تایمرش تموم شده
        obstacleSpawnTimer -= deltaTime;
        if (obstacleSpawnTimer <= 0) {
            int[] pattern = getRandomPattern();
            ObstacleWave wave = new ObstacleWave(
                    pattern,
                    Constants.SPAWN_DISTANCE,
                    currentObstacleSpeed,
                    Constants.CENTER_X,
                    Constants.CENTER_Y,
                    Constants.OBSTACLE_WALL_WIDTH
            );

            // اضافه به صفحه
            for (Node wallNode : wave.getWallShapeNodes()) {
                gamePane.getChildren().add(wallNode);
            }

            wave.setColor(colorManager.getObstacleColor());
            activeWaves.add(wave);

            obstacleSpawnTimer = obstacleSpawnInterval;
        }



// 4. آپدیت تمام موج‌ها
        List<ObstacleWave> toRemove = new ArrayList<>();
        for (ObstacleWave wave : activeWaves) {
            wave.update(deltaTime, Constants.CENTER_X, Constants.CENTER_Y);

            if (wave.isMarkedForRemoval()) {
                toRemove.add(wave);
            }
        }

// 5. حذف موج‌هایی که رسیدن به مرکز
        for (ObstacleWave wave : toRemove) {
            for (Node node : wave.getWallShapeNodes()) {
                gamePane.getChildren().remove(node);
            }
            activeWaves.remove(wave);
        }

//         بعد از برخورد یا اتمام بازی
//        long elapsedMillis = (long)(timeSinceGameStart * 1000);  // زمان بازی به میلی‌ثانیه
//        HighScoreManager.saveIfNewHighScore(elapsedMillis);  // ذخیره رکورد جدید در صورتی که بزرگتر از رکورد قبلی باشه

        if (!gameOver && checkCollisionWithWalls()) {
            gameOver = true;
            AudioManager.stopGameplayMusic();

            AudioManager.playGameOver();
            stopGame();

            long elapsedMillis = (long)(timeSinceGameStart * 1000);
            HighScoreManager.saveIfNewHighScore(currentPlayerName, elapsedMillis);
            if (GameHistoryManager.isHistoryEnabled()) {
                GameHistoryManager.saveGameRecord(currentPlayerName, elapsedMillis);
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ap2superhexagon/gameover-view.fxml"));
                Parent overlay = loader.load();

                GameoverView controller = loader.getController();
                controller.setBackgroundImage();
                controller.setFinalScore(currentPlayerName, elapsedMillis);

                gamePane.getChildren().add(overlay);

                pauseButton.setVisible(false);
                yourRecordLabel.setVisible(false);
                scoreLabel.setVisible(false);
                bestRecordLabel.setVisible(false);
                bestScoreLabel.setVisible(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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

        if (!gameOver) {  // 🔒 فقط در صورتی که بازی در حال اجراست، مثلث بچرخه
            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
                AudioManager.playRotateSound();
                currentPlayerSide = (currentPlayerSide - 1 + Constants.SIDES) % Constants.SIDES;
                moved = true;
            }
            else if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
                AudioManager.playRotateSound();
                currentPlayerSide = (currentPlayerSide + 1) % Constants.SIDES;
                moved = true;
            }

            if (moved) {
                updatePlayerTrianglePosition();
            }
        }
    }



    private void createBackgroundSections() {
        // دریافت ابعاد Pane برای محاسبه مرکز و شعاع
        // این مقادیر باید در زمان فراخوانی این متد معتبر باشند
        // معمولاً بعد از اینکه Stage نمایش داده شده یا با Listener ها
        double paneWidth = gamePane.getWidth()  ;
        double paneHeight = gamePane.getHeight()  ;

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
        double radius = 2000;

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

    private void updateElementColors() {
        int sectionIndex = 0;

        for (Node node : gamePane.getChildren()) {
            if(node.getStyleClass().contains("background-section") &&node instanceof Polygon) {
                Polygon section = (Polygon) node;
                Color fillColor = (sectionIndex % 2 == 0) ? colorManager.getBackgroundColor1() : colorManager.getBackgroundColor2();
                section.setFill(fillColor);
                sectionIndex++;
            }
        }
        if (centralHexagon != null) {
            centralHexagon.setStroke(colorManager.getHexagonColor());
            centralHexagon.setFill(colorManager.getBackgroundColor1());
        }

        if (playerTriangle != null) {
            playerTriangle.setFill(colorManager.getPlayerColor());
        }

        for (ObstacleWave wave : activeWaves) {
            wave.setColor(colorManager.getObstacleColor());
        }
    }

    public void startGame() {
        if (gameLoop != null && !gameLoop.isRunning()) {
            // اطمینان از فوکوس قبل از شروع لوپ
            Platform.runLater(() -> {
                gamePane.requestFocus();
                System.out.println("startGame: gamePane focus requested.");
                // شروع لوپ بعد از اطمینان از فوکوس
                gameLoop.start();
            });
        } else if (gameLoop == null) {
            System.err.println("Cannot start game, GameLoop is null!");
        } else {
            System.out.println("startGame called, but loop is already running.");
        }
    }

    public void stopGame() {
        if (gameLoop != null && gameLoop.isRunning()) {
            gameLoop.stop();
        }
    }

    @FXML
    private void handleBackButton() { // نام فرضی
        stopGame();
        // کدی برای بازگشت به منوی اصلی
        // MainApplication.getInstance().showMainMenu(); // یا روش مشابه
        System.out.println("Back button pressed, game stopped.");
    }


    private int[] getRandomPattern() {
        if (randomGenerator.nextDouble() < 0.5) {
            return predefinedPatterns.get(randomGenerator.nextInt(predefinedPatterns.size()));
        } else {
            int[] pattern = new int[6];
            int numGaps = 0;

            for (int i = 0; i < 6; i++) {
                pattern[i] = randomGenerator.nextBoolean() ? 1 : 0;
                if (pattern[i] == 0) numGaps++;
            }

            if (numGaps < 2) {
                int forcedGap1 = randomGenerator.nextInt(6);
                int forcedGap2 = (forcedGap1 + 1 + randomGenerator.nextInt(5)) % 6;
                pattern[forcedGap1] = 0;
                pattern[forcedGap2] = 0;
            }

            return pattern;
        }
    }


    private boolean checkCollisionWithWalls() {
        for (ObstacleWave wave : activeWaves) {
            for (ObstacleWall wall : wave.getWalls()) {
                Shape intersect = Shape.intersect(playerTriangle, (Shape) wall.getShapeNode());

                // اگه ناحیه اشتراک هندسی داشتن
                if (intersect.getBoundsInLocal().getWidth() > 0 &&
                        intersect.getBoundsInLocal().getHeight() > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setPlayerName(String playerName) {
        this.currentPlayerName = playerName;
    }

}