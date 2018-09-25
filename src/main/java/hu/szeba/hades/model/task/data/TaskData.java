package hu.szeba.hades.model.task.data;

import hu.szeba.hades.meta.Options;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TaskData {

    private File taskDirectory;
    private File taskWorkingDirectory;

    private String taskName;

    private List<Solution> solutions;
    private List<SourceFile> sources;

    public TaskData(String taskName) {
        this.taskDirectory = getTaskDirectory(taskName);
        this.taskWorkingDirectory = getTaskWorkingDirectory(taskName);
        this.taskName = taskName;
        solutions = new ArrayList<>();
        sources = new ArrayList<>();
    }

    private File getTaskDirectory(String taskName) {
        return new File(Options.getCampaignDatabasePath(), "tasks/" + taskName);
    }

    private File getTaskWorkingDirectory(String taskName) {
        return new File(Options.getWorkingDirectoryPath(), "tasks/" + taskName);
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
