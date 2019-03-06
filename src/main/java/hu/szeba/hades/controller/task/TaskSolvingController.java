package hu.szeba.hades.controller.task;

import hu.szeba.hades.meta.Options;
import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.model.task.data.SourceFile;
import hu.szeba.hades.model.task.data.TaskData;
import hu.szeba.hades.view.task.LockedMenusWrapper;
import hu.szeba.hades.view.task.TaskSolvingView;
import hu.szeba.hades.view.task.TerminalArea;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import javax.swing.*;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskSolvingController implements SourceUpdaterForClosableTabs {

    private Task task;
    private AtomicBoolean stopFlag;

    public TaskSolvingController(Task task) {
        this.task = task;
        this.stopFlag = new AtomicBoolean(false);
    }

    public void setSourceList(TaskSolvingView taskSolvingView) {
        TaskData data = task.getData();
        taskSolvingView.setTaskInstructions(data.getTaskDescription().getLongDescription());
        taskSolvingView.setSourceList(data.copySourceNames(), data.copyReadonlySources(), data.getSyntaxStyle());
        taskSolvingView.setCodeAreaContents(data.getSources());
    }

    public void compile(Map<String, JTextArea> codeAreas,
                        TerminalArea terminalArea, LockedMenusWrapper lockedMenusWrapper) throws IOException {
        // Clear the terminal and save sources first!
        saveSourceContentsWithTerminalOutput(codeAreas, terminalArea);

        // Disable build menu and set stopFlag
        lockedMenusWrapper.initForCompiler();
        stopFlag.set(false);

        // Get reference to TaskData
        TaskData data = task.getData();

        // Start a worker thread to compile the task!
        TaskCompilerThread taskCompilerThread = new TaskCompilerThread(
                task.getProgramCompiler(),
                data.copySourceNamesWithPath(),
                data.copyTaskWorkingDirectory(),
                stopFlag,
                lockedMenusWrapper,
                terminalArea,
                task.getCompilerOutputRegister());
        taskCompilerThread.execute();
    }

    public void compileAndRun(Map<String, JTextArea> codeAreas,
                              TerminalArea terminalArea, LockedMenusWrapper lockedMenusWrapper) throws IOException {
        // Clear the terminal and save sources first!
        saveSourceContentsWithTerminalOutput(codeAreas, terminalArea);

        // Disable build menu and set stopFlag
        lockedMenusWrapper.initForCompilerAndRunner();
        stopFlag.set(false);

        // Get reference to TaskData
        TaskData data = task.getData();

        // Start a worker thread to compile the task!
        TaskCompilerAndRunnerThread taskCompilerAndRunnerThread = new TaskCompilerAndRunnerThread(
                task.getProgramCompiler(),
                data.copySourceNamesWithPath(),
                data.copyTaskWorkingDirectory(),
                data.copyInputResultPairs(),
                Options.getConfigIntData("max_stream_byte_count"),
                stopFlag,
                lockedMenusWrapper,
                terminalArea,
                task.getCompilerOutputRegister());
        taskCompilerAndRunnerThread.execute();
    }

    public void run(TerminalArea terminalArea, LockedMenusWrapper lockedMenusWrapper) {
        // Get task data
        TaskData data = task.getData();

        // Clear terminal, and disable build menu
        terminalArea.clear();
        lockedMenusWrapper.initForRunner();
        stopFlag.set(false);
        // End of permissions

        // Start a worker thread to run the task!
        TaskRunnerThread taskRunnerThread = new TaskRunnerThread(
                task.getCompilerOutputRegister().getCompilerOutput().getProgram(),
                data.copyInputResultPairs(),
                Options.getConfigIntData("max_stream_byte_count"),
                stopFlag,
                lockedMenusWrapper,
                terminalArea);
        taskRunnerThread.execute();
    }

    public void stopCurrentProcess(TerminalArea terminalArea) {
        if (!stopFlag.get()) {
            stopFlag.set(true);
            terminalArea.add("> Stopping running process...\n");
        }
    }

    public void addNewSourceFile(String name, TaskSolvingView taskSolvingView) throws IOException {
        if (task.getData().getSourceByName(name) == null) {
            SourceFile src = task.getData().addSource(name);
            if (src != null) {
                // Add the file!
                taskSolvingView.addSourceFile(name, src.isReadonly(), task.getData().getSyntaxStyle());
            }
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "Source file already exists!");
        }
    }

    public void saveSourceContentsWithTerminalOutput(Map<String, JTextArea> codeAreas, TerminalArea terminalArea)
            throws IOException {
        terminalArea.clear();
        terminalArea.add(">>> Saving sources...\n\n");
        saveSourceContents(codeAreas);
        terminalArea.add("#Save successful!\n\n");
    }

    public void openExistingSourceFile(String name, TaskSolvingView taskSolvingView) {
        SourceFile src = task.getData().getSourceByName(name);
        taskSolvingView.addSourceFile(name, src.isReadonly(), task.getData().getSyntaxStyle());
        taskSolvingView.setCodeAreaContent(name, src.getData());
    }

    public void saveSourceContents(Map<String, JTextArea> codeAreas) throws IOException {
        task.getData().setSourceContents(codeAreas);
        task.getData().saveSources();
    }

    public boolean isSourceReadonly(String name) {
        return task.getData().getSourceByName(name).isReadonly();
    }

    public void deleteSourceFile(String name) throws IOException {
        task.getData().deleteSourceByName(name);
    }

    @Override
    public void updateSourceFileData(String name, JTextArea codeArea) {
        task.getData().getSourceByName(name).setData(codeArea.getText());
    }
}
