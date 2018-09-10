package hu.szeba.hades.model.task;

import hu.szeba.hades.model.task.data.TaskData;

import java.io.File;

public class TaskFactory {

    private static final String C = "C";

    public static Task createTask(File campaignDirectory,
                                  File campaignWorkingDirectory,
                                  String taskName) {

        // Get new paths to the task itself.
        File taskDirectory = new File(campaignDirectory, taskName);
        File taskWorkingDirectory = new File(campaignWorkingDirectory, taskName);

        // TODO: Read this from file! (based on the above paths)
        String language = C;

        TaskData taskData = new TaskData(taskDirectory, taskWorkingDirectory, taskName, language);

        switch (language) {
            case C:
                return new TaskC(taskData);
            default:
                return null;
        }

    }

}
