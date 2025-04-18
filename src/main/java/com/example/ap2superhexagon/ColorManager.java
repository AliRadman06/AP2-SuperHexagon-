package com.example.ap2superhexagon;

import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.List;

public class ColorManager {

    public static class ColorPalette {
        final Color background1;
        final Color background2;
        final Color hexagonColor;
        final Color playerColor;
        final Color obstacleColor;

        ColorPalette(Color bg1, Color bg2, Color hexagon, Color player, Color obstacle) {
            this.background1 = bg1;
            this.background2 = bg2;
            this.hexagonColor = hexagon;
            this.playerColor = player;
            this.obstacleColor = obstacle;
        }
    }

    private final List<ColorPalette> palettes;
    private int currentPaletteIndex;

    private static final ColorPalette GRAY_PALETTE = new ColorPalette(
            Color.rgb(30, 30, 30),
            Color.rgb(50, 50, 50),
            Color.rgb(90, 90, 90),
            Color.rgb(255, 255, 255),
            Color.rgb(90, 90, 90)
    );

    private static final ColorPalette GREEN_PALETTE = new ColorPalette(
            Color.rgb(15, 40, 15),
            Color.rgb(25, 60, 25),
            Color.rgb(50, 110, 50),
            Color.rgb(255, 255, 255),
            Color.rgb(50, 110, 50)
    );

    private static final ColorPalette BLUE_PALETTE = new ColorPalette(
            Color.rgb(10, 10, 50),
            Color.rgb(20, 20, 80),
            Color.rgb(50, 50, 150),
            Color.rgb(255, 255, 255),
            Color.rgb(50, 50, 150)
    );

    private static final ColorPalette BLUE_PALETTE2 = new ColorPalette(
            Color.rgb(10, 30, 45),
            Color.rgb(15, 45, 65),
            Color.rgb(30, 90, 120),
            Color.rgb(255, 255, 255),
            Color.rgb(30, 90, 120)
    );

    public ColorManager() {
        palettes = Arrays.asList(

                BLUE_PALETTE,
                GRAY_PALETTE,
                BLUE_PALETTE2,
                GREEN_PALETTE

        );
        currentPaletteIndex = 0;
    }

    public void nextPalette() {
        currentPaletteIndex = (currentPaletteIndex + 1) % palettes.size();
        System.out.println("Switched to Palette index: " + currentPaletteIndex);
    }


    public ColorPalette getCurrentPalette() {
        return palettes.get(currentPaletteIndex);
    }


    public Color getBackgroundColor1() {
        return getCurrentPalette().background1;
    }

    public Color getBackgroundColor2() {
        return getCurrentPalette().background2;
    }

    public Color getObstacleColor() {
        return getCurrentPalette().obstacleColor;
    }

    public Color getPlayerColor() {
        return getCurrentPalette().playerColor;
    }

    public Color getHexagonColor() {
        return getCurrentPalette().hexagonColor;
    }
}
