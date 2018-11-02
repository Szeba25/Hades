package hu.szeba.hades.controller.task;

import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.view.task.TaskSolvingView;

import java.io.IOException;

public class TaskSolvingController {

    private TaskSolvingView taskSolvingView;
    private Task task;

    public TaskSolvingController(TaskSolvingView taskSolvingView, Task task) {
        this.taskSolvingView = taskSolvingView;
        this.taskSolvingView.setCodeAreaContent(task.getFirstSourceContent());
        this.taskSolvingView.setSourceList(task.getSourceList());
        this.task = task;
    }

    public void compile() throws IOException {
        // Set the sources content and save sources on EDT
        task.setFirstSourceContent(taskSolvingView.getCodeAreaContent());
        task.saveFirstSource();

        // Clear terminal, and disable compile menu
        taskSolvingView.getTerminalArea().setText("Compilation started...\n");
        taskSolvingView.getBuildMenu().setEnabled(false);

        // Start a worker thread to compile the task!
        TaskCompilerWorker taskCompilerWorker = new TaskCompilerWorker(task,
                taskSolvingView.getBuildMenu(),
                taskSolvingView.getTerminalArea());
        taskCompilerWorker.execute();
    }

}
