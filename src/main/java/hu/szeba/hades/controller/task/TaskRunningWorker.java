package hu.szeba.hades.controller.task;

import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.model.task.result.Result;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class TaskRunningWorker extends SwingWorker<Integer, String> {

    private Task task;
    private JMenu disabledBuildMenu;
    private JTextArea terminalArea;

    public TaskRunningWorker(Task task, JMenu disabledBuildMenu, JTextArea terminalArea) {
        this.task = task;
        this.disabledBuildMenu = disabledBuildMenu;
        this.terminalArea = terminalArea;
    }

    @Override
    protected Integer doInBackground() throws IOException, InterruptedException {
        Result result = task.run();
        for (int i = 0; i < result.getResultLineCount(); i++) {
            publish(result.getResultLine(i).getData() + "\n");
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
    }

}
