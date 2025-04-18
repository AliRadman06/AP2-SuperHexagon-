package com.example.ap2superhexagon;

import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.util.prefs.Preferences;

public class AudioManager {
    private static MediaPlayer menuPlayer;
    private static MediaPlayer gamePlayer;
    private static MediaPlayer effectPlayer;
    private static double volume = 0.5;
    private static MediaPlayer gameplayPlayer;

    private static boolean musicEnabled = true;
    private static Preferences prefs = Preferences.userNodeForPackage(AudioManager.class);

    public static boolean isMusicEnabled() {
        return musicEnabled;
    }

    public static void setMusicEnabled(boolean enabled) {
        musicEnabled = enabled;
        if (menuPlayer != null) {
            if (enabled) menuPlayer.play();
            else menuPlayer.pause();
        }
    }

    public static void saveSettings() {
        prefs.putBoolean("musicEnabled", musicEnabled);
    }

    public static void loadSettings() {
        musicEnabled = prefs.getBoolean("musicEnabled", true);
    }


    public static void playMenuTheme() {
        stopAll();
        try {
            URL resource = AudioManager.class.getResource("/music/That Zen Moment.mp3");
            Media media = new Media(resource.toString());
            menuPlayer = new MediaPlayer(media);
            menuPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            menuPlayer.setVolume(volume);
            menuPlayer.play();
        } catch (Exception e) {
            System.err.println("Error loading menu theme: " + e.getMessage());
        }
    }

    public static void playGameTheme() {
        stopAll();
        try {
            URL resource = AudioManager.class.getResource("/music/Backbeat.mp3");
            Media media = new Media(resource.toString());
            gamePlayer = new MediaPlayer(media);
            gamePlayer.setCycleCount(MediaPlayer.INDEFINITE);
            gamePlayer.setVolume(volume);
            gamePlayer.play();
        } catch (Exception e) {
            System.err.println("Error loading game theme: " + e.getMessage());
        }
    }

    public static void playGameOver() {
        playSoundEffect("/music/negative_beeps-6008.mp3");
    }

    public static void playRotateSound() {
//        playSoundEffect("/audio/sfx/rotate.wav");
    }

    public static void playClickSound() {
        playSoundEffect("/music/aspose_switch36.mp3");
    }

    private static void playSoundEffect(String path) {
        try {
            URL resource = AudioManager.class.getResource(path);
            Media media = new Media(resource.toString());
            effectPlayer = new MediaPlayer(media);
            effectPlayer.setVolume(volume);
            effectPlayer.play();
        } catch (Exception e) {
            System.err.println("Error playing sound effect: " + e.getMessage());
        }
    }

    public static void stopAll() {
        if (menuPlayer != null) menuPlayer.stop();
        if (gamePlayer != null) gamePlayer.stop();
    }

    private static double getAdjustedVolume(double baseVolume) {
        return baseVolume * volume; // تطبیق با حجم کلی سیستم
    }

    public static void stopGameplayMusic() {
        if (gameplayPlayer != null) {
            gameplayPlayer.stop();
        }
    }
}