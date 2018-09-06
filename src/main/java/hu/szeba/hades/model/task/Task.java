package hu.szeba.hades.model.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Task {

    private File taskDirectory;
    private File workingTaskDirectory;
    private String taskName;
    private List<Solution> solutions;
    private List<SourceFile> sources;

    public Task(File campaignDirectory, File workingCampaignDirectory, String taskName) {
        taskDirectory = new File(campaignDirectory, taskName);
        workingTaskDirectory = new File(workingCampaignDirectory, taskName);
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
