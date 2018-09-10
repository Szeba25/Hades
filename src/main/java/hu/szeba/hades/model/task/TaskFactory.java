package hu.szeba.hades.model.task;

import hu.szeba.hades.model.task.data.TaskData;

import java.io.File;

public class TaskFactory {

    private static final String C = "C";

    public static Task createTask(File campaignDirectory,
                                  File campaignWorkingDirectory,
                                  String taskName) {

        TaskData taskData = new TaskData(
                new File(campaignDirectory, taskName),
                new File(campaignWorkingDirectory, taskName),
                taskName);

        // TODO: Read this from file!
        String language = C;

        switch (language) {
            case C:
                return new TaskC(taskData);
            default:
                return null;
        }

    }

}
