package hu.szeba.hades.main.meta;

import hu.szeba.hades.main.model.course.TaskCollectionInfo;

import java.io.File;
import java.io.IOException;

public interface TaskSolverAgent {

    void setCurrentTaskCollectionInfo(TaskCollectionInfo info);
    void setCurrentTaskCollection(String taskCollectionFullId);
    void setCurrentTask(String taskFullId);

    boolean isCurrentTaskCompleted();
    void markCurrentTaskAsCompleted() throws IOException;
    void markCurrentTaskAsStarted() throws IOException;

    File getCurrentTaskWorkingDirectoryPath();

}
