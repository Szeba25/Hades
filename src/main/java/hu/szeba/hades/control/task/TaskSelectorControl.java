package hu.szeba.hades.control.task;

import hu.szeba.hades.model.campaign.Campaign;
import hu.szeba.hades.model.task.Task;

public class TaskSelectorControl {

    private Campaign campaign;

    public TaskSelectorControl(Campaign campaign) {
        this.campaign = campaign;
    }

    public Task createTask(String taskName) {
        return campaign.createTask(taskName);
    }

    public String[] getTaskNames() {
        return campaign.getTaskNames();
    }

}
