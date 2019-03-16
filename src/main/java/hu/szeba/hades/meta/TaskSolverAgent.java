package hu.szeba.hades.meta;

import java.io.File;
import java.io.IOException;

public interface TaskSolverAgent {

    void setCurrentTaskCollection(String taskCollectionFullId);
    void setCurrentTask(String taskFullId);

    boolean isCurrentTaskCompleted();
    void markCurrentTaskAsCompleted() throws IOException;
    void markCurrentTaskAsStarted() throws IOException;

    File getCurrentTaskWorkingDirectoryPath();

}
