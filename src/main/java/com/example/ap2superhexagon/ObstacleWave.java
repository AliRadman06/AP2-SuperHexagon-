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

    /**
     * سازنده کمکی با استفاده از ضخامت دیوار پیش‌فرض.
     */
    public ObstacleWave(int[] pattern, double initialDistance, double speed, double centerX, double centerY) {
        this(pattern, initialDistance, speed, centerX, centerY, Constants.DEFAULT_WALL_WIDTH);
    }

    /**
     * این متد باید در هر فریم از حلقه بازی (Game Loop) فراخوانی شود.
     * موقعیت موج را به‌روز کرده و وضعیت حذف آن را بررسی می‌کند.
     *
     * @param deltaTime زمان سپری شده از فریم قبلی (به ثانیه).
     * @param centerX   مختصات X مرکز صفحه بازی.
     * @param centerY   مختصات Y مرکز صفحه بازی.
     */
    public void update(double deltaTime, double centerX, double centerY) {
        // اگر موج قبلاً برای حذف علامت خورده، کاری انجام نده
        if (this.markedForRemoval) {
            return;
        }

        // موج را به سمت مرکز حرکت بده
        this.distanceFromCenter -= this.speed * deltaTime;

        // بررسی کن آیا موج به مرکز رسیده یا از آستانه حذف عبور کرده است
        // REMOVAL_THRESHOLD_RADIUS معمولاً کمی کمتر از شعاع بازیکن است
        if (this.distanceFromCenter < Constants.REMOVAL_THRESHOLD_RADIUS) {
            this.markedForRemoval = true;
            // نکته: دیوارها در آخرین موقعیت قبل از حذف باقی می‌مانند.
            // اگر نیاز به انیمیشن ناپدید شدن باشد، منطق بیشتری لازم است.
            // فعلاً، فقط برای حذف علامت می‌زنیم.
            return; // نیازی به آپدیت شکل دیوارها نیست چون موج حذف خواهد شد
        }

        // اگر موج هنوز فعال است، شکل تمام دیوارهای آن را به‌روز کن
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


    /**
     * بررسی می‌کند که آیا این موج برای حذف از بازی علامت خورده است یا خیر.
     * (معمولاً به این معنی است که به مرکز رسیده یا از صفحه خارج شده)
     *
     * @return true اگر موج باید حذف شود، در غیر این صورت false.
     */
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
