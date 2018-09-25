package hu.szeba.hades.model.task;

import hu.szeba.hades.model.task.data.TaskData;

import java.io.File;

public interface TaskFactory {

    Task getTask(File campaignDirectory, File campaignWorkingDirectory, String taskName);

}
