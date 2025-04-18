package com.example.ap2superhexagon;

import javafx.animation.AnimationTimer;

import java.util.Locale;
import java.util.function.Consumer;

public class GameLoop{

    private AnimationTimer timer;
    private final Consumer<Long> updateAction ;
    private boolean running = false;

    public GameLoop(Consumer<Long> updateAction) {
        this.updateAction = updateAction;
    }

    public void start() {
        if (running) {
            return;
        }

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateAction.accept(now);
            }
        };

        timer.start();
        running = true;
        System.out.println("Game loop started.");


    }

    public void stop() {
        if ( !running || timer == null ) {
            return;
        }
        timer.stop();
        running = false;
        System.out.println("Game loop stopped.");
    }

    public boolean isRunning() {
        return running;
    }
}