package hu.szeba.hades.controller.task;

import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.view.task.TaskSolvingView;

import java.io.IOException;

public class TaskSolvingController {

    private TaskSolvingView taskSolvingView;
    private Task task;

    public TaskSolvingController(TaskSolvingView taskSolvingView, Task task) {
        this.taskSolvingView = taskSolvingView;
        this.taskSolvingView.setSourceList(task.getSourceList());
        this.taskSolvingView.setCodeAreaContent(task.getSourceContent(0));
        this.task = task;
    }

    public void compile(int selected) throws IOException {
        // Set the sources content and save sources on EDT
        task.setSourceContent(selected, taskSolvingView.getCodeAreaContent());
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

    public void changeFile(int selected, int previous) {
        task.setSourceContent(previous, taskSolvingView.getCodeAreaContent());
        taskSolvingView.setCodeAreaContent(task.getSourceContent(selected));
    }
}
