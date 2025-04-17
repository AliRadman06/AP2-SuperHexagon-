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
            Color.rgb(30, 30, 30),    // پس‌زمینه ۱: خیلی تیره
            Color.rgb(50, 50, 50),    // پس‌زمینه ۲: کمی روشن‌تر
            Color.rgb(90, 90, 90),    // شش‌ضلعی: طوسی متوسط
            Color.rgb(255, 255, 255), // بازیکن: سفید
            Color.rgb(90, 90, 90)     // مانع: هم‌رنگ شش‌ضلعی
    );

    private static final ColorPalette GREEN_PALETTE = new ColorPalette(
            Color.rgb(15, 40, 15),    // پس‌زمینه ۱: سبز خیلی تیره
            Color.rgb(25, 60, 25),    // پس‌زمینه ۲: سبز تیره‌تر
            Color.rgb(50, 110, 50),   // شش‌ضلعی: سبز متوسط
            Color.rgb(255, 255, 255), // بازیکن: سفید
            Color.rgb(50, 110, 50)    // مانع: هم‌رنگ شش‌ضلعی
    );

    private static final ColorPalette BLUE_PALETTE = new ColorPalette(
            Color.rgb(10, 10, 50),    // پس‌زمینه ۱: آبی خیلی تیره
            Color.rgb(20, 20, 80),    // پس‌زمینه ۲: آبی تیره‌تر
            Color.rgb(50, 50, 150),   // شش‌ضلعی: آبی متوسط
            Color.rgb(255, 255, 255), // بازیکن: سفید
            Color.rgb(50, 50, 150)    // مانع: هم‌رنگ شش‌ضلعی
    );

    private static final ColorPalette BLUE_PALETTE2 = new ColorPalette(
            Color.rgb(10, 30, 45),    // پس‌زمینه ۱: آبی-سبز خیلی تیره
            Color.rgb(15, 45, 65),    // پس‌زمینه ۲: آبی-سبز تیره‌تر
            Color.rgb(30, 90, 120),   // شش‌ضلعی: آبی-سبز متوسط
            Color.rgb(255, 255, 255), // بازیکن: سفید
            Color.rgb(30, 90, 120)    // مانع: هم‌رنگ شش‌ضلعی
    );

    // --- می‌توانید پالت‌های بیشتری اینجا تعریف کنید ---
    // private static final ColorPalette RED_PALETTE = new ColorPalette(...);


    public ColorManager() {
        // لیست پالت‌هایی که می‌خواهیم استفاده کنیم
        palettes = Arrays.asList(
                GRAY_PALETTE,
                BLUE_PALETTE,
                GREEN_PALETTE,
                BLUE_PALETTE2
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
