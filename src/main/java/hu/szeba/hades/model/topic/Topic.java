package hu.szeba.hades.model.topic;

import hu.szeba.hades.io.TaskGraphFile;
import hu.szeba.hades.meta.Options;
import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.model.task.data.MissingResultFileForProgramInputException;
import hu.szeba.hades.model.task.graph.AdjacencyMatrix;
import hu.szeba.hades.model.task.languages.UnsupportedProgrammingLanguageException;
import hu.szeba.hades.model.task.taskfactory.TaskFactoryDecider;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Topic {

    private File topicDirectory;
    private File topicWorkingDirectory;
    private String topicName;
    private AdjacencyMatrix taskMatrix;
    private List<String> taskNames;

    private final String language;

    public Topic(String courseName, String topicName, String language) throws IOException {
        this.topicDirectory = new File(Options.getDatabasePath(),
                "courses/" + courseName + "/" + topicName);
        this.topicWorkingDirectory = new File(Options.getWorkingDirectoryPath(),
                "courses/" + courseName + "/" + topicName);
        this.topicName = topicName;
        this.language = language;
        loadTaskNames();
    }

    private void loadTaskNames() throws IOException {
        TaskGraphFile taskGraphFile = new TaskGraphFile(new File(topicDirectory, "tasks.graph"));
        taskMatrix = new AdjacencyMatrix(taskGraphFile.getTuples());
        taskNames = taskMatrix.getNodeNames();
    }

    public Task createTask(String taskName, boolean continueTask) throws UnsupportedProgrammingLanguageException, IOException, MissingResultFileForProgramInputException {
        return TaskFactoryDecider.decideFactory(language).getTask(taskName, continueTask);
    }

    public List<String> getTaskNames() {
        return taskNames;
    }

}
