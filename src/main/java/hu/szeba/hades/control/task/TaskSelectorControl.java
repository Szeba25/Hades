package hu.szeba.hades.control.task;

import hu.szeba.hades.model.task.Task;

public interface TaskSelectorControl {

    String[] getTaskNames();
    Task createTask(String taskName);

}
