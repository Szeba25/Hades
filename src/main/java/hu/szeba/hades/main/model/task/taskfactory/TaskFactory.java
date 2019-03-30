package hu.szeba.hades.main.model.task.taskfactory;

import hu.szeba.hades.main.io.DescriptionFile;
import hu.szeba.hades.main.meta.User;
import hu.szeba.hades.main.model.task.Task;
import hu.szeba.hades.main.model.task.data.MissingResultFileException;

import java.io.IOException;

public interface TaskFactory {

    Task getTask(User user, String courseId, String modeId, String taskCollectionId, String taskId,
                 DescriptionFile taskDescription, boolean continueTask)
            throws IOException, MissingResultFileException;

}
