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

        // Clear terminal, and disable compile menu
        taskSolvingView.getTerminalArea().setText("Compilation started...\n");
        taskSolvingView.getBuildMenu().setEnabled(false);

        // Start a worker thread to compile the task!
        TaskCompilerWorker taskCompilerWorker = new TaskCompilerWorker(task,
                taskSolvingView.getBuildMenu(),
                taskSolvingView.getTerminalArea());
        taskCompilerWorker.execute();
    }

    public void run() {
        // Clear terminal, and disable compile menu
        taskSolvingView.getTerminalArea().setText("Running program...\n");
        taskSolvingView.getBuildMenu().setEnabled(false);

        // Start a worker thread to run the task!
        TaskRunningWorker taskRunningWorker = new TaskRunningWorker(task,
                taskSolvingView.getBuildMenu(),
                taskSolvingView.getTerminalArea());
        taskRunningWorker.execute();
    }

}
