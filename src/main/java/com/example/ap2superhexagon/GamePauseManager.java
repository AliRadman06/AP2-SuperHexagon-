package com.example.ap2superhexagon;

import javafx.scene.shape.Polygon;

public class GamePauseManager {

        private boolean isPaused = false;

        public void togglePause() {
            isPaused = !isPaused;
        }

        public boolean isPaused() {
            return isPaused;
        }


}
