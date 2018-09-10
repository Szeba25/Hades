package hu.szeba.hades.control.task;

import hu.szeba.hades.model.task.Task;

public class TaskSelectorControl {

    private TaskSelectorControlMethods campaign;

    public TaskSelectorControl(TaskSelectorControlMethods campaign) {
        this.campaign = campaign;
    }

    public Task createTask(String taskName) {
        return campaign.createTask(taskName);
    }

}
