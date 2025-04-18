package com.example.ap2superhexagon;

import javafx.scene.Scene;

public class SceneManager {
    private static Scene mainMenuScene;

    public static void setMainMenuScene(Scene scene) {
        mainMenuScene = scene;
    }

    public static Scene getMainMenuScene() {
        return mainMenuScene;
    }
}