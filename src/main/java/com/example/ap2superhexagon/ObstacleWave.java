package com.example.ap2superhexagon;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ObstacleWave {

    private final int[] pattern;
    private List<ObstacleWall> walls;
    private double distanceFromCenter;
    private final double speed;
    private boolean markedForRemoval = false;


    public ObstacleWave(int[] pattern, double initialDistance, double speed, double centerX, double centerY, double wallWidth) {

        Objects.requireNonNull(pattern, "Pattern cannot be null.");
        if (pattern.length != 6) {
            throw new IllegalArgumentException("Pattern must be an array of length 6.");
        }
        if (initialDistance <= 0) {
            throw new IllegalArgumentException("Initial distance must be positive.");
        }
        if (speed <= 0) {
            throw new IllegalArgumentException("Speed must be positive.");
        }
        if (wallWidth <= 0) {
            throw new IllegalArgumentException("Wall width must be positive.");
        }

        this.pattern = pattern;
        this.distanceFromCenter = initialDistance;
        this.speed = speed;
        this.walls = new ArrayList<>();

        for (int i = 0; i < pattern.length; i++) {
            if (pattern[i] == 1) {
                double angle = i * 60.0 + 30.0;
                ObstacleWall wall = new ObstacleWall(angle, wallWidth);
                wall.updateShape(this.distanceFromCenter, centerX, centerY);
                this.walls.add(wall);
            }
        }
    }

    public ObstacleWave(int[] pattern, double initialDistance, double speed, double centerX, double centerY) {
        this(pattern, initialDistance, speed, centerX, centerY, Constants.DEFAULT_WALL_WIDTH);
    }

    public void update(double deltaTime, double centerX, double centerY) {

        if (this.markedForRemoval) {
            return;
        }
            this.distanceFromCenter -= this.speed * deltaTime;

            if (this.distanceFromCenter < Constants.REMOVAL_THRESHOLD_RADIUS) {
                this.markedForRemoval = true;
                return;
            }

            for (ObstacleWall wall : this.walls) {
                wall.updateShape(this.distanceFromCenter, centerX, centerY);
            }


    }

    public List<Node> getWallShapeNodes() {
        List<Node> shapes = new ArrayList<>(walls.size());
        for (ObstacleWall wall : walls) {
            shapes.add(wall.getShapeNode());
        }
        return shapes;
    }


    public List<ObstacleWall> getWalls() {
        return List.copyOf(this.walls);
    }


    public boolean isMarkedForRemoval() {
        return this.markedForRemoval;
    }


    public void setColor(Color color) {
        for (ObstacleWall wall : this.walls) {
            wall.setColor(color);
        }
    }

    public int[] getPattern() {
        return java.util.Arrays.copyOf(pattern, pattern.length);
    }

    public double getDistanceFromCenter() {
        return distanceFromCenter;
    }
}
