package hu.szeba.hades.model.task;

import hu.szeba.hades.io.DescriptionXMLFile;
import hu.szeba.hades.io.GraphFile;
import hu.szeba.hades.meta.Options;
import hu.szeba.hades.meta.User;
import hu.szeba.hades.model.course.ModeData;
import hu.szeba.hades.model.task.data.MissingResultFileException;
import hu.szeba.hades.model.task.data.TaskDescription;
import hu.szeba.hades.model.task.graph.AdjacencyMatrix;
import hu.szeba.hades.model.task.languages.InvalidLanguageException;
import hu.szeba.hades.model.task.taskfactory.TaskFactoryDecider;
import hu.szeba.hades.view.MappedElement;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class TaskCollection {

    private User user;
    private String courseId;
    private String modeId;
    private String taskCollectionId;
    private ModeData modeData;
    private File taskCollectionDirectory;
    private File tasksDirectory;
    private AdjacencyMatrix taskMatrix;
    private List<MappedElement> possibleTasks;
    private Map<String, TaskDescription> taskDescriptions;
    private Set<String> unavailableTaskIds;

    private final String language;

    public TaskCollection(User user, String courseId, String modeId, String taskCollectionId, ModeData modeData, String language)
            throws IOException, ParserConfigurationException, SAXException {

        this.user = user;
        this.courseId = courseId;
        this.modeId = modeId;
        this.taskCollectionId = taskCollectionId;
        this.modeData = modeData;
        this.taskCollectionDirectory = new File(Options.getDatabasePath(), courseId + "/task_collections/" + taskCollectionId);
        this.tasksDirectory = new File(Options.getDatabasePath(), courseId + "/tasks");
        this.language = language;

        loadTaskIds();
        loadTaskDescriptions();
        generateUnavailableTaskIds();
    }

    private void loadTaskIds() throws IOException {
        GraphFile graphFile = new GraphFile(new File(taskCollectionDirectory, "tasks.graph"));
        taskMatrix = new AdjacencyMatrix(graphFile.getTuples());
    }

    private void loadTaskDescriptions() throws IOException, SAXException, ParserConfigurationException {
        // Create list of possible tasks
        possibleTasks = new ArrayList<>();
        // Create associative map for descriptions
        taskDescriptions = new HashMap<>();

        // Load all task descriptions
        for (String taskId : taskMatrix.getNodeNames()) {
            DescriptionXMLFile descriptionFile = new DescriptionXMLFile(new File(tasksDirectory, taskId + "/description.xml"));
            TaskDescription description = descriptionFile.parse(modeData.isIgnoreStory());
            possibleTasks.add(new MappedElement(taskId, description.getTaskTitle()));
            taskDescriptions.put(taskId, description);
        }
    }

    public void generateUnavailableTaskIds() {
        // Only generate, if we don't ignore dependencies
        unavailableTaskIds = new HashSet<>();
        if (!modeData.isIgnoreDependency()) {
            for (String taskId : taskMatrix.getNodeNames()) {
                boolean available = true;
                List<String> list = taskMatrix.getParentNodes(taskId);
                for (String parents : list) {
                    available = available && (user.isTaskCompleted(courseId + "/" + modeId + "/" + taskCollectionId + "/" + parents));
                }
                // The task is unavailable, if any of its parents is not completed...
                if (!available) {
                    unavailableTaskIds.add(taskId);
                }
            }
        }
    }

    public Task createTask(String taskId, boolean continueTask)
            throws InvalidLanguageException, IOException, MissingResultFileException {

        // Set the current task collection, and task identifier strings!
        String taskCollectionFullId = courseId + "/" + modeId + "/" + taskCollectionId;
        user.setCurrentTaskCollection(taskCollectionFullId);
        user.setCurrentTask(taskCollectionFullId + "/" + taskId);

        return TaskFactoryDecider.decideFactory(language).getTask(user, courseId, modeId, taskCollectionId, taskId,
                taskDescriptions.get(taskId), continueTask);
    }

    public boolean isTaskCompleted(String taskId) {
        return user.isTaskCompleted(courseId + "/" + modeId + "/" + taskCollectionId + "/" + taskId);
    }

    public boolean isTaskStarted(String taskId) {
        return user.isTaskStarted(courseId + "/" + modeId + "/" + taskCollectionId + "/" + taskId);
    }

    public boolean isTaskUnavailable(String taskId) {
        return unavailableTaskIds.contains(taskId);
    }

    public TaskDescription getTaskDescription(String taskId) {
        return taskDescriptions.get(taskId);
    }

    public List<MappedElement> getPossibleTasks() {
        return possibleTasks;
    }

}
