package com.example.ap2superhexagon;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class GameHistoryManager {
    private static final String FILE_PATH = "game_history.json";
    private static final Gson gson = new Gson();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static Preferences prefs = Preferences.userNodeForPackage(GameHistoryManager.class);
    private static boolean saveHistoryEnabled = true;

    public static class GameRecord {
        private String playerName;
        private String dateTime;
        private long durationMillis;

        public GameRecord(String playerName, String dateTime, long durationMillis) {
            this.playerName = playerName;
            this.dateTime = dateTime;
            this.durationMillis = durationMillis;
        }


        public String getPlayerName() {
            return playerName;
        }

        public String getDateTime() {
            return dateTime;
        }

        public String getDuration() {
            return String.format("%.1f s", durationMillis / 1000.0);
        }
    }


    public static List<GameRecord> loadHistory() {
        try {
            if (!Files.exists(Path.of(FILE_PATH))) {
                return new ArrayList<>();
            }
            Reader reader = new FileReader(FILE_PATH);
            List<GameRecord> history = gson.fromJson(reader, new TypeToken<List<GameRecord>>(){}.getType());
            reader.close();
            return history != null ? history : new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveGameRecord(String playerName, long durationMillis) {

        if (!saveHistoryEnabled) {
            System.out.println("History saving is currently disabled");
            return;
        }
        List<GameRecord> history = loadHistory();
        String dateTime = LocalDateTime.now().format(DATE_FORMATTER);
        history.add(new GameRecord(playerName, dateTime, durationMillis));

        try {
            Writer writer = new FileWriter(FILE_PATH);
            gson.toJson(history, writer);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean isHistoryEnabled() {
        return saveHistoryEnabled;
    }

    public static void setHistoryEnabled(boolean enabled) {
        saveHistoryEnabled  = enabled;
    }

    public static void saveSettings() {
        prefs.putBoolean("historyEnabled", saveHistoryEnabled);
    }

    public static void loadSettings() {
        saveHistoryEnabled  = prefs.getBoolean("historyEnabled", true);
    }
}