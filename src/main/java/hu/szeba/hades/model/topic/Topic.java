package hu.szeba.hades.model.topic;

import hu.szeba.hades.io.DescriptionXMLFile;
import hu.szeba.hades.io.StoryXMLFile;
import hu.szeba.hades.io.TaskGraphFile;
import hu.szeba.hades.meta.Options;
import hu.szeba.hades.meta.User;
import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.model.task.data.MissingResultFileException;
import hu.szeba.hades.model.task.data.TaskDescription;
import hu.szeba.hades.model.task.data.TaskStory;
import hu.szeba.hades.model.task.graph.AdjacencyMatrix;
import hu.szeba.hades.model.task.languages.InvalidLanguageException;
import hu.szeba.hades.model.task.taskfactory.TaskFactoryDecider;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Topic {

    private User user;
    private String courseName;
    private String topicName;
    private File topicDirectory;
    private AdjacencyMatrix taskMatrix;
    private List<String> taskIds;
    private Map<String, TaskDescription> taskDescriptions;
    private Map<String, TaskStory> taskStories;
    private Map<String, String> taskTitleToTaskId;
    private Map<String, String> taskIdToTaskTitle;

    private final String language;

    public Topic(User user, String courseName, String topicName, String language) throws IOException, ParserConfigurationException, SAXException {
        this.user = user;
        this.courseName = courseName;
        this.topicName = topicName;
        this.topicDirectory = new File(Options.getDatabasePath(), "courses/" + courseName + "/" + topicName);
        this.language = language;

        loadTaskIds();
        loadTaskTexts();
    }

    private void loadTaskIds() throws IOException {
        TaskGraphFile taskGraphFile = new TaskGraphFile(new File(topicDirectory, "tasks.graph"));
        taskMatrix = new AdjacencyMatrix(taskGraphFile.getTuples());
        taskIds = taskMatrix.getNodeNames();
    }

    private void loadTaskTexts() throws IOException, SAXException, ParserConfigurationException {
        DescriptionXMLFile descriptionFile = new DescriptionXMLFile(new File(topicDirectory, "descriptions.xml"));
        taskDescriptions = descriptionFile.parseTaskDescriptions();

        // Create mapping for both direction!
        taskTitleToTaskId = new HashMap<>();
        taskIdToTaskTitle = new HashMap<>();
        for (TaskDescription description : taskDescriptions.values()) {
            taskTitleToTaskId.put(description.getTaskTitle(), description.getTaskId());
            taskIdToTaskTitle.put(description.getTaskId(), description.getTaskTitle());
        }

        StoryXMLFile storyXMLFile = new StoryXMLFile(new File(topicDirectory, "stories.xml"));
        taskStories = storyXMLFile.parseTaskStories();
    }

    public Task createTask(String taskId, boolean continueTask)
            throws InvalidLanguageException, IOException, MissingResultFileException {

        return TaskFactoryDecider.decideFactory(language).getTask(user, courseName, topicName, taskId,
                taskDescriptions.get(taskId), taskStories.get(taskId), continueTask);
    }

    public boolean progressExistsForTask(String taskId) {
        return user.progressExistsForTask(courseName, topicName, taskId);
    }

    public TaskDescription getTaskDescription(String taskId) {
        return taskDescriptions.get(taskId);
    }

    public List<String> getTaskTitles() {
        List<String> taskTitles = new LinkedList<>();
        for (String taskId : taskIds) {
            taskTitles.add(taskDescriptions.get(taskId).getTaskTitle());
        }
        return taskTitles;
    }

    public String getTaskIdByTaskTitle(String taskTitle) {
        return taskTitleToTaskId.get(taskTitle);
    }

}
