package hu.szeba.hades.model.task.data;

import hu.szeba.hades.meta.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class TaskData {

    private File taskDirectory;
    private File taskWorkingDirectory;

    private String taskName;

    private List<Solution> solutions;
    private List<SourceFile> sources;

    public TaskData(String taskName) throws IOException {
        this.taskDirectory = getTaskDirectory(taskName);
        this.taskWorkingDirectory = getTaskWorkingDirectory(taskName);
        if (!taskWorkingDirectory.exists()) {
            FileUtils.forceMkdir(taskWorkingDirectory);
            FileUtils.copyDirectory(taskDirectory, taskWorkingDirectory);
        }
        this.taskName = taskName;
        solutions = new ArrayList<>();
        sources = new ArrayList<>();
        makeSolutions();
        makeSources();
    }

    private void makeSolutions() { }

    private void makeSources() throws IOException {
        // TODO: Replace with config file that lists sources!!!
        SourceFile src = new SourceFile("main.c");
        src.setData(String.join("\n", Files.readAllLines(
                        Paths.get(new File(taskWorkingDirectory, "main.c").getAbsolutePath()))));
        sources.add(src);
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

    public List<Solution> getSolutions() {
        return solutions;
    }

    public List<SourceFile> getSources() {
        return sources;
    }

}
