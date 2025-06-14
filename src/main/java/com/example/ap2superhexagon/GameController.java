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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameController {

    @FXML
    private Pane gamePane;
    @FXML
    private Label scoreLabel;
    @FXML
    private Button pauseButton;
    @FXML
    private Label yourRecordLabel;

    private List<ObstacleWave> activeWaves;
    private Random randomGenerator;
    private double obstacleSpawnTimer;
    private double obstacleSpawnInterval = Constants.INITIAL_OBSTACLE_SPAWN_INTERVAL;
    private double currentObstacleSpeed = Constants.INITIAL_OBSTACLE_SPEED;
    private boolean gameOver = false;
    private long lastUpdateTimeNanos = 0;
    private Polygon centralHexagon;
    private Polygon playerTriangle;
    private int currentPlayerSide = 0;
    private String currentPlayerName;
    private ColorManager colorManager = new ColorManager();
    private GameLoop gameLoop;
    private long lastPaletteSwitchTime = 0;
    private final long paletteSwitchInterval = Constants.PALETTE_SWITCH_INTERVAL_SECONDS ;

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
    private long gameStartTime;
    private AnimationTimer timer;

    @FXML
    public void initialize() {
        AudioManager.playGameTheme();
        scoreLabel.setText("0.0");
        currentPlayerSide = 0;
        gameStartTime = System.currentTimeMillis();
        this.activeWaves = new ArrayList<>();
        this.randomGenerator = new Random();
        this.obstacleSpawnTimer = this.obstacleSpawnInterval;
        this.currentObstacleSpeed = Constants.INITIAL_OBSTACLE_SPEED;
        this.gameOver = false;
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
        setupTimer();

    }


    private void setupTimer() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long elapsedMillis = System.currentTimeMillis() - gameStartTime;
                double seconds = elapsedMillis / 1000.0;
                scoreLabel.setText(String.format("%.1f", seconds));
            }
        };
        timer.start();
    }

    private void update (long now) {
        if (lastUpdateTimeNanos == 0) {
            lastUpdateTimeNanos = now;
            return;
        }
        double deltaTime = (now - lastUpdateTimeNanos) / 1_000_000_000.0;
        lastUpdateTimeNanos = now;
        timeSinceGameStart += deltaTime;
        if ((int)(timeSinceGameStart / Constants.SPEED_INCREASE_INTERVAL) >
                (int)((timeSinceGameStart - deltaTime) / Constants.SPEED_INCREASE_INTERVAL)) {
            currentObstacleSpeed += Constants.SPEED_INCREMENT;
            obstacleSpawnInterval = Math.max(Constants.MIN_OBSTACLE_SPAWN_INTERVAL, obstacleSpawnInterval - Constants.SPAWN_INTERVAL_DECREMENT);
        }

        long intervalNanos = paletteSwitchInterval * 1_000_000_000L;
        if (now - lastPaletteSwitchTime > intervalNanos) {
            colorManager.nextPalette();
            updateElementColors();
            lastPaletteSwitchTime = now;
        }

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

            for (Node wallNode : wave.getWallShapeNodes()) {
                gamePane.getChildren().add(wallNode);
            }

            wave.setColor(colorManager.getObstacleColor());
            activeWaves.add(wave);

            obstacleSpawnTimer = obstacleSpawnInterval;
        }

        List<ObstacleWave> toRemove = new ArrayList<>();
        for (ObstacleWave wave : activeWaves) {
            wave.update(deltaTime, Constants.CENTER_X, Constants.CENTER_Y);

            if (wave.isMarkedForRemoval()) {
                toRemove.add(wave);
            }
        }

        for (ObstacleWave wave : toRemove) {
            for (Node node : wave.getWallShapeNodes()) {
                gamePane.getChildren().remove(node);
            }
            activeWaves.remove(wave);
        }

        if (!gameOver && checkCollisionWithWalls()) {
            gameOver = true;
            AudioManager.stopGameplayMusic();
            AudioManager.playGameOver();
            long elapsedMillis = System.currentTimeMillis() - gameStartTime;
            HighScoreManager.saveIfNewHighScore(currentPlayerName, elapsedMillis);
            stopGame();
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    public void handlePauseButton(ActionEvent event) {
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
        double angleStep = 360.0 / Constants.SIDES;
        double baseAngleDegrees = -90.0;
        double targetAngleDegrees = baseAngleDegrees + angleStep * currentPlayerSide;
        double targetAngleRadians = Math.toRadians(targetAngleDegrees);
        double playerX = Constants.CENTER_X + Constants.PLAYER_DISTANCE_FROM_CENTER * Math.cos(targetAngleRadians);
        double playerY = Constants.CENTER_Y + Constants.PLAYER_DISTANCE_FROM_CENTER * Math.sin(targetAngleRadians);
        playerTriangle.setTranslateX(playerX);
        playerTriangle.setTranslateY(playerY);
        playerTriangle.setRotate(targetAngleDegrees + 90);
    }



    private void handleKeyPress(KeyEvent event) {
        boolean moved = false;
        if (!gameOver) {
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

        double paneWidth = gamePane.getWidth()  ;
        double paneHeight = gamePane.getHeight()  ;

        if (paneWidth <= 0 || paneHeight <= 0) {
            paneWidth = Constants.SCREEN_WIDTH;
            paneHeight = Constants.SCREEN_HEIGHT;
            System.out.println("Warning: Pane dimensions not available, using defaults for background.");
        }

        double centerX = paneWidth / 2.0;
        double centerY = paneHeight / 2.0;
        double radius = 2000;

        gamePane.getChildren().removeIf(node -> node.getStyleClass().contains("background-section"));

        for (int i = 0; i < 6; i++) {

            double angle1 = Math.toRadians(i * 60.0);
            double angle2 = Math.toRadians((i + 1) * 60.0);
            double x1 = centerX + radius * Math.cos(angle1);
            double y1 = centerY + radius * Math.sin(angle1);
            double x2 = centerX + radius * Math.cos(angle2);
            double y2 = centerY + radius * Math.sin(angle2);
            Polygon section = new Polygon();
            section.getPoints().addAll(
                    centerX, centerY,
                    x1, y1,
                    x2, y2
            );
            Color fillColor = (i % 2 == 0) ? colorManager.getBackgroundColor1() : colorManager.getBackgroundColor2();
            section.setFill(fillColor);
            section.setStroke(null);
            section.getStyleClass().add("background-section");
            gamePane.getChildren().add(0, section);
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

    public void stopGame() {
        if (gameLoop != null && gameLoop.isRunning()) {
            gameLoop.stop();
        }
        if (timer != null) {
            timer.stop();
        }
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
