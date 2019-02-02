package hu.szeba.hades.controller.task;

import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.model.task.data.TaskData;
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
                        JTextArea terminalArea, JMenu buildMenu) throws IOException {
        // Set the sources content and save sources on EDT
        TaskData data = task.getData();
        data.setSourceContents(codeAreas);
        data.saveSources();

        // Clear terminal, and disable build menu
        terminalArea.setText("");
        buildMenu.setEnabled(false);

        // Start a worker thread to compile the task!
        TaskCompilerWorker taskCompilerWorker = new TaskCompilerWorker(
                task, // Passed as register interface type!
                task.getProgramCompiler(),
                data.copySourceNamesWithPath(),
                data.copyTaskWorkingDirectory(),
                buildMenu,
                terminalArea);
        taskCompilerWorker.execute();
    }

    public void compileAndRun(Map<String, RSyntaxTextArea> codeAreas,
                              JTextArea terminalArea, JMenu buildMenu) throws IOException {
        // Set the sources content and save sources on EDT
        TaskData data = task.getData();
        data.setSourceContents(codeAreas);
        data.saveSources();

        // Clear terminal, and disable build menu
        terminalArea.setText("");
        buildMenu.setEnabled(false);

        // Start a worker thread to compile the task!
        TaskCompilerAndRunnerWorker taskCompilerAndRunnerWorker = new TaskCompilerAndRunnerWorker(
                task, // Passed as register interface type!
                task.getProgramCompiler(),
                data.copyInputResultPairs(),
                data.copySourceNamesWithPath(),
                data.copyTaskWorkingDirectory(),
                buildMenu,
                terminalArea);
        taskCompilerAndRunnerWorker.execute();
    }

    public void run(JTextArea terminalArea, JMenu buildMenu) {
        // Clear terminal, and disable build menu
        terminalArea.setText("");
        buildMenu.setEnabled(false);

        // Start a worker thread to run the task!
        TaskRunningWorker taskRunningWorker = new TaskRunningWorker(
                task.getCompilerOutput().getProgram(),
                task.getData().copyInputResultPairs(),
                buildMenu,
                terminalArea);
        taskRunningWorker.execute();
    }
}
