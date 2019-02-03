package hu.szeba.hades.controller.task;

import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.model.task.data.TaskData;
import hu.szeba.hades.view.task.BuildMenuWrapper;
import hu.szeba.hades.view.task.TaskSolvingView;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import javax.swing.*;
import java.io.IOException;
import java.util.Map;

public class TaskSolvingController {

    private Task task;

    public TaskSolvingController(Task task) {
        this.task = task;
    }

    public void setSourceList(TaskSolvingView taskSolvingView) {
        TaskData data = task.getData();
        taskSolvingView.setTaskInstructions(data.getTaskDescription().getLongDescription());
        taskSolvingView.setSourceList(data.copySourceNames(), data.getSyntaxStyle());
        taskSolvingView.setCodeAreaContents(data.getSources());
    }

    public void compile(Map<String, RSyntaxTextArea> codeAreas,
                        JTextArea terminalArea, BuildMenuWrapper buildMenuWrapper) throws IOException {
        // Set the sources content and save sources on EDT
        TaskData data = task.getData();
        data.setSourceContents(codeAreas);
        data.saveSources();

        // Clear terminal, and disable build menu
        terminalArea.setText("");
        buildMenuWrapper.setBuildEnabled(false);
        buildMenuWrapper.setBuildAndRunEnabled(false);
        buildMenuWrapper.setRunEnabled(false);

        // Start a worker thread to compile the task!
        TaskCompilerWorker taskCompilerWorker = new TaskCompilerWorker(
                task, // Passed as register interface type!
                task.getProgramCompiler(),
                data.copySourceNamesWithPath(),
                data.copyTaskWorkingDirectory(),
                buildMenuWrapper,
                terminalArea);
        taskCompilerWorker.execute();
    }

    public void compileAndRun(Map<String, RSyntaxTextArea> codeAreas,
                              JTextArea terminalArea, BuildMenuWrapper buildMenuWrapper) throws IOException {
        // Set the sources content and save sources on EDT
        TaskData data = task.getData();
        data.setSourceContents(codeAreas);
        data.saveSources();

        // Clear terminal, and disable build menu
        terminalArea.setText("");
        buildMenuWrapper.setBuildEnabled(false);
        buildMenuWrapper.setBuildAndRunEnabled(false);
        buildMenuWrapper.setRunEnabled(false);
        buildMenuWrapper.setStopEnabled(true);

        // Start a worker thread to compile the task!
        TaskCompilerAndRunnerWorker taskCompilerAndRunnerWorker = new TaskCompilerAndRunnerWorker(
                task, // Passed as register interface type!
                task.getProgramCompiler(),
                data.copyInputResultPairs(),
                data.copySourceNamesWithPath(),
                data.copyTaskWorkingDirectory(),
                buildMenuWrapper,
                terminalArea);
        taskCompilerAndRunnerWorker.execute();
    }

    public void run(JTextArea terminalArea, BuildMenuWrapper buildMenuWrapper) {
        // Clear terminal, and disable build menu
        terminalArea.setText("");
        buildMenuWrapper.setBuildEnabled(false);
        buildMenuWrapper.setBuildAndRunEnabled(false);
        buildMenuWrapper.setRunEnabled(false);
        buildMenuWrapper.setStopEnabled(true);

        // Start a worker thread to run the task!
        TaskRunnerWorker taskRunnerWorker = new TaskRunnerWorker(
                task.getCompilerOutput().getProgram(),
                task.getData().copyInputResultPairs(),
                buildMenuWrapper,
                terminalArea);
        taskRunnerWorker.execute();
    }

    public void stopCachedProcess() {

    }

}
