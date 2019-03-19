package hu.szeba.hades.main.controller.task;

import hu.szeba.hades.main.model.compiler.CompilerOutput;
import hu.szeba.hades.main.model.compiler.ProgramCompiler;
import hu.szeba.hades.main.model.task.CompilerOutputRegister;
import hu.szeba.hades.main.view.task.LockedMenusWrapper;
import hu.szeba.hades.main.view.task.TerminalArea;

import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskCompilerThread extends SwingWorker<Integer, String> implements Publisher {

    private ProgramCompiler compiler;
    private String[] sources;
    private File path;
    private AtomicBoolean stopFlag;

    private LockedMenusWrapper lockedMenusWrapper;
    private TerminalArea terminalArea;
    private CompilerOutputRegister register;

    public TaskCompilerThread(ProgramCompiler compiler,
                              String[] sources,
                              File path,
                              AtomicBoolean stopFlag,
                              LockedMenusWrapper lockedMenusWrapper,
                              TerminalArea terminalArea,
                              CompilerOutputRegister register) {
        this.compiler = compiler;
        this.sources = sources;
        this.path = path;

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
        lockedMenusWrapper.finishForCompiler(register.getCompilerOutput().isReady());
        stopFlag.set(false);
    }

}
