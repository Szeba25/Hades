package hu.szeba.hades.model.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Task {

    public static final int TYPE_C = 0;
    public static final int TYPE_CPP = 1;

    private File taskDirectory;
    private File taskWorkingDirectory;
    private String taskName;
    private int taskType;
    private List<Solution> solutions;
    private List<SourceFile> sources;

    public Task(File campaignDirectory, File campaignWorkingDirectory, String taskName) {
        taskDirectory = new File(campaignDirectory, taskName);
        taskWorkingDirectory = new File(campaignWorkingDirectory, taskName);
        this.taskName = taskName;
        this.taskType = TYPE_C;
        solutions = new ArrayList<>();
        sources = new ArrayList<>();
    }

    public File getTaskWorkingDirectory() {
        return taskWorkingDirectory;
    }

    public String getTaskName() {
        return taskName;
    }

    public int getTaskType() {
        return taskType;
    }

    public List<Solution> getSolutions() {
        return solutions;
    }

    public List<SourceFile> getSources() {
        return sources;
    }

}
