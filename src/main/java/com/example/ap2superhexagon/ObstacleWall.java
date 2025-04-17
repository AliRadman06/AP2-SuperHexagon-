package com.example.ap2superhexagon;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 * نماینده یک تکه دیوار مانع به شکل ذوزنقه.
 * این دیوار با یکی از 6 بخش پس‌زمینه تراز می‌شود.
 */
public class ObstacleWall {

    private Polygon shape;           // شکل گرافیکی دیوار (ذوزنقه)
    private final double angleDegrees;     // زاویه مرکزی این دیوار (0, 60, 120, ...)
    private final double wallWidth;        // ضخامت دیوار (در راستای شعاع از مرکز)

    // زاویه هر بخش شش‌ضلعی (360 / 6 = 60 درجه)
    private static final double SECTOR_ANGLE_DEGREES = 60.0;
    // نصف زاویه بخش به رادیان، برای محاسبه گوشه‌ها (30 درجه)
    private static final double HALF_SECTOR_ANGLE_RAD = Math.toRadians(SECTOR_ANGLE_DEGREES / 2.0);

    /**
     * سازنده یک تکه دیوار مانع (ذوزنقه‌ای).
     *
     * @param angleDegrees زاویه مرکزی این دیوار نسبت به مرکز بازی (0, 60, 120, ...).
     * @param wallWidth    ضخامت دیوار در راستای شعاع.
     */
    public ObstacleWall(double angleDegrees, double wallWidth) {
        if (wallWidth <= 0) {
            throw new IllegalArgumentException("Wall width must be positive.");
        }
        this.angleDegrees = angleDegrees;
        this.wallWidth = wallWidth;

        // Polygon خالی ایجاد می‌شود. نقاط آن در متد updateShape محاسبه و تنظیم می‌شوند.
        this.shape = new Polygon();
        // رنگ و استایل اولیه (می‌تواند بعداً توسط ObstacleWave یا ColorManager تغییر کند)
        this.shape.setFill(Color.DARKGRAY); // یک رنگ پیش‌فرض
        this.shape.getStyleClass().add("obstacle-wall"); // برای استایل‌دهی با CSS (اختیاری)
    }

    /**
     * مختصات گوشه‌های ذوزنقه را بر اساس فاصله فعلی موج از مرکز محاسبه کرده
     * و شکل Polygon را به‌روز می‌کند.
     *
     * @param distanceFromCenter فاصله فعلی مرکز موج تا مرکز بازی.
     * @param centerX            مختصات X مرکز صفحه بازی.
     * @param centerY            مختصات Y مرکز صفحه بازی.
     */
    public void updateShape(double distanceFromCenter, double centerX, double centerY) {
        // محاسبه شعاع لبه داخلی و خارجی ذوزنقه نسبت به مرکز بازی
        // distanceFromCenter معرف خط مرکزی ضخامت دیوار است.
        double innerRadius = distanceFromCenter - this.wallWidth / 2.0;
        double outerRadius = distanceFromCenter + this.wallWidth / 2.0;

        // جلوگیری از شعاع داخلی منفی (اگرچه موج باید قبل از این مرحله حذف شود)
        if (innerRadius < 0) {
            innerRadius = 0;
        }
        // اگر شعاع خارجی هم منفی شد (بسیار بعید)، آن را هم صفر کن
        if (outerRadius < 0) {
            outerRadius = 0;
        }


        // زاویه مرکزی دیوار به رادیان
        double centerAngleRad = Math.toRadians(this.angleDegrees);

        // محاسبه زاویه گوشه‌های چپ و راست ذوزنقه نسبت به مرکز بازی
        // این زوایا خطوط جداکننده بخش‌های پس‌زمینه را مشخص می‌کنند.
        double angleLeftRad = centerAngleRad - HALF_SECTOR_ANGLE_RAD;
        double angleRightRad = centerAngleRad + HALF_SECTOR_ANGLE_RAD;

        // محاسبه مختصات ۴ گوشه ذوزنقه در مختصات صفحه بازی:
        // گوشه 1: داخلی-چپ (Inner-Left)
        double x1 = centerX + innerRadius * Math.cos(angleLeftRad);
        double y1 = centerY + innerRadius * Math.sin(angleLeftRad);

        // گوشه 2: خارجی-چپ (Outer-Left)
        double x2 = centerX + outerRadius * Math.cos(angleLeftRad);
        double y2 = centerY + outerRadius * Math.sin(angleLeftRad);

        // گوشه 3: خارجی-راست (Outer-Right)
        double x3 = centerX + outerRadius * Math.cos(angleRightRad);
        double y3 = centerY + outerRadius * Math.sin(angleRightRad);

        // گوشه 4: داخلی-راست (Inner-Right)
        double x4 = centerX + innerRadius * Math.cos(angleRightRad);
        double y4 = centerY + innerRadius * Math.sin(angleRightRad);

        // به‌روزرسانی مستقیم نقاط Polygon با مختصات محاسبه شده
        // ترتیب نقاط مهم است تا شکل درست رسم شود (مثلاً: داخلی-چپ، خارجی-چپ، خارجی-راست، داخلی-راست)
        shape.getPoints().setAll(x1, y1, x2, y2, x3, y3, x4, y4);

        // دیگر نیازی به تنظیم Translate یا Rotate برای shape نیست،
        // چون مختصات محاسبه شده، مطلق و در دنیای بازی هستند.
    }

    /**
     * گره (Node) گرافیکی دیوار را برای اضافه شدن به صحنه بازی برمی‌گرداند.
     *
     * @return Node مربوط به این دیوار.
     */
    public Node getShapeNode() {
        return this.shape;
    }

    /**
     * شکل Polygon دیوار را برای بررسی برخورد یا کارهای دیگر برمی‌گرداند.
     *
     * @return شکل Polygon.
     */
    public Polygon getPolygon() {
        return this.shape;
    }

    /**
     * رنگ دیوار را تنظیم می‌کند.
     *
     * @param color رنگ جدید برای دیوار.
     */
    public void setColor(Color color) {
        if (this.shape != null) {
            this.shape.setFill(color);
        }
    }
}
