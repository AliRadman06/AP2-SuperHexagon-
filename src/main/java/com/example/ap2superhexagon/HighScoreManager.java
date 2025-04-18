package com.example.ap2superhexagon;

import com.google.gson.Gson;  // برای استفاده از Gson جهت ذخیره و بارگذاری JSON

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class HighScoreManager {
    private static final String FILE_PATH = "best-score.json";  // فایل ذخیره رکورد
    private static final Gson gson = new Gson();  // استفاده از Gson برای پردازش JSON

    // کلاس داخلی برای ذخیره رکورد
    private static class HighScore {
        public long bestMillis;  // زمان رکورد به میلی‌ثانیه
    }

    // بارگذاری رکورد از فایل
    public static long loadHighScore() {
        try {
            if (!Files.exists(Path.of(FILE_PATH))) return 0;  // اگر فایل وجود نداشت، رکورد صفر باشه
            Reader reader = new FileReader(FILE_PATH);
            HighScore score = gson.fromJson(reader, HighScore.class);
            reader.close();
            return score != null ? score.bestMillis : 0;  // اگر رکورد یافت شد، برگردوندن اون
        } catch (Exception e) {
            e.printStackTrace();
            return 0;  // در صورت خطا، رکورد صفر برمی‌گردونیم
        }
    }

    // ذخیره رکورد جدید در صورتی که رکورد جدید بزرگتر از رکورد قبلی باشد
    public static void saveIfNewHighScore(String playerName, long newScoreMillis) {
        long current = loadHighScore();  // رکورد فعلی رو بارگذاری می‌کنیم
        if (newScoreMillis > current) {  // اگر رکورد جدید بزرگتر بود، اون رو ذخیره می‌کنیم
            try {
                Writer writer = new FileWriter(FILE_PATH);  // باز کردن فایل برای نوشتن
                HighScore score = new HighScore();
                score.bestMillis = newScoreMillis;  // رکورد جدید رو ذخیره می‌کنیم
                gson.toJson(score, writer);  // تبدیل شیء به JSON و نوشتن در فایل
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
