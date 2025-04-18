package com.example.ap2superhexagon;

import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class HighScoreManager {
    private static final String FILE_PATH = "best-score.json";
    private static final Gson gson = new Gson();


    private static class HighScore {
        public long bestMillis;
    }


    public static long loadHighScore() {
        try {
            if (!Files.exists(Path.of(FILE_PATH))) return 0;
            Reader reader = new FileReader(FILE_PATH);
            HighScore score = gson.fromJson(reader, HighScore.class);
            reader.close();
            return score != null ? score.bestMillis : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    public static void saveIfNewHighScore(String playerName, long newScoreMillis) {
        long current = loadHighScore();
        if (newScoreMillis > current) {
            try {
                Writer writer = new FileWriter(FILE_PATH);
                HighScore score = new HighScore();
                score.bestMillis = newScoreMillis;
                gson.toJson(score, writer);
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}