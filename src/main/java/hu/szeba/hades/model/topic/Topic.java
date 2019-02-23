package hu.szeba.hades.model.topic;

import hu.szeba.hades.io.DescriptionXMLFile;
import hu.szeba.hades.io.TaskGraphFile;
import hu.szeba.hades.meta.Options;
import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.model.task.data.MissingResultFileException;
import hu.szeba.hades.model.task.data.TaskDescription;
import hu.szeba.hades.model.task.graph.AdjacencyMatrix;
import hu.szeba.hades.model.task.languages.InvalidLanguageException;
import hu.szeba.hades.model.task.taskfactory.TaskFactoryDecider;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Topic {

    private File topicDirectory;
    private File topicWorkingDirectory;
    private String topicName;
    private AdjacencyMatrix taskMatrix;
    private List<String> taskNames;
    private Map<String, TaskDescription> taskDescriptions;
    private Map<String, String> taskTitleToTaskName;
    private Map<String, String> taskNameToTaskTitle;

    private final String language;

    public Topic(String courseName, String topicName, String language) throws IOException, ParserConfigurationException, SAXException {
        this.topicDirectory = new File(Options.getDatabasePath(),
                "courses/" + courseName + "/" + topicName);
        this.topicWorkingDirectory = new File(Options.getWorkingDirectoryPath(),
                "courses/" + courseName + "/" + topicName);
        this.topicName = topicName;
        this.language = language;

        loadTaskNames();
        loadTaskDescriptions();
    }

    private void loadTaskNames() throws IOException {
        TaskGraphFile taskGraphFile = new TaskGraphFile(new File(topicDirectory, "tasks.graph"));
        taskMatrix = new AdjacencyMatrix(taskGraphFile.getTuples());
        taskNames = taskMatrix.getNodeNames();
    }

    private void loadTaskDescriptions() throws IOException, SAXException, ParserConfigurationException {
        DescriptionXMLFile descriptionFile = new DescriptionXMLFile(new File(topicDirectory, "descriptions.xml"));
        taskDescriptions = descriptionFile.parseTaskDescriptions();
        // Create mapping for both direction!
        taskTitleToTaskName = new HashMap<>();
        taskNameToTaskTitle = new HashMap<>();
        for (TaskDescription description : taskDescriptions.values()) {
            taskTitleToTaskName.put(description.getTaskTitle(), description.getTaskName());
            taskNameToTaskTitle.put(description.getTaskName(), description.getTaskTitle());
        }
    }

    public Task createTask(String taskName, boolean continueTask)
            throws InvalidLanguageException, IOException, MissingResultFileException {
        return TaskFactoryDecider.decideFactory(language).getTask(taskName, taskDescriptions.get(taskName), continueTask);
    }

    public boolean progressExists(String taskName) {
        return new File(Options.getWorkingDirectoryPath(), "tasks/" + taskName).exists();
    }

    public TaskDescription getTaskDescription(String taskName) {
        return taskDescriptions.get(taskName);
    }

    public List<String> getTaskTitles() {
        List<String> taskTitles = new LinkedList<>();
        for (String taskName : taskNames) {
            taskTitles.add(taskDescriptions.get(taskName).getTaskTitle());
        }
        return taskTitles;
    }

    public String getTaskNameByTaskTitle(String taskTitle) {
        return taskTitleToTaskName.get(taskTitle);
    }

}
