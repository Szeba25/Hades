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
        this.task = task;
    }

    public void compile() {
        // Set the sources content
        task.setFirstSourceContent(taskSolvingView.getCodeAreaContent());

        // Start a worker thread to compile the task!
        taskSolvingView.getTerminalArea().setText("");
        taskSolvingView.getCompileMenuItem().setEnabled(false);
        TaskCompilerWorker taskCompilerWorker = new TaskCompilerWorker(task,
                taskSolvingView.getCompileMenuItem(),
                taskSolvingView.getTerminalArea());
        taskCompilerWorker.execute();
    }

}
