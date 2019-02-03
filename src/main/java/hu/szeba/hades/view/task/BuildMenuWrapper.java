package hu.szeba.hades.view.task;

import javax.swing.*;

public class BuildMenuWrapper {

    private JMenuItem buildMenuItem;
    private JMenuItem buildAndRunMenuItem;
    private JMenuItem runMenuItem;

    public BuildMenuWrapper(JMenuItem buildMenuItem,
                            JMenuItem buildAndRunMenuItem,
                            JMenuItem runMenuItem) {
        this.buildMenuItem = buildMenuItem;
        this.buildAndRunMenuItem = buildAndRunMenuItem;
        this.runMenuItem = runMenuItem;
    }

    public void setBuildEnabled(boolean value) {
        buildMenuItem.setEnabled(value);
    }

    public void setBuildAndRunEnabled(boolean value) {
        buildAndRunMenuItem.setEnabled(value);
    }

    public void setRunEnabled(boolean value) {
        runMenuItem.setEnabled(value);
    }

}
