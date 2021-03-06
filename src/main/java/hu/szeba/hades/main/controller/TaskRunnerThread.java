package hu.szeba.hades.main.controller;

import hu.szeba.hades.main.meta.TaskSolverAgent;
import hu.szeba.hades.main.model.task.data.InputResultPair;
import hu.szeba.hades.main.model.task.program.Program;
import hu.szeba.hades.main.view.components.LockedMenusWrapper;
import hu.szeba.hades.main.view.components.TerminalArea;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskRunnerThread extends SwingWorker<Integer, String> implements Publisher {

    private TaskSolverAgent agent;
    private Program program;
    private List<InputResultPair> inputResultPairs;
    private int maxByteCount;
    private AtomicBoolean stopFlag;

    private LockedMenusWrapper lockedMenusWrapper;
    private TerminalArea terminalArea;

    public TaskRunnerThread(TaskSolverAgent agent,
                            Program program,
                            List<InputResultPair> inputResultPairs,
                            int maxByteCount,
                            AtomicBoolean stopFlag,
                            LockedMenusWrapper lockedMenusWrapper,
                            TerminalArea terminalArea) {
        this.agent = agent;
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
        TaskRunnerWork taskRunnerWork = new TaskRunnerWork(agent, program, inputResultPairs, maxByteCount, stopFlag);
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
        lockedMenusWrapper.finishForRunner();
        stopFlag.set(false);
    }

}
