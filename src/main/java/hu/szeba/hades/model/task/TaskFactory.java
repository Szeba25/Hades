package hu.szeba.hades.model.task;

import java.io.File;

public class TaskFactory {

    public static final int TYPE_C = 0;
    public static final int TYPE_CPP = 1;

    public static Task createTask(File campaignDirectory, File campaignWorkingDirectory, String taskName) {

        File taskDirectory = new File(campaignDirectory, taskName);
        File taskWorkingDirectory = new File(campaignWorkingDirectory, taskName);

        int taskType = TYPE_C; // TODO: Replace with proper type loading!

        switch (taskType) {
            case TYPE_C:
                return new TaskC(taskDirectory, taskWorkingDirectory, taskName);
            case TYPE_CPP:
                return new TaskCPP(taskDirectory, taskWorkingDirectory, taskName);
            default:
                return null;
        }

    }

}
