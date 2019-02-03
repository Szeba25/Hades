package hu.szeba.hades.controller.task;

import hu.szeba.hades.model.compiler.CompilerOutput;
import hu.szeba.hades.model.compiler.ProgramCompiler;
import hu.szeba.hades.model.task.CompilerOutputRegister;
import hu.szeba.hades.model.task.data.InputResultPair;
import hu.szeba.hades.model.task.result.Result;
import hu.szeba.hades.model.task.result.ResultDifference;
import hu.szeba.hades.model.task.result.ResultMatcher;
import hu.szeba.hades.view.task.BuildMenuWrapper;

import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskCompilerAndRunnerWorker extends SwingWorker<Integer, String> {

    private ProgramCompiler compiler;
    private CompilerOutputRegister register;
    private List<InputResultPair> inputResultPairs;
    private String[] sources;
    private File path;
    private BuildMenuWrapper buildMenuWrapper;
    private JTextArea terminalArea;
    private CompilerOutput output;

    TaskCompilerAndRunnerWorker(CompilerOutputRegister register, ProgramCompiler compiler,
                                List<InputResultPair> inputResultPairs,
                                String[] sources, File path,
                                BuildMenuWrapper buildMenuWrapper, JTextArea terminalArea) {
        this.compiler = compiler;
        this.register = register;
        this.inputResultPairs = inputResultPairs;
        this.sources = sources;
        this.path = path;
        this.buildMenuWrapper = buildMenuWrapper;
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

        if (output.isReady()) {
            publish("\n> Running program...\n\n");

            // Run
            ResultMatcher matcher = new ResultMatcher();

            for (InputResultPair inputResultPair : inputResultPairs) {
                publish("> Using input: " + inputResultPair.getProgramInput().getFile().getName() + "\n");
                Result result = output.getProgram().run(inputResultPair.getProgramInput());
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
        }
        return 0;
    }

    @Override
    protected void process(List<String> chunks) {
        chunks.forEach(terminalArea::append);
    }

    @Override
    protected void done() {
        buildMenuWrapper.setBuildEnabled(true);
        buildMenuWrapper.setBuildAndRunEnabled(true);
        buildMenuWrapper.setRunEnabled(output.isReady());
        buildMenuWrapper.setStopEnabled(false);
        register.registerCompilerOutput(output);
    }

}
