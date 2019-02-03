package hu.szeba.hades.controller.task;

import javax.swing.*;

public class ProcessCache {

    private volatile Process process;

    public ProcessCache() {
        this.process = null;
    }

    public synchronized void clear() {
        process = null;
    }

    public synchronized void set(Process process) {
        this.process = process;
    }

    public synchronized void destroy(JTextArea terminalArea) {
        if (process == null) {
            terminalArea.append("> Process didn't started yet. Please wait a second!\n");
        } else {
            process.destroyForcibly();
            process = null;
            terminalArea.append("> Requesting process destroy...\n");
        }
    }

}
