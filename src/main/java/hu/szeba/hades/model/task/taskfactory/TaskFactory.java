package hu.szeba.hades.model.task.taskfactory;

import hu.szeba.hades.meta.Options;
import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.model.task.data.TaskData;

import java.io.File;

public interface TaskFactory {

    Task getTask(String taskName);

}