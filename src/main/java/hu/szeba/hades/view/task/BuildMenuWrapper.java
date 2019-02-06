package hu.szeba.hades.view.task;

import javax.management.JMException;
import javax.swing.*;

public class BuildMenuWrapper {

    private JMenuItem buildMenuItem;
    private JMenuItem buildAndRunMenuItem;
    private JMenuItem runMenuItem;
    private JMenuItem stopMenuItem;

    public BuildMenuWrapper(JMenuItem buildMenuItem,
                            JMenuItem buildAndRunMenuItem,
                            JMenuItem runMenuItem,
                            JMenuItem stopMenuItem) {
        this.buildMenuItem = buildMenuItem;
        this.buildAndRunMenuItem = buildAndRunMenuItem;
        this.runMenuItem = runMenuItem;
        this.stopMenuItem = stopMenuItem;
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
