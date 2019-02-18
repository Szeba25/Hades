package hu.szeba.hades.controller.task;

import hu.szeba.hades.model.task.data.InputResultPair;
import hu.szeba.hades.model.task.program.Program;
import hu.szeba.hades.view.task.LockedMenusWrapper;
import hu.szeba.hades.view.task.TerminalArea;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskRunnerThread extends SwingWorker<Integer, String> implements Publisher {

    private Program program;
    private List<InputResultPair> inputResultPairs;
    private int maxByteCount;
    private AtomicBoolean stopFlag;

    private LockedMenusWrapper lockedMenusWrapper;
    private TerminalArea terminalArea;

    public TaskRunnerThread(Program program,
                            List<InputResultPair> inputResultPairs,
                            int maxByteCount,
                            AtomicBoolean stopFlag,
                            LockedMenusWrapper lockedMenusWrapper,
                            TerminalArea terminalArea) {
        this.program = program;
        this.inputResultPairs = inputResultPairs;
        this.maxByteCount = maxByteCount;
        this.stopFlag = stopFlag;

        this.lockedMenusWrapper = lockedMenusWrapper;
        this.terminalArea = terminalArea;
    }

    @Override
    public void customPublish(String message) {
        publish(message);
    }

    @Override
    protected Integer doInBackground() throws IOException, InterruptedException {
        TaskRunnerWork taskRunnerWork = new TaskRunnerWork(program, inputResultPairs, maxByteCount, stopFlag);
        taskRunnerWork.execute(this);
        return 0;
    }

    @Override
    protected void process(List<String> chunks) {
        for (String line : chunks) {
            terminalArea.add(line);
        }
    }

    @Override
    protected void done() {
        lockedMenusWrapper.setBuildEnabled(true);
        lockedMenusWrapper.setBuildAndRunEnabled(true);
        lockedMenusWrapper.setRunEnabled(true);
        lockedMenusWrapper.setStopEnabled(false);
        lockedMenusWrapper.setLockExit(false);
        stopFlag.set(false);
    }

}
