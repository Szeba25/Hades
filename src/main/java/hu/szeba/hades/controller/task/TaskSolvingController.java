package hu.szeba.hades.controller.task;

import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.view.task.TaskSolvingView;

import java.io.IOException;

public class TaskSolvingController {

    private TaskSolvingView taskSolvingView;
    private Task task;

    public TaskSolvingController(TaskSolvingView taskSolvingView, Task task) {
        this.taskSolvingView = taskSolvingView;
        this.taskSolvingView.setSourceList(task.getSourceFileNameList(), task.getSyntaxStyle());
        this.taskSolvingView.setCodeAreaContents(task.getSourceFiles());
        this.task = task;
    }

    public void compile() throws IOException {
        // Set the sources content and save sources on EDT
        task.setSourceContents(taskSolvingView.getCodeAreas());
        task.saveSources();

        // Clear terminal, and disable build menu
        taskSolvingView.getTerminalArea().setText("");
        taskSolvingView.getBuildMenu().setEnabled(false);

        // Start a worker thread to compile the task!
        TaskCompilerWorker taskCompilerWorker = new TaskCompilerWorker(
                task, // Passed as register interface type!
                task.getProgramCompiler(),
                task.getSourceFileNameList(),
                task.getTaskWorkingDirectoryCopy(),
                taskSolvingView.getBuildMenu(),
                taskSolvingView.getTerminalArea());
        taskCompilerWorker.execute();
    }

    public void compileAndRun() throws IOException {
        // Set the sources content and save sources on EDT
        task.setSourceContents(taskSolvingView.getCodeAreas());
        task.saveSources();

        // Clear terminal, and disable build menu
        taskSolvingView.getTerminalArea().setText("");
        taskSolvingView.getBuildMenu().setEnabled(false);

        // Start a worker thread to compile the task!
        TaskCompilerAndRunnerWorker taskCompilerAndRunnerWorker = new TaskCompilerAndRunnerWorker(
                task, // Passed as register interface type!
                task.getProgramCompiler(),
                task.getSourceFileNameList(),
                task.getTaskWorkingDirectoryCopy(),
                taskSolvingView.getBuildMenu(),
                taskSolvingView.getTerminalArea());
        taskCompilerAndRunnerWorker.execute();
    }

    public void run() {
        // Clear terminal, and disable build menu
        taskSolvingView.getTerminalArea().setText("");
        taskSolvingView.getBuildMenu().setEnabled(false);

        // Start a worker thread to run the task!
        TaskRunningWorker taskRunningWorker = new TaskRunningWorker(
                task.getCompilerOutput().getProgram(),
                taskSolvingView.getBuildMenu(),
                taskSolvingView.getTerminalArea());
        taskRunningWorker.execute();
    }
}
