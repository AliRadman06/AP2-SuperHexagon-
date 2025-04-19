package com.example.ap2superhexagon;

public final class Constants {

    // جلوگیری از ساختن شیء از این کلاس (چون فقط شامل ثابت هست)
    private Constants() {}

    // --- ابعاد صفحه و مرکز ---
    public static final double SCREEN_WIDTH = 1024.0;
    public static final double SCREEN_HEIGHT = 576.0;
    public static final double CENTER_X = 512;
    public static final double CENTER_Y = 288;

    // --- مشخصات شش‌ضلعی مرکزی ---
    public static final double HEXAGON_RADIUS = 60.0; // شعاع شش ضلعی مرکزی

    // --- مشخصات بازیکن ---
    public static final double PLAYER_SIZE = 15.0; // اندازه مثلث بازیکن

    // فاصله مرکز مثلث بازیکن از مرکز صفحه
    public static final double PLAYER_DISTANCE_FROM_CENTER = HEXAGON_RADIUS + 10.0;

    public static final int PALETTE_SWITCH_INTERVAL_SECONDS = 3 ;
    public static final long INITIAL_OBSTACLE_SPAWN_INTERVAL = 3;
    public static final long INITIAL_OBSTACLE_SPEED = 150;
    public static final long MIN_GAPS_IN_PATTERN = 10;

    // --- ثابت‌های مربوط به چرخش (اگه لازم شد) ---
    public static final int SIDES = 6; // تعداد اضلاع شش ضلعی

    public static final int SPAWN_DISTANCE = 700;
    public static final double OBSTACLE_WALL_WIDTH = 30.0;

    public static final double DEFAULT_WALL_WIDTH = 15.0;
    public static final double REMOVAL_THRESHOLD_RADIUS = 75.0;

    public static final double SPEED_INCREASE_INTERVAL = 1; // هر 10 ثانیه
    public static final double SPEED_INCREMENT = 5.0;         // افزایش 2 واحدی

    public static final double MIN_OBSTACLE_SPAWN_INTERVAL = 0.8; // پایین‌ترین مقدار مجاز
    public static final double SPAWN_INTERVAL_DECREMENT = 0.4;   // هر بار اینقدر کم شه
}
