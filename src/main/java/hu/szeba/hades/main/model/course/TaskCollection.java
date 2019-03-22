package hu.szeba.hades.main.model.course;

import hu.szeba.hades.main.io.ConfigFile;
import hu.szeba.hades.main.io.DescriptionXMLFile;
import hu.szeba.hades.main.io.GraphFile;
import hu.szeba.hades.main.meta.Options;
import hu.szeba.hades.main.meta.User;
import hu.szeba.hades.main.model.helper.ModeData;
import hu.szeba.hades.main.model.helper.TaskCollectionInfo;
import hu.szeba.hades.main.model.task.Task;
import hu.szeba.hades.main.model.task.data.MissingResultFileException;
import hu.szeba.hades.main.model.task.data.TaskDescription;
import hu.szeba.hades.main.model.task.graph.AdjacencyMatrix;
import hu.szeba.hades.main.model.task.graph.Graph;
import hu.szeba.hades.main.model.task.languages.InvalidLanguageException;
import hu.szeba.hades.main.model.task.taskfactory.TaskFactoryDecider;
import hu.szeba.hades.main.view.elements.AbstractState;
import hu.szeba.hades.main.view.elements.TaskElement;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskCollection {

    private User user;
    private String courseId;
    private String modeId;
    private String taskCollectionId;
    private ModeData modeData;
    private File taskCollectionDirectory;
    private File tasksDirectory;
    private Graph taskGraph;
    private List<TaskElement> possibleTasks;
    private Map<String, String> idToTitleMap;

    private final String language;
    private final TaskCollectionInfo info;

    private boolean collectionUnavailable;

    public TaskCollection(User user, String courseId, String modeId, String taskCollectionId, ModeData modeData,
                          String language, boolean collectionUnavailable)
            throws IOException, ParserConfigurationException, SAXException {

        this.user = user;
        this.courseId = courseId;
        this.modeId = modeId;
        this.taskCollectionId = taskCollectionId;
        this.modeData = modeData;
        this.taskCollectionDirectory = new File(Options.getDatabasePath(), courseId + "/task_collections/" + taskCollectionId);
        this.tasksDirectory = new File(Options.getDatabasePath(), courseId + "/tasks");
        this.language = language;
        this.collectionUnavailable = collectionUnavailable;

        loadTaskIds();
        loadTaskDescriptions();
        generateCachedData();

        ConfigFile file = new ConfigFile(new File(taskCollectionDirectory, "meta.conf"));
        info = new TaskCollectionInfo(possibleTasks.size(), Double.parseDouble(file.getData(0, 1)));
    }

    private void loadTaskIds() throws IOException {
        GraphFile graphFile = new GraphFile(new File(taskCollectionDirectory, "tasks.graph"));
        taskGraph = new AdjacencyMatrix(graphFile.getTuples());
    }

    private void loadTaskDescriptions() throws IOException, SAXException, ParserConfigurationException {
        // Create list of possible tasks
        possibleTasks = new ArrayList<>();
        idToTitleMap = new HashMap<>();
        // Load all task descriptions
        for (String taskId : taskGraph.getNodes()) {
            DescriptionXMLFile descriptionFile = new DescriptionXMLFile(new File(tasksDirectory, taskId + "/description.xml"));
            TaskDescription description = descriptionFile.parse(modeData.isIgnoreStory());
            possibleTasks.add(new TaskElement(taskId, description.getTaskTitle(), description));
            idToTitleMap.put(taskId, description.getTaskTitle());
        }
    }

    public void setCollectionUnavailable(boolean collectionUnavailable) {
        this.collectionUnavailable = collectionUnavailable;
    }

    public void generateCachedData() {
        for (TaskElement element : possibleTasks) {
            // If the task is completed, set COMPLETED status!
            String taskFullId = courseId + "/" + modeId + "/" + taskCollectionId + "/" + element.getId();
            if (user.isTaskCompleted(taskFullId)) {
                element.setState(AbstractState.COMPLETED);
            } else if (user.isTaskStarted(taskFullId)) {
                element.setState(AbstractState.IN_PROGRESS);
            } else if (!modeData.isIgnoreDependency()) {
                // Only generate, if we don't ignore dependencies
                if (collectionUnavailable) {
                    element.setState(AbstractState.UNAVAILABLE);
                } else {
                    // Assume that the task is available
                    boolean available = true;

                    // Get the parent node names, and create an empty list for prerequisites
                    List<String> parentList = taskGraph.getParentNodes(element.getId());
                    List<String> prerequisites = new ArrayList<>();

                    // Loop in the parent list
                    for (String parentId : parentList) {
                        // If the parent is not completed, add its title to the prerequisites
                        boolean parentCompleted = user.isTaskCompleted(courseId + "/" + modeId + "/" + taskCollectionId + "/" + parentId);
                        if (!parentCompleted) {
                            prerequisites.add(idToTitleMap.get(parentId));
                        }
                        available = available && parentCompleted;
                    }

                    // Set the calculated values
                    if (available) {
                        element.setState(AbstractState.AVAILABLE);
                    } else {
                        element.setState(AbstractState.UNAVAILABLE);
                    }
                    element.setPrerequisites(prerequisites);
                }
            } else {
                // Otherwise set to available
                element.setState(AbstractState.AVAILABLE);
            }
        }
    }

    public Task createTask(TaskElement element, boolean continueTask)
            throws InvalidLanguageException, IOException, MissingResultFileException {

        // Set task collection info!
        user.setCurrentTaskCollectionInfo(info);

        // Set the current task collection full id, and task full id!
        String taskCollectionFullId = courseId + "/" + modeId + "/" + taskCollectionId;
        user.setCurrentTaskCollection(taskCollectionFullId);
        user.setCurrentTask(taskCollectionFullId + "/" + element.getId());

        return TaskFactoryDecider.decideFactory(language).getTask(user, courseId, modeId, taskCollectionId,
                element.getId(), element.getDescription(), continueTask);
    }

    public List<TaskElement> getPossibleTasks() {
        return possibleTasks;
    }

    public int getCompletedTasksCount() {
        int count = 0;
        for (TaskElement element : possibleTasks) {
            if (element.getState() == AbstractState.COMPLETED) {
                count++;
            }
        }
        return count;
    }

    public int getTaskCompletionCount() {
        return info.getTaskCompletionCount();
    }

    public double getCompletionThreshold() {
        return info.getCompletionThreshold();
    }

}
