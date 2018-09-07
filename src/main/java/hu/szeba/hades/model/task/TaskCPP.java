package hu.szeba.hades.model.task;

import java.io.File;

public class TaskCPP extends Task {

    public TaskCPP(File campaignDirectory, File campaignWorkingDirectory, String taskName) {
        super(campaignDirectory, campaignWorkingDirectory, taskName);
    }

    @Override
    public int getTaskType() {
        return TaskFactory.TYPE_CPP;
    }
}
