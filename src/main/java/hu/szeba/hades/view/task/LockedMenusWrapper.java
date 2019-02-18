package hu.szeba.hades.view.task;

import javax.swing.*;

public class LockedMenusWrapper {

    private boolean lockExit;
    private JMenuItem buildMenuItem;
    private JMenuItem buildAndRunMenuItem;
    private JMenuItem runMenuItem;
    private JMenuItem stopMenuItem;

    public LockedMenusWrapper(JMenuItem buildMenuItem,
                              JMenuItem buildAndRunMenuItem,
                              JMenuItem runMenuItem,
                              JMenuItem stopMenuItem) {
        lockExit = false;
        this.buildMenuItem = buildMenuItem;
        this.buildAndRunMenuItem = buildAndRunMenuItem;
        this.runMenuItem = runMenuItem;
        this.stopMenuItem = stopMenuItem;
    }

    public synchronized void setLockExit(boolean lockExit) {
        this.lockExit = lockExit;
    }

    public synchronized boolean getLockExit() {
        return lockExit;
    }

    public synchronized void setBuildEnabled(boolean value) {
        buildMenuItem.setEnabled(value);
    }

    public synchronized void setBuildAndRunEnabled(boolean value) {
        buildAndRunMenuItem.setEnabled(value);
    }

    public synchronized void setRunEnabled(boolean value) {
        runMenuItem.setEnabled(value);
    }

    public synchronized void setStopEnabled(boolean value) {
        stopMenuItem.setEnabled(value);
    }

}
