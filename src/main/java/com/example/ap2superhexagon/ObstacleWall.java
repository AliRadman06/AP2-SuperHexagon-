package com.example.ap2superhexagon;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class ObstacleWall {

    private Polygon shape;
    private final double angleDegrees;
    private final double wallWidth;


    private static final double SECTOR_ANGLE_DEGREES = 60.0;

    private static final double HALF_SECTOR_ANGLE_RAD = Math.toRadians(SECTOR_ANGLE_DEGREES / 2.0);

    public ObstacleWall(double angleDegrees, double wallWidth) {
        if (wallWidth <= 0) {
            throw new IllegalArgumentException("Wall width must be positive.");
        }
        this.angleDegrees = angleDegrees;
        this.wallWidth = wallWidth;


        this.shape = new Polygon();

        this.shape.setFill(Color.DARKGRAY);
        this.shape.getStyleClass().add("obstacle-wall");
    }


    public void updateShape(double distanceFromCenter, double centerX, double centerY) {

        double innerRadius = distanceFromCenter - this.wallWidth / 2.0;
        double outerRadius = distanceFromCenter + this.wallWidth / 2.0;

        if (innerRadius < 0) {
            innerRadius = 0;
        }

        if (outerRadius < 0) {
            outerRadius = 0;
        }

        double centerAngleRad = Math.toRadians(this.angleDegrees);

        double angleLeftRad = centerAngleRad - HALF_SECTOR_ANGLE_RAD;
        double angleRightRad = centerAngleRad + HALF_SECTOR_ANGLE_RAD;

        double x1 = centerX + innerRadius * Math.cos(angleLeftRad);
        double y1 = centerY + innerRadius * Math.sin(angleLeftRad);

        double x2 = centerX + outerRadius * Math.cos(angleLeftRad);
        double y2 = centerY + outerRadius * Math.sin(angleLeftRad);

        double x3 = centerX + outerRadius * Math.cos(angleRightRad);
        double y3 = centerY + outerRadius * Math.sin(angleRightRad);

        double x4 = centerX + innerRadius * Math.cos(angleRightRad);
        double y4 = centerY + innerRadius * Math.sin(angleRightRad);

        shape.getPoints().setAll(x1, y1, x2, y2, x3, y3, x4, y4);
    }

    public Node getShapeNode() {
        return this.shape;
    }

    public Polygon getPolygon() {
        return this.shape;
    }

    public void setColor(Color color) {
        if (this.shape != null) {
            this.shape.setFill(color);
        }
    }
}
