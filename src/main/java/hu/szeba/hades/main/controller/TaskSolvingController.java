package hu.szeba.hades.main.controller;

import hu.szeba.hades.main.meta.Options;
import hu.szeba.hades.main.model.task.Task;
import hu.szeba.hades.main.io.EditableTextFile;
import hu.szeba.hades.main.view.components.LockedMenusWrapper;
import hu.szeba.hades.main.view.TaskSolvingView;
import hu.szeba.hades.main.view.components.TerminalArea;

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

    public void setTaskViewContent(TaskSolvingView taskSolvingView) {
        taskSolvingView.setTaskInstructions(task.getTaskDescription().getInstructions());
        taskSolvingView.setTaskStory(task.getTaskDescription().getStory());
        taskSolvingView.setSourceList(task.copySourceNames(), task.copyReadonlySources(), task.getSyntaxStyle());
        taskSolvingView.setCodeAreaContents(task.getSources());
    }

    public void compile(Map<String, JTextArea> codeAreas,
                        TerminalArea terminalArea, LockedMenusWrapper lockedMenusWrapper) throws IOException {
        // Clear the terminal and save sources first!
        saveSourceContentsWithTerminalOutput(codeAreas, terminalArea);

        // Disable build menu and set stopFlag
        lockedMenusWrapper.initForCompiler();
        stopFlag.set(false);

        // Start a worker thread to compile the task!
        TaskCompilerThread taskCompilerThread = new TaskCompilerThread(
                task.getProgramCompiler(),
                task.copySourceNamesWithPath(),
                task.copyTaskWorkingDirectory(),
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

        // Start a worker thread to compile the task!
        TaskCompilerAndRunnerThread taskCompilerAndRunnerThread = new TaskCompilerAndRunnerThread(
                task.getAgent(),
                task.getProgramCompiler(),
                task.copySourceNamesWithPath(),
                task.copyTaskWorkingDirectory(),
                task.copyInputResultPairs(),
                Options.getConfigIntData("max_stream_byte_count"),
                stopFlag,
                lockedMenusWrapper,
                terminalArea,
                task.getCompilerOutputRegister());
        taskCompilerAndRunnerThread.execute();
    }

    public void run(TerminalArea terminalArea, LockedMenusWrapper lockedMenusWrapper) {
        // Clear terminal, and disable build menu
        terminalArea.clear();
        lockedMenusWrapper.initForRunner();
        stopFlag.set(false);
        // End of permissions

        // Start a worker thread to run the task!
        TaskRunnerThread taskRunnerThread = new TaskRunnerThread(
                task.getAgent(),
                task.getCompilerOutputRegister().getCompilerOutput().getProgram(),
                task.copyInputResultPairs(),
                Options.getConfigIntData("max_stream_byte_count"),
                stopFlag,
                lockedMenusWrapper,
                terminalArea);
        taskRunnerThread.execute();
    }

    public void stopCurrentProcess(TerminalArea terminalArea) {
        if (!stopFlag.get()) {
            stopFlag.set(true);
            terminalArea.add("> Stop running process...\n");
        }
    }

    public void addNewSourceFile(String name, TaskSolvingView taskSolvingView) throws IOException {
        if (task.getSourceByName(name) == null) {
            EditableTextFile src = task.addSource(name);
            if (src != null) {
                // Add the file!
                taskSolvingView.addSourceFile(name, src.isReadonly(), task.getSyntaxStyle());
            }
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "Source file with this name already exists!");
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
        EditableTextFile src = task.getSourceByName(name);
        taskSolvingView.addSourceFile(name, src.isReadonly(), task.getSyntaxStyle());
        taskSolvingView.setCodeAreaContent(name, src.getData());
    }

    public void saveSourceContents(Map<String, JTextArea> codeAreas) throws IOException {
        task.setSourceContents(codeAreas);
        task.saveSources();
    }

    public boolean isSourceReadonly(String name) {
        return task.getSourceByName(name).isReadonly();
    }

    public void deleteSourceFile(String name, TaskSolvingView taskSolvingView) throws IOException {
        task.deleteSourceByName(name);
        taskSolvingView.deleteSourceFile(name);
    }

    public void renameSourceFile(String oldName, String newName, TaskSolvingView taskSolvingView) throws IOException {
        if (task.getSourceByName(newName) == null) {
            task.renameSource(oldName, newName);
            taskSolvingView.renameSourceFile(oldName, newName);
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "Source file with this name already exists!");
        }
    }

    @Override
    public void updateSourceFileData(String name, JTextArea codeArea) {
        task.getSourceByName(name).setData(codeArea.getText());
    }

}
