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
import java.io.IOException;
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
    private int maxByteCount;
    private AtomicBoolean stopFlag;
    private CompilerOutput output;

    TaskCompilerAndRunnerWorker(CompilerOutputRegister register, ProgramCompiler compiler,
                                List<InputResultPair> inputResultPairs,
                                String[] sources, File path,
                                BuildMenuWrapper buildMenuWrapper, JTextArea terminalArea,
                                int maxByteCount, AtomicBoolean stopFlag) {
        this.compiler = compiler;
        this.register = register;
        this.inputResultPairs = inputResultPairs;
        this.sources = sources;
        this.path = path;
        this.buildMenuWrapper = buildMenuWrapper;
        this.terminalArea = terminalArea;
        this.maxByteCount = maxByteCount;
        this.stopFlag = stopFlag;
        this.output = null;
    }

    @Override
    protected Integer doInBackground() throws IOException, InterruptedException {
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
                Result result = output.getProgram().run(inputResultPair.getProgramInput(), maxByteCount, stopFlag);

                if (stopFlag.get()) {
                    publish("> Process force closed!\n");
                    return 0;
                }

                if (!result.anyInputPresent()) {
                    publish("> No response...\n\n");
                } else {
                    for (int i = 0; i < result.getDebugLineCount(); i++) {
                        publish("@" + (i + 1) + ". " + result.getDebugLineByIndex(i) + "\n");
                    }
                    if (result.getDebugLineCount() > 0) {
                        publish("\n");
                    }
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
                    if (matcher.getDifferencesSize() > 0) {
                        publish("\n");
                    }
                }
            }

            publish("... End of running!");
        }
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
        buildMenuWrapper.setBuildEnabled(true);
        buildMenuWrapper.setBuildAndRunEnabled(true);
        buildMenuWrapper.setRunEnabled(output.isReady());
        buildMenuWrapper.setStopEnabled(false);
        register.registerCompilerOutput(output);
        stopFlag.set(false);
    }

}
