package hu.szeba.hades.model.task.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TaskData {

    private File taskDirectory;
    private File taskWorkingDirectory;

    private String taskName;

    private List<Solution> solutions;
    private List<SourceFile> sources;

    public TaskData(File taskDirectory, File taskWorkingDirectory, String taskName) {
        this.taskDirectory = taskDirectory;
        this.taskWorkingDirectory = taskWorkingDirectory;
        this.taskName = taskName;
        solutions = new ArrayList<>();
        sources = new ArrayList<>();
    }

    public File getTaskDirectory() {
        return taskDirectory;
    }

    public File getTaskWorkingDirectory() {
        return taskWorkingDirectory;
    }

    public String getTaskName() {
        return taskName;
    }

    public List<Solution> getSolutions() {
        return solutions;
    }

    public List<SourceFile> getSources() {
        return sources;
    }

}
