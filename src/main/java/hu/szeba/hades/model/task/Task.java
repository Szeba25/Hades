package hu.szeba.hades.model.task;

import hu.szeba.hades.model.task.data.Solution;
import hu.szeba.hades.model.task.data.SourceFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class Task {

    private File taskDirectory;
    private File taskWorkingDirectory;
    private String taskName;
    private List<Solution> solutions;
    private List<SourceFile> sources;

    public Task(File taskDirectory, File taskWorkingDirectory, String taskName) {
        this.taskDirectory = taskDirectory;
        this.taskWorkingDirectory = taskWorkingDirectory;
        this.taskName = taskName;
        solutions = new ArrayList<>();
        sources = new ArrayList<>();
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

    public abstract int getTaskType();

}
