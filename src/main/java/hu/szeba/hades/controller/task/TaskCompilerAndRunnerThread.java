package hu.szeba.hades.controller.task;

import hu.szeba.hades.meta.TaskSolverAgent;
import hu.szeba.hades.model.compiler.CompilerOutput;
import hu.szeba.hades.model.compiler.ProgramCompiler;
import hu.szeba.hades.model.task.CompilerOutputRegister;
import hu.szeba.hades.model.task.data.InputResultPair;
import hu.szeba.hades.view.task.LockedMenusWrapper;
import hu.szeba.hades.view.task.TerminalArea;

import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskCompilerAndRunnerThread extends SwingWorker<Integer, String> implements Publisher {

    private TaskSolverAgent agent;
    private String taskIdentifierString;
    private ProgramCompiler compiler;
    private String[] sources;
    private File path;
    private List<InputResultPair> inputResultPairs;
    private int maxByteCount;
    private AtomicBoolean stopFlag;

    private LockedMenusWrapper lockedMenusWrapper;
    private TerminalArea terminalArea;
    private CompilerOutputRegister register;

    public TaskCompilerAndRunnerThread(TaskSolverAgent agent,
                                       String taskIdentifierString,
                                       ProgramCompiler compiler,
                                       String[] sources,
                                       File path,
                                       List<InputResultPair> inputResultPairs,
                                       int maxByteCount,
                                       AtomicBoolean stopFlag,
                                       LockedMenusWrapper lockedMenusWrapper,
                                       TerminalArea terminalArea,
                                       CompilerOutputRegister register) {
        this.agent = agent;
        this.taskIdentifierString = taskIdentifierString;
        this.compiler = compiler;
        this.sources = sources;
        this.path = path;
        this.inputResultPairs = inputResultPairs;
        this.maxByteCount = maxByteCount;
        this.stopFlag = stopFlag;

        this.lockedMenusWrapper = lockedMenusWrapper;
        this.terminalArea = terminalArea;
        this.register = register;
    }

    @Override
    public void customPublish(String message) {
        publish(message);
    }

    @Override
    protected Integer doInBackground() throws Exception {
        TaskCompilerWork taskCompilerWork = new TaskCompilerWork(compiler, sources, path);
        taskCompilerWork.execute(this);
        CompilerOutput output = taskCompilerWork.getOutput();

        // Run only if output is ready!
        if (output.isReady()) {
            publish("\n"); // To separate the two runs!
            TaskRunnerWork taskRunnerWork =
                    new TaskRunnerWork(agent, taskIdentifierString, output.getProgram(),
                            inputResultPairs, maxByteCount, stopFlag);
            taskRunnerWork.execute(this);
        }

        // Register the output!
        register.setCompilerOutput(output);
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
        lockedMenusWrapper.finishForCompilerAndRunner(register.getCompilerOutput().isReady());
        stopFlag.set(false);
    }

}
