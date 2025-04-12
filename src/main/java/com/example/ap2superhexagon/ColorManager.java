package com.example.ap2superhexagon;

import javafx.scene.paint.Color;
import java.util.Arrays;
import java.util.List;

public class ColorManager {

    // کلاس داخلی برای نگهداری یک پالت رنگ
    public static class ColorPalette { // عمومی می‌کنیم تا GameController هم بهش دسترسی داشته باشه (اگر لازم شد)
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

    // تعریف پالت‌ها
    private static final ColorPalette GRAY_PALETTE = new ColorPalette(
            Color.rgb(40, 40, 40), Color.rgb(55, 55, 55),
            Color.DARKGRAY, Color.WHITE, Color.LIGHTGRAY
    );

    private static final ColorPalette BLUE_PALETTE = new ColorPalette(
            Color.NAVY, Color.rgb(0, 0, 100),
            Color.BLUE, Color.CYAN, Color.LIGHTBLUE
    );

    private static final ColorPalette GREEN_PALETTE = new ColorPalette(
            Color.DARKGREEN, Color.rgb(0, 80, 0),
            Color.GREEN, Color.PALEGREEN, Color.LIMEGREEN
    );

    // --- می‌توانید پالت‌های بیشتری اینجا تعریف کنید ---
    // private static final ColorPalette RED_PALETTE = new ColorPalette(...);


    public ColorManager() {
        // لیست پالت‌هایی که می‌خواهیم استفاده کنیم
        palettes = Arrays.asList(
                GRAY_PALETTE,
                BLUE_PALETTE,
                GREEN_PALETTE
                // RED_PALETTE // اگر اضافه کردید
        );
        currentPaletteIndex = 0; // شروع با پالت اول
    }

    // متد برای رفتن به پالت بعدی به صورت مرتب
    public void nextPalette() {
        currentPaletteIndex = (currentPaletteIndex + 1) % palettes.size();
        System.out.println("Switched to Palette index: " + currentPaletteIndex); // برای دیباگ
    }

    // متد برای گرفتن پالت فعلی (ممکن است مفید باشد)
    public ColorPalette getCurrentPalette() {
        return palettes.get(currentPaletteIndex);
    }


    // --- متدهای کمکی برای گرفتن رنگ‌های خاص از پالت فعلی ---

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
