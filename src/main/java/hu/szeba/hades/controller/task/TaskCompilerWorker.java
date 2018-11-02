package hu.szeba.hades.controller.task;

import hu.szeba.hades.model.task.Task;

import javax.swing.*;
import java.util.List;

public class TaskCompilerWorker extends SwingWorker<Integer, String> {

    private Task task;
    private JTextArea terminalArea;

    public TaskCompilerWorker(Task task, JTextArea terminalArea) {
        this.task = task;
        this.terminalArea = terminalArea;
    }

    @Override
    protected Integer doInBackground() throws Exception {
        task.saveFirstSource();
        publish("Sources saved...\n");
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

}
