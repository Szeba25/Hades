package hu.szeba.hades.controller.task;

import javax.swing.*;

public class ProcessCache {

    private volatile Process process;

    public ProcessCache() {
        this.process = null;
    }

    public void clear() {
        process = null;
    }

    public void set(Process process) {
        this.process = process;
    }

    public void destroy(JTextArea terminalArea) {
        if (process != null) {
            process.destroyForcibly();
            process = null;
            terminalArea.append("> Requesting process destroy...\n");
        } else {
            terminalArea.append("> Process didn't started yet. Please wait a second!\n");
        }
    }

}
