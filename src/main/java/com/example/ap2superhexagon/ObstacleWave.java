package com.example.ap2superhexagon;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ObstacleWave {

    private final int[] pattern; // الگوی باینری 6 تایی (0=شکاف, 1=دیوار)
    private List<ObstacleWall> walls; // لیست دیوارهای موجود در این موج
    private double distanceFromCenter; // فاصله فعلی مرکز موج تا مرکز بازی
    private final double speed;        // سرعت حرکت موج به سمت مرکز (پیکسل بر ثانیه)
    private boolean markedForRemoval = false; // برای حذف موج وقتی از محدوده خارج شد


    public ObstacleWave(int[] pattern, double initialDistance, double speed, double centerX, double centerY, double wallWidth) {
        // بررسی ورودی‌ها
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

        // ساختن دیوارهای لازم بر اساس الگو
        for (int i = 0; i < pattern.length; i++) {
            if (pattern[i] == 1) { // اگر در این موقعیت (سکتور) دیوار داریم
                // محاسبه زاویه مرکزی برای این دیوار (0, 60, 120, 180, 240, 300)
                double angle = i * 60.0 + 30.0;
                // ایجاد یک نمونه دیوار ذوزنقه‌ای
                ObstacleWall wall = new ObstacleWall(angle, wallWidth);
                // محاسبه و تنظیم شکل اولیه دیوار در موقعیت شروع موج
                wall.updateShape(this.distanceFromCenter, centerX, centerY);
                // افزودن دیوار به لیست دیوارهای این موج
                this.walls.add(wall);
            }
            // اگر pattern[i] == 0، هیچ دیواری در آن بخش ایجاد نمی‌شود (شکاف)
        }
    }

    public ObstacleWave(int[] pattern, double initialDistance, double speed, double centerX, double centerY) {
        this(pattern, initialDistance, speed, centerX, centerY, Constants.DEFAULT_WALL_WIDTH);
    }

    public void update(double deltaTime, double centerX, double centerY) {

        if (this.markedForRemoval) {
            return;
        }




            // موج را به سمت مرکز حرکت بده
            this.distanceFromCenter -= this.speed * deltaTime;

            if (this.distanceFromCenter < Constants.REMOVAL_THRESHOLD_RADIUS) {
                this.markedForRemoval = true;
                return;
            }

            for (ObstacleWall wall : this.walls) {
                wall.updateShape(this.distanceFromCenter, centerX, centerY);
            }


    }

    /**
     * لیستی از گره‌های گرافیکی (Node) همه دیوارهای این موج را برمی‌گرداند.
     * این لیست برای افزودن یا حذف دیوارها از صحنه بازی استفاده می‌شود.
     *
     * @return لیستی از Node های دیوارها.
     */
    public List<Node> getWallShapeNodes() {
        List<Node> shapes = new ArrayList<>(walls.size());
        for (ObstacleWall wall : walls) {
            shapes.add(wall.getShapeNode());
        }
        return shapes;
    }

    /**
     * لیستی از خود آبجکت‌های ObstacleWall را برمی‌گرداند.
     * این ممکن است برای بررسی دقیق‌تر برخورد لازم باشد.
     *
     * @return لیست آبجکت‌های ObstacleWall.
     */
    public List<ObstacleWall> getWalls() {
        // برگرداندن یک کپی غیرقابل تغییر برای جلوگیری از تغییرات خارجی ناخواسته
        return List.copyOf(this.walls);
        // یا اگر تغییر لیست اصلی از بیرون مجاز است: return this.walls;
    }


    public boolean isMarkedForRemoval() {
        return this.markedForRemoval;
    }

    /**
     * رنگ تمام دیوارهای موجود در این موج را تنظیم می‌کند.
     *
     * @param color رنگ جدید برای دیوارها.
     */
    public void setColor(Color color) {
        for (ObstacleWall wall : this.walls) {
            wall.setColor(color);
        }
    }

    /**
     * الگوی باینری استفاده شده برای ساخت این موج را برمی‌گرداند.
     *
     * @return آرایه int الگوی موج.
     */

    public int[] getPattern() {
        // برگرداندن یک کپی برای جلوگیری از تغییر الگوی داخلی
        return java.util.Arrays.copyOf(pattern, pattern.length);
    }

    /**
     * فاصله فعلی مرکز موج تا مرکز بازی را برمی‌گرداند.
     * @return فاصله از مرکز.
     */
    public double getDistanceFromCenter() {
        return distanceFromCenter;
    }
}
