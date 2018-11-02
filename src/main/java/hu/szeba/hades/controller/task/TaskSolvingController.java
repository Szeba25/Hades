package hu.szeba.hades.controller.task;

import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.view.task.TaskSolvingView;

import java.io.IOException;

public class TaskSolvingController {

    private TaskSolvingView taskSolvingView;
    private Task task;

    public TaskSolvingController(TaskSolvingView taskSolvingView, Task task) {
        this.taskSolvingView = taskSolvingView;
        this.taskSolvingView.setTestSourceEditing(task.getFirstSourceContent());
        this.task = task;
    }

    public void compile() throws IOException, InterruptedException {
        this.task.setFirstSourceContent(this.taskSolvingView.getSourceContent());
        this.task.compile();
    }

}
