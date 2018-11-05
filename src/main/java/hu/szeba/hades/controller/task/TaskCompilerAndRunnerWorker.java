package hu.szeba.hades.controller.task;

import hu.szeba.hades.model.compiler.CompilerOutput;
import hu.szeba.hades.model.compiler.ProgramCompiler;
import hu.szeba.hades.model.task.CompilerOutputRegister;
import hu.szeba.hades.model.task.result.Result;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class TaskCompilerAndRunnerWorker extends SwingWorker<Integer, String> {

    private ProgramCompiler compiler;
    private CompilerOutputRegister register;
    private String[] sources;
    private File path;
    private JMenu disabledBuildMenu;
    private JTextArea terminalArea;
    private CompilerOutput output;

    TaskCompilerAndRunnerWorker(CompilerOutputRegister register, ProgramCompiler compiler,
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
        // Compile
        output = compiler.compile(sources, path);
        for (String message : output.getCompilerMessages()) {
            publish(message + "\n");
        }
        publish("\n> Running program...\n\n");
        // Run
        if (output.isReady()) {
            Result result = output.getProgram().run(null);
            for (int i = 0; i < result.getResultLineCount(); i++) {
                publish(result.getResultLine(i).getData() + "\n");
            }
        } else {
            publish("Cannot to run program: compilation failed");
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
