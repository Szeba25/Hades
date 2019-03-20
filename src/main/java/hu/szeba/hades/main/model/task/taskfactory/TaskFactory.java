package hu.szeba.hades.main.model.task.taskfactory;

import hu.szeba.hades.main.meta.User;
import hu.szeba.hades.main.model.task.Task;
import hu.szeba.hades.main.model.task.data.MissingResultFileException;
import hu.szeba.hades.main.model.task.data.TaskDescription;

import java.io.IOException;

public interface TaskFactory {

    Task getTask(User user, String courseId, String modeId, String taskCollectionId, String taskId,
                 TaskDescription taskDescription, boolean continueTask)
            throws IOException, MissingResultFileException;

}
