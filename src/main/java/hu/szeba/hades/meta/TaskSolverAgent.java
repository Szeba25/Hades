package hu.szeba.hades.meta;

import hu.szeba.hades.model.helper.TaskCollectionInfo;

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
