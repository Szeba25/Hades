package hu.szeba.hades.model.task.taskfactory;

import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.model.task.data.MissingResultFileForProgramInputException;
import hu.szeba.hades.model.task.data.TaskDescription;

import java.io.IOException;

public interface TaskFactory {

    Task getTask(String taskName, TaskDescription taskDescription, boolean continueTask) throws IOException, MissingResultFileForProgramInputException;

}
