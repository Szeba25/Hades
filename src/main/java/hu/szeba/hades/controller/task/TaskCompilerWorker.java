package hu.szeba.hades.controller.task;

import hu.szeba.hades.model.task.Task;

import javax.swing.*;
import java.util.List;

public class TaskCompilerWorker extends SwingWorker<Integer, String> {

    private Task task;
    private JMenu disabledBuildMenu;
    private JTextArea terminalArea;

    public TaskCompilerWorker(Task task, JMenu disabledBuildMenu, JTextArea terminalArea) {
        this.task = task;
        this.disabledBuildMenu = disabledBuildMenu;
        this.terminalArea = terminalArea;
    }

    @Override
    protected Integer doInBackground() throws Exception {
        task.compile();
        for (String message : task.getCompileMessages()) {
            publish(message + "\n");
        }
        return 0;
    }

    @Override
    protected void process(List<String> chunks) {
        chunks.forEach(terminalArea::append);
    }

    @Override
    protected void done() {
        disabledBuildMenu.setEnabled(true);
        disabledBuildMenu.getItem(2).setEnabled(task.isProgramReady());
    }

}
