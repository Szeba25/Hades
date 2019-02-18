package hu.szeba.hades.view.task;

import javax.swing.*;

public class LockedMenusWrapper {

    private boolean lockExit;
    private JMenuItem newFileItem;
    private JMenuItem deleteFileItem;
    private JMenuItem renameFileItem;
    private JMenuItem buildMenuItem;
    private JMenuItem buildAndRunMenuItem;
    private JMenuItem runMenuItem;
    private JMenuItem stopMenuItem;

    public LockedMenusWrapper(
            JMenuItem newFileItem,
            JMenuItem deleteFileItem,
            JMenuItem renameFileItem,
            JMenuItem buildMenuItem,
            JMenuItem buildAndRunMenuItem,
            JMenuItem runMenuItem,
            JMenuItem stopMenuItem) {
        lockExit = false;
        this.newFileItem = newFileItem;
        this.deleteFileItem = deleteFileItem;
        this.renameFileItem = renameFileItem;
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
        setDeleteFileEnabled(false);
        setRenameFileEnabled(false);
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
        setDeleteFileEnabled(false);
        setRenameFileEnabled(false);
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
        setDeleteFileEnabled(false);
        setRenameFileEnabled(false);
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
        setDeleteFileEnabled(true);
        setRenameFileEnabled(true);
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
        setDeleteFileEnabled(true);
        setRenameFileEnabled(true);
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
        setDeleteFileEnabled(true);
        setRenameFileEnabled(true);
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

    private synchronized void setDeleteFileEnabled(boolean value) {
        deleteFileItem.setEnabled(value);
    }

    private synchronized void setRenameFileEnabled(boolean value) {
        renameFileItem.setEnabled(value);
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
