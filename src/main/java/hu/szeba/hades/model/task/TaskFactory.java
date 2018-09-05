package hu.szeba.hades.model.task;

import java.io.File;

public class TaskFactory {

    public static Task createTask(File campaignDirectory, String taskName) {
        return new Task(campaignDirectory, taskName);
    }

}
