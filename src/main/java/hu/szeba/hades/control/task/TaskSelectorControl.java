package hu.szeba.hades.control.task;

import hu.szeba.hades.model.campaign.Campaign;

public class TaskSelectorControl {

    private Campaign campaign;

    public TaskSelectorControl(Campaign campaign) {
        this.campaign = campaign;
    }

    public String[] getTaskNames() {
        return campaign.getTaskNames();
    }
}
