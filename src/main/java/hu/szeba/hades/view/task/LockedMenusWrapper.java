package hu.szeba.hades.view.task;

import javax.swing.*;

public class LockedMenusWrapper {

    private boolean lockExit;
    private JMenuItem newFileItem;
    private JMenuItem deleteFilePopupItem;
    private JMenuItem saveAllFileItem;
    private JMenuItem buildMenuItem;
    private JMenuItem buildAndRunMenuItem;
    private JMenuItem runMenuItem;
    private JMenuItem stopMenuItem;

    public LockedMenusWrapper(
            JMenuItem newFileItem,
            JMenuItem deleteFilePopupItem,
            JMenuItem saveAllFileItem,
            JMenuItem buildMenuItem,
            JMenuItem buildAndRunMenuItem,
            JMenuItem runMenuItem,
            JMenuItem stopMenuItem) {
        lockExit = false;
        this.newFileItem = newFileItem;
        this.deleteFilePopupItem = deleteFilePopupItem;
        this.saveAllFileItem = saveAllFileItem;
        this.buildMenuItem = buildMenuItem;
        this.buildAndRunMenuItem = buildAndRunMenuItem;
        this.runMenuItem = runMenuItem;
        this.stopMenuItem = stopMenuItem;
    }

    private synchronized void setLockExit(boolean lockExit) {
        this.lockExit = lockExit;
    }

    public synchronized boolean getLockExit() {
        return lockExit;
    }

    public synchronized void initForCompiler() {
        // File
        setNewFileEnabled(false);
        setDeleteFilePopupEnabled(false);
        setSaveAllFileEnabled(false);
        // Build
        setBuildEnabled(false);
        setBuildAndRunEnabled(false);
        setRunEnabled(false);
        setStopEnabled(false);
        // Lock
        setLockExit(true);
    }

    public synchronized void initForCompilerAndRunner() {
        // File
        setNewFileEnabled(false);
        setDeleteFilePopupEnabled(false);
        setSaveAllFileEnabled(false);
        // Build
        setBuildEnabled(false);
        setBuildAndRunEnabled(false);
        setRunEnabled(false);
        setStopEnabled(true);
        // Lock
        setLockExit(true);
    }

    public synchronized void initForRunner() {
        // File
        setNewFileEnabled(false);
        setDeleteFilePopupEnabled(false);
        setSaveAllFileEnabled(true);
        // Build
        setBuildEnabled(false);
        setBuildAndRunEnabled(false);
        setRunEnabled(false);
        setStopEnabled(true);
        // Lock
        setLockExit(true);
    }

    public synchronized void finishForCompiler(boolean programReady) {
        // File
        setNewFileEnabled(true);
        setDeleteFilePopupEnabled(true);
        setSaveAllFileEnabled(true);
        // Build
        setBuildEnabled(true);
        setBuildAndRunEnabled(true);
        setRunEnabled(programReady);
        setStopEnabled(false);
        // Lock
        setLockExit(false);
    }

    public synchronized void finishForCompilerAndRunner(boolean programReady) {
        // File
        setNewFileEnabled(true);
        setDeleteFilePopupEnabled(true);
        setSaveAllFileEnabled(true);
        // Build
        setBuildEnabled(true);
        setBuildAndRunEnabled(true);
        setRunEnabled(programReady);
        setStopEnabled(false);
        // Lock
        setLockExit(false);
    }

    public synchronized void finishForRunner() {
        // File
        setNewFileEnabled(true);
        setDeleteFilePopupEnabled(true);
        setSaveAllFileEnabled(true);
        // Build
        setBuildEnabled(true);
        setBuildAndRunEnabled(true);
        setRunEnabled(true);
        setStopEnabled(false);
        // Lock
        setLockExit(false);
    }

    private synchronized void setNewFileEnabled(boolean value) {
        newFileItem.setEnabled(value);
    }

    private synchronized void setDeleteFilePopupEnabled(boolean value) {
        deleteFilePopupItem.setEnabled(value);
    }

    private synchronized void setSaveAllFileEnabled(boolean value) {
        saveAllFileItem.setEnabled(value);
    }

    private synchronized void setBuildEnabled(boolean value) {
        buildMenuItem.setEnabled(value);
    }

    private synchronized void setBuildAndRunEnabled(boolean value) {
        buildAndRunMenuItem.setEnabled(value);
    }

    private synchronized void setRunEnabled(boolean value) {
        runMenuItem.setEnabled(value);
    }

    private synchronized void setStopEnabled(boolean value) {
        stopMenuItem.setEnabled(value);
    }

}
