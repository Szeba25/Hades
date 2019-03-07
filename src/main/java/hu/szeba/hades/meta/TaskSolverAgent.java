package hu.szeba.hades.meta;

import java.io.IOException;

public interface TaskSolverAgent {

    boolean isTaskCompleted(String identifierString);
    void markTaskAsCompleted(String identifierString) throws IOException;

}
