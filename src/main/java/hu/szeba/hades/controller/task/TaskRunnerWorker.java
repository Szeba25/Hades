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

public class TaskRunnerWorker extends SwingWorker<Integer, String> {

    private ProcessCache processCache;
    private Program program;
    private List<InputResultPair> inputResultPairs;
    private BuildMenuWrapper buildMenuWrapper;
    private JTextArea terminalArea;
    private int maxByteCount;

    TaskRunnerWorker(ProcessCache processCache,
                     Program program,
                     List<InputResultPair> inputResultPairs,
                     BuildMenuWrapper buildMenuWrapper, JTextArea terminalArea,
                     int maxByteCount) {
        this.processCache = processCache;
        this.program = program;
        this.inputResultPairs = inputResultPairs;
        this.buildMenuWrapper = buildMenuWrapper;
        this.terminalArea = terminalArea;
        this.maxByteCount = maxByteCount;
    }

    @Override
    protected Integer doInBackground() throws IOException, InterruptedException {
        publish("> Running program...\n\n");

        ResultMatcher matcher = new ResultMatcher();

        for (InputResultPair inputResultPair : inputResultPairs) {
            publish("> Using input: " + inputResultPair.getProgramInput().getFile().getName() + "\n");
            Result result = program.run(inputResultPair.getProgramInput(), processCache, maxByteCount);

            for (int i = 0; i < result.getResultLineCount(); i++) {
                publish((i + 1) + ". " + result.getResultLineByIndex(i).getData() + "\n");
            }
            publish("\n");
            matcher.match(result, inputResultPair.getDesiredResult());
            for (int i = 0; i < matcher.getDifferencesSize(); i++) {
                ResultDifference diff = matcher.getDifference(i);
                publish("* difference at line: " + diff.getLineNumber() + ". \"" + diff.getFirstLine().getData() + "\" should be \""
                        + diff.getSecondLine().getData() + "\"\n");
            }
            if (matcher.getDifferencesSize() > 0)
                publish("\n");
        }

        publish("... End of running!");
        return 0;
    }

    @Override
    protected void process(List<String> chunks) {
        for (String line : chunks) {
            if (line.length() < 200) {
                terminalArea.append(line);
            } else {
                terminalArea.append(line.substring(0, 200) + ".....\n");
            }
        }
    }

    @Override
    protected void done() {
        processCache.clear();
        buildMenuWrapper.setBuildEnabled(true);
        buildMenuWrapper.setBuildAndRunEnabled(true);
        buildMenuWrapper.setRunEnabled(true);
        buildMenuWrapper.setStopEnabled(false);
    }

}
