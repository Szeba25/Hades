package hu.szeba.hades.model.campaign;

import hu.szeba.hades.io.TaskGraphFile;
import hu.szeba.hades.meta.Options;
import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.model.task.graph.AdjacencyMatrix;
import hu.szeba.hades.model.task.languages.UnsupportedProgrammingLanguageException;
import hu.szeba.hades.model.task.taskfactory.TaskFactoryDecider;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Campaign {

    private File campaignDirectory;
    private File campaignWorkingDirectory;
    private String campaignName;
    private AdjacencyMatrix taskMatrix;
    private List<String> taskNames;

    // Language cannot change!
    private final String language;

    public Campaign(String courseName, String campaignName, String language) throws IOException {
        this.campaignDirectory =
                new File(Options.getDatabasePath(),
                        "courses/" + courseName + "/" + campaignName);
        this.campaignWorkingDirectory =
                new File(Options.getWorkingDirectoryPath(),
                        "courses/" + courseName + "/" + campaignName);
        this.campaignName = campaignName;

        this.language = language;

        loadTaskNames();
    }

    private void loadTaskNames() throws IOException {
        TaskGraphFile taskGraphFile = new TaskGraphFile(
                new File(campaignDirectory, "tasks.graph"));
        taskMatrix = new AdjacencyMatrix(taskGraphFile.getTuples());
        taskNames = taskMatrix.getNodeNames();
    }

    public Task createTask(String taskName) throws UnsupportedProgrammingLanguageException {
        return TaskFactoryDecider.decideFactory(language).getTask(taskName);
    }

    public List<String> getTaskNames() {
        return taskNames;
    }

}
