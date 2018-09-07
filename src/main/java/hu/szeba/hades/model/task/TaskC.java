package hu.szeba.hades.model.task;

import java.io.File;

public class TaskC extends Task {

    public TaskC(File campaignDirectory, File campaignWorkingDirectory, String taskName) {
        super(campaignDirectory, campaignWorkingDirectory, taskName);
    }

    @Override
    public int getTaskType() {
        return TaskFactory.TYPE_C;
    }

}
