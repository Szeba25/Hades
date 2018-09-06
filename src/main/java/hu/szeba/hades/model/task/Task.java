package hu.szeba.hades.model.task;

import hu.szeba.hades.meta.Options;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Task {

    private Options options;
    private File taskDirectory;
    private File taskWorkingDirectory;
    private String taskName;
    private List<Solution> solutions;
    private List<SourceFile> sources;

    public Task(Options options, File campaignDirectory, File campaignWorkingDirectory, String taskName) {
        this.options = options;
        taskDirectory = new File(campaignDirectory, taskName);
        taskWorkingDirectory = new File(campaignWorkingDirectory, taskName);
        this.taskName = taskName;
        solutions = new ArrayList<>();
        sources = new ArrayList<>();
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
