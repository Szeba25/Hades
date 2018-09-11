package hu.szeba.hades.controller.task;

import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.view.task.TaskSolvingView;

public class TaskSolvingController {

    private TaskSolvingView view;
    private Task task;

    public TaskSolvingController(TaskSolvingView view, Task task) {
        this.view = view;
        this.task = task;
    }

}
