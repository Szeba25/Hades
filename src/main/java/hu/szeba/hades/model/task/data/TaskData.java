package hu.szeba.hades.model.task.data;

import hu.szeba.hades.io.DataFile;
import hu.szeba.hades.meta.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskData {

    private File taskDirectory;
    private File taskWorkingDirectory;

    private final String taskName;
    private final String language;

    private List<Solution> solutions;
    private List<SourceFile> sources;

    public TaskData(String taskName, String language) throws IOException {
        this.taskDirectory = getTaskDirectory(taskName);
        this.taskWorkingDirectory = getTaskWorkingDirectory(taskName);
        if (!taskWorkingDirectory.exists()) {
            FileUtils.forceMkdir(taskWorkingDirectory);
            FileUtils.copyDirectory(taskDirectory, taskWorkingDirectory);
        }
        this.taskName = taskName;
        this.language = language;
        solutions = new ArrayList<>();
        sources = new ArrayList<>();
        makeSolutions();
        makeSources();
    }

    private void makeSolutions() { }

    private void makeSources() throws IOException {
        DataFile sourceList = new DataFile(new File(taskWorkingDirectory, ".meta/sources.dat"));
        for (int i = 0; i < sourceList.getLineCount(); i++) {
            sources.add(new SourceFile(new File(taskWorkingDirectory, sourceList.getData(i, 0))));
        }
    }

    private File getTaskDirectory(String taskName) {
        return new File(Options.getDatabasePath(), "tasks/" + taskName);
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

    public String getLanguage() {
        return language;
    }

    public List<Solution> getSolutions() {
        return solutions;
    }

    public List<SourceFile> getSources() {
        return sources;
    }

}
