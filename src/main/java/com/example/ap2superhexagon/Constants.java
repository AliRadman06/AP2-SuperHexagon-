package com.example.ap2superhexagon;

public final class Constants { // 'final' یعنی نمیشه از این کلاس ارث‌بری کرد

    // جلوگیری از ساختن شیء از این کلاس (چون فقط شامل ثابت هست)
    private Constants() {}

    // --- ابعاد صفحه و مرکز ---
    public static final double SCREEN_WIDTH = 1024.0;
    public static final double SCREEN_HEIGHT = 576.0;
    public static final double CENTER_X = SCREEN_WIDTH / 2.0;
    public static final double CENTER_Y = SCREEN_HEIGHT / 2.0;

    // --- مشخصات شش‌ضلعی مرکزی ---
    public static final double HEXAGON_RADIUS = 60.0; // شعاع شش ضلعی مرکزی

    // --- مشخصات بازیکن ---
    public static final double PLAYER_SIZE = 20.0; // اندازه مثلث بازیکن
    // فاصله مرکز مثلث بازیکن از مرکز صفحه
    public static final double PLAYER_DISTANCE_FROM_CENTER = HEXAGON_RADIUS + 20.0;

    // --- ثابت‌های مربوط به چرخش (اگه لازم شد) ---
    public static final int SIDES = 6; // تعداد اضلاع شش ضلعی

    // --- ثابت‌های دیگر بازی (سرعت اولیه، رنگ‌ها و ...) بعدا میتونن اینجا اضافه بشن ---
    // public static final double INITIAL_GAME_SPEED = 1.0;
    // public static final javafx.scene.paint.Color PLAYER_COLOR = javafx.scene.paint.Color.CYAN;
    // public static final javafx.scene.paint.Color HEXAGON_STROKE_COLOR = javafx.scene.paint.Color.WHITE;
}
