package hu.szeba.hades.controller.task;

import hu.szeba.hades.model.compiler.CompilerOutput;
import hu.szeba.hades.model.compiler.ProgramCompiler;
import hu.szeba.hades.model.task.CompilerOutputRegister;
import hu.szeba.hades.model.task.data.Solution;
import hu.szeba.hades.model.task.result.Result;
import hu.szeba.hades.model.task.result.ResultDifference;
import hu.szeba.hades.model.task.result.ResultMatcher;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class TaskCompilerAndRunnerWorker extends SwingWorker<Integer, String> {

    private ProgramCompiler compiler;
    private CompilerOutputRegister register;
    private List<Solution> solutions;
    private String[] sources;
    private File path;
    private JMenu disabledBuildMenu;
    private JTextArea terminalArea;
    private CompilerOutput output;

    TaskCompilerAndRunnerWorker(CompilerOutputRegister register, ProgramCompiler compiler,
                                List<Solution> solutions,
                                String[] sources, File path,
                                JMenu disabledBuildMenu, JTextArea terminalArea) {
        this.compiler = compiler;
        this.register = register;
        this.solutions = solutions;
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
        ResultMatcher matcher = new ResultMatcher();

        for (Solution solution : solutions) {
            publish("> Using input: " + solution.getProgramInput().getFile().getName() + "\n");
            Result result = output.getProgram().run(solution.getProgramInput());
            for (int i = 0; i < result.getResultLineCount(); i++) {
                publish(i + ". " + result.getResultLine(i).getData() + "\n");
            }
            publish("\n");
            matcher.match(result, solution.getDesiredResult());
            for (int i = 0; i < matcher.getDifferencesSize(); i++) {
                ResultDifference diff = matcher.getDifference(i);
                publish("* difference at line: " + diff.getLineNumber());
                publish(". [" + diff.getFirstLine().getData() + "] should be ["
                        + diff.getSecondLine().getData() + "]\n");
            }
            publish("\n");
        }

        publish("... End of running!");
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
