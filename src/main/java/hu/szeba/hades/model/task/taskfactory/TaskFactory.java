package hu.szeba.hades.model.task.taskfactory;

import hu.szeba.hades.meta.User;
import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.model.task.data.MissingResultFileException;
import hu.szeba.hades.model.task.data.TaskDescription;
import hu.szeba.hades.model.task.data.TaskStory;

import java.io.IOException;

public interface TaskFactory {

    Task getTask(User user, String courseName, String topicName, String taskId,
                 TaskDescription taskDescription, TaskStory taskStory, boolean continueTask) throws IOException, MissingResultFileException;

}
