package hu.szeba.hades.model.task;

import hu.szeba.hades.io.DescriptionXMLFile;
import hu.szeba.hades.io.TaskGraphFile;
import hu.szeba.hades.meta.Options;
import hu.szeba.hades.meta.User;
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

public class TaskCollection {

    private User user;
    private String courseName;
    private String taskCollectionName;
    private File taskCollectionDirectory;
    private File tasksDirectory;
    private AdjacencyMatrix taskMatrix;
    private List<String> taskIds;
    private Map<String, TaskDescription> taskDescriptions;
    private Map<String, String> taskTitleToTaskId;
    private Map<String, String> taskIdToTaskTitle;
    private Set<String> unavailableTaskIds;

    private final String language;

    public TaskCollection(User user, String courseName, String taskCollectionName, String language) throws IOException, ParserConfigurationException, SAXException {
        this.user = user;
        this.courseName = courseName;
        this.taskCollectionName = taskCollectionName;
        this.taskCollectionDirectory = new File(Options.getDatabasePath(), courseName + "/topics/" + taskCollectionName);
        this.tasksDirectory = new File(Options.getDatabasePath(), courseName + "/tasks");
        this.language = language;

        loadTaskIds();
        loadTaskDescriptions();
        generateUnavailableTaskIds();
    }

    private void loadTaskIds() throws IOException {
        TaskGraphFile taskGraphFile = new TaskGraphFile(new File(taskCollectionDirectory, "tasks.graph"));
        taskMatrix = new AdjacencyMatrix(taskGraphFile.getTuples());
        taskIds = taskMatrix.getNodeNames();
    }

    private void loadTaskDescriptions() throws IOException, SAXException, ParserConfigurationException {
        // Create associative map for descriptions
        taskDescriptions = new HashMap<>();
        // Create mapping for both direction!
        taskTitleToTaskId = new HashMap<>();
        taskIdToTaskTitle = new HashMap<>();

        // Load all task descriptions
        for (String taskId : taskIds) {
            DescriptionXMLFile descriptionFile = new DescriptionXMLFile(new File(tasksDirectory, taskId + "/description.xml"));
            TaskDescription description = descriptionFile.parse();
            taskDescriptions.put(taskId, description);
            taskTitleToTaskId.put(description.getTaskTitle(), taskId);
            taskIdToTaskTitle.put(taskId, description.getTaskTitle());
        }
    }

    public void generateUnavailableTaskIds() {
        unavailableTaskIds = new HashSet<>();
        for (String taskId : taskMatrix.getNodeNames()) {
            boolean available = true;
            List<String> list = taskMatrix.getParentNodes(taskId);
            for (String parents : list) {
                available = available && (user.isTaskCompleted(courseName + "/" + taskCollectionName + "/" + parents));
            }
            // The task is unavailable, if any of its parents is not completed...
            if (!available) {
                unavailableTaskIds.add(taskId);
            }
        }
    }

    public Task createTask(String taskId, boolean continueTask)
            throws InvalidLanguageException, IOException, MissingResultFileException {

        return TaskFactoryDecider.decideFactory(language).getTask(user, courseName, taskCollectionName, taskId, taskDescriptions.get(taskId), continueTask);
    }

    public boolean isTaskCompleted(String taskId) {
        return user.isTaskCompleted(courseName + "/" + taskCollectionName + "/" + taskId);
    }

    public boolean isProgressExists(String taskId) {
        return user.isProgressExists(courseName + "/" + taskCollectionName + "/" + taskId);
    }

    public boolean isTaskUnavailable(String taskId) {
        return unavailableTaskIds.contains(taskId);
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
