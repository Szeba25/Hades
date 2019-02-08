package hu.szeba.hades.controller.task;

import hu.szeba.hades.model.compiler.CompilerOutput;
import hu.szeba.hades.model.compiler.ProgramCompiler;
import hu.szeba.hades.model.task.CompilerOutputRegister;
import hu.szeba.hades.model.task.data.InputResultPair;
import hu.szeba.hades.view.task.BuildMenuWrapper;
import hu.szeba.hades.view.task.TerminalArea;

import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskCompilerAndRunnerThread extends SwingWorker<Integer, String> implements Publisher {

    private ProgramCompiler compiler;
    private String[] sources;
    private File path;
    private List<InputResultPair> inputResultPairs;
    private int maxByteCount;
    private AtomicBoolean stopFlag;

    private BuildMenuWrapper buildMenuWrapper;
    private TerminalArea terminalArea;
    private CompilerOutputRegister register;

    public TaskCompilerAndRunnerThread(ProgramCompiler compiler,
                                       String[] sources,
                                       File path,
                                       List<InputResultPair> inputResultPairs,
                                       int maxByteCount,
                                       AtomicBoolean stopFlag,
                                       BuildMenuWrapper buildMenuWrapper,
                                       TerminalArea terminalArea,
                                       CompilerOutputRegister register) {
        this.compiler = compiler;
        this.sources = sources;
        this.path = path;
        this.inputResultPairs = inputResultPairs;
        this.maxByteCount = maxByteCount;
        this.stopFlag = stopFlag;

        this.buildMenuWrapper = buildMenuWrapper;
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
            TaskRunnerWork taskRunnerWork = new TaskRunnerWork(output.getProgram(), inputResultPairs, maxByteCount, stopFlag);
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
        buildMenuWrapper.setBuildEnabled(true);
        buildMenuWrapper.setBuildAndRunEnabled(true);
        buildMenuWrapper.setRunEnabled(register.getCompilerOutput().isReady());
        buildMenuWrapper.setStopEnabled(false);
        stopFlag.set(false);
    }

}
