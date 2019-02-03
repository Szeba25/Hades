package hu.szeba.hades.controller.task;

import java.util.concurrent.atomic.AtomicBoolean;

public class TaskThreadObserver {

    private AtomicBoolean shouldStop;

    public TaskThreadObserver() {
        shouldStop = new AtomicBoolean(false);
    }

    public void initialize() {
        shouldStop.set(false);
    }

    public void stop() {
        shouldStop.set(true);
    }

    public boolean shouldStop() {
        return shouldStop.get();
    }

}
