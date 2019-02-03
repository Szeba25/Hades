package hu.szeba.hades.controller.task;

import hu.szeba.hades.model.task.data.InputResultPair;
import hu.szeba.hades.model.task.program.Program;
import hu.szeba.hades.model.task.result.Result;
import hu.szeba.hades.model.task.result.ResultDifference;
import hu.szeba.hades.model.task.result.ResultMatcher;
import hu.szeba.hades.view.task.BuildMenuWrapper;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskRunnerWorker extends SwingWorker<Integer, String> {

    private TaskThreadObserver taskThreadObserver;
    private ProcessCache processCache;
    private Program program;
    private List<InputResultPair> inputResultPairs;
    private BuildMenuWrapper buildMenuWrapper;
    private JTextArea terminalArea;

    TaskRunnerWorker(TaskThreadObserver taskThreadObserver,
                    ProcessCache processCache, Program program,
                     List<InputResultPair> inputResultPairs,
                     BuildMenuWrapper buildMenuWrapper, JTextArea terminalArea) {
        this.taskThreadObserver = taskThreadObserver;
        this.processCache = processCache;
        this.program = program;
        this.inputResultPairs = inputResultPairs;
        this.buildMenuWrapper = buildMenuWrapper;
        this.terminalArea = terminalArea;
    }

    @Override
    protected Integer doInBackground() throws IOException, InterruptedException {
        publish("> Running program...\n\n");

        ResultMatcher matcher = new ResultMatcher();

        for (InputResultPair inputResultPair : inputResultPairs) {
            publish("> Using input: " + inputResultPair.getProgramInput().getFile().getName() + "\n");
            Result result = program.run(inputResultPair.getProgramInput(), taskThreadObserver, processCache);
            for (int i = 0; i < result.getResultLineCount(); i++) {
                publish((i + 1) + ". " + result.getResultLineByIndex(i).getData() + "\n");
            }
            publish("\n");
            matcher.match(result, inputResultPair.getDesiredResult());
            for (int i = 0; i < matcher.getDifferencesSize(); i++) {
                ResultDifference diff = matcher.getDifference(i);
                publish("* difference at line: " + diff.getLineNumber());
                publish(". \"" + diff.getFirstLine().getData() + "\" should be \""
                        + diff.getSecondLine().getData() + "\"\n");
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
        processCache.clearProcess();
        taskThreadObserver.stop();
        buildMenuWrapper.setBuildEnabled(true);
        buildMenuWrapper.setBuildAndRunEnabled(true);
        buildMenuWrapper.setRunEnabled(true);
        buildMenuWrapper.setStopEnabled(false);
    }

}
