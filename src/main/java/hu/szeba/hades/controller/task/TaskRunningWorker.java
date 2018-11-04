package hu.szeba.hades.controller.task;

import hu.szeba.hades.model.task.program.Program;
import hu.szeba.hades.model.task.result.Result;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class TaskRunningWorker extends SwingWorker<Integer, String> {

    private Program program;
    private JMenu disabledBuildMenu;
    private JTextArea terminalArea;

    TaskRunningWorker(Program program, JMenu disabledBuildMenu, JTextArea terminalArea) {
        this.program = program;
        this.disabledBuildMenu = disabledBuildMenu;
        this.terminalArea = terminalArea;
    }

    @Override
    protected Integer doInBackground() throws IOException, InterruptedException {
        publish("> Running program...\n\n");
        Result result = program.run(null);
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
