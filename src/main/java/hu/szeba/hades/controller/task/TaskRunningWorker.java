package hu.szeba.hades.controller.task;

import hu.szeba.hades.model.task.data.Solution;
import hu.szeba.hades.model.task.program.Program;
import hu.szeba.hades.model.task.result.Result;
import hu.szeba.hades.model.task.result.ResultDifference;
import hu.szeba.hades.model.task.result.ResultMatcher;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class TaskRunningWorker extends SwingWorker<Integer, String> {

    private Program program;
    private List<Solution> solutions;
    private JMenu disabledBuildMenu;
    private JTextArea terminalArea;

    TaskRunningWorker(Program program, List<Solution> solutions, JMenu disabledBuildMenu, JTextArea terminalArea) {
        this.program = program;
        this.solutions = solutions;
        this.disabledBuildMenu = disabledBuildMenu;
        this.terminalArea = terminalArea;
    }

    @Override
    protected Integer doInBackground() throws IOException, InterruptedException {
        publish("> Running program...\n\n");

        ResultMatcher matcher = new ResultMatcher();

        for (Solution solution : solutions) {
            publish("> Using input: " + solution.getProgramInput().getFile().getName() + "\n");
            Result result = program.run(solution.getProgramInput());
            for (int i = 0; i < result.getResultLineCount(); i++) {
                publish((i + 1) + ". " + result.getResultLineByIndex(i).getData() + "\n");
            }
            publish("\n");
            matcher.match(result, solution.getDesiredResult());
            for (int i = 0; i < matcher.getDifferencesSize(); i++) {
                ResultDifference diff = matcher.getDifference(i);
                publish("* difference at line: " + diff.getLineNumber());
                publish(". [" + diff.getFirstLine().getData() + "] should be ["
                        + diff.getSecondLine().getData() + "]\n");
            }
            publish("\n");
        }

        publish("... End of running!");
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
