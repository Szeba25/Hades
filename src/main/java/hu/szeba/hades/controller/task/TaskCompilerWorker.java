package hu.szeba.hades.controller.task;

import hu.szeba.hades.model.compiler.CompilerOutput;
import hu.szeba.hades.model.compiler.ProgramCompiler;
import hu.szeba.hades.model.task.CompilerOutputRegister;
import hu.szeba.hades.model.task.Task;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class TaskCompilerWorker extends SwingWorker<Integer, String> {

    private ProgramCompiler compiler;
    private CompilerOutputRegister register;
    private String[] sources;
    private File path;
    private JMenu disabledBuildMenu;
    private JTextArea terminalArea;
    private CompilerOutput output;

    TaskCompilerWorker(CompilerOutputRegister register, ProgramCompiler compiler,
                       String[] sources, File path,
                       JMenu disabledBuildMenu, JTextArea terminalArea) {
        this.compiler = compiler;
        this.register = register;
        this.sources = sources;
        this.path = path;
        this.disabledBuildMenu = disabledBuildMenu;
        this.terminalArea = terminalArea;
        this.output = null;
    }

    @Override
    protected Integer doInBackground() throws Exception {
        publish("> Compilation started...\n\n");
        output = compiler.compile(sources, path);
        for (String message : output.getCompilerMessages()) {
            publish(message + "\n");
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
        disabledBuildMenu.getItem(4).setEnabled(output.isReady());
        register.registerCompilerOutput(output);
    }

}
