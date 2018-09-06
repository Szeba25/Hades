package hu.szeba.hades.model.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Task {

    private File taskDirectory;
    private String taskName;
    private List<Solution> solutions;
    private List<SourceFile> sources;

    public Task(File campaignDirectory, String taskName) {
        taskDirectory = new File(campaignDirectory, taskName);
        this.taskName = taskName;
        solutions = new ArrayList<>();
        sources = new ArrayList<>();
    }

    public String getName() {
        return taskName;
    }
}
