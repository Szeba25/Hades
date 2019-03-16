package hu.szeba.hades.model.task;

import hu.szeba.hades.io.ConfigFile;
import hu.szeba.hades.io.DescriptionXMLFile;
import hu.szeba.hades.io.GraphFile;
import hu.szeba.hades.meta.Options;
import hu.szeba.hades.meta.TaskCollectionInfo;
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
    private Map<String, List<String>> cachedTaskPrerequisites;

    private final String language;
    private final TaskCollectionInfo info;

    private boolean unavailable;

    public TaskCollection(User user, String courseId, String modeId, String taskCollectionId, ModeData modeData,
                          String language, boolean unavailable)
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

        unavailableTaskIds = new HashSet<>();
        cachedTaskPrerequisites = new HashMap<>();
        generateCachedData();

        ConfigFile file = new ConfigFile(new File(taskCollectionDirectory, "meta.conf"));
        info = new TaskCollectionInfo(possibleTasks.size(), Double.parseDouble(file.getData(0, 1)));

        this.unavailable = unavailable;
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

    public void updateUnavailability(boolean value) {
        unavailable = value;
    }

    public void generateCachedData() {
        // Only generate, if we don't ignore dependencies
        unavailableTaskIds.clear();
        cachedTaskPrerequisites.clear();

        if (!modeData.isIgnoreDependency()) {
            for (String taskId : taskMatrix.getNodeNames()) {
                boolean taskAvailable = true;
                List<String> parentList = taskMatrix.getParentNodes(taskId);
                List<String> reqList = new ArrayList<>();
                cachedTaskPrerequisites.put(taskId, reqList);

                for (String parentId : parentList) {
                    boolean parentCompleted = user.isTaskCompleted(courseId + "/" + modeId + "/" + taskCollectionId + "/" + parentId);
                    if (!parentCompleted) {
                        TaskDescription parentDescription = taskDescriptions.get(parentId);
                        reqList.add(parentDescription.getTaskTitle());
                    }
                    taskAvailable = taskAvailable && parentCompleted;
                }

                // The task is unavailable, if any of its parents is not completed...
                if (!taskAvailable) {
                    unavailableTaskIds.add(taskId);
                }
            }
        } else {
            for (String taskId : taskMatrix.getNodeNames()) {
                cachedTaskPrerequisites.put(taskId, new ArrayList<>());
            }
        }
    }

    public Task createTask(String taskId, boolean continueTask)
            throws InvalidLanguageException, IOException, MissingResultFileException {

        // Set task collection info!
        user.setCurrentTaskCollectionInfo(info);

        // Set the current task collection full id, and task full id!
        String taskCollectionFullId = courseId + "/" + modeId + "/" + taskCollectionId;
        user.setCurrentTaskCollection(taskCollectionFullId);
        user.setCurrentTask(taskCollectionFullId + "/" + taskId);

        return TaskFactoryDecider.decideFactory(language).getTask(user, courseId, modeId, taskCollectionId, taskId,
                taskDescriptions.get(taskId), continueTask);
    }

    public TaskStatus getTaskEffectiveStatus(String taskId) {
        String taskFullId = courseId + "/" + modeId + "/" + taskCollectionId + "/" + taskId;
        if (user.isTaskCompleted(taskFullId)) {
            return TaskStatus.COMPLETED;
        } else if (user.isTaskStarted(taskFullId)) {
            return TaskStatus.IN_PROGRESS;
        } else if (unavailable || unavailableTaskIds.contains(taskId)) {
            return TaskStatus.UNAVAILABLE;
        } else {
            return TaskStatus.AVAILABLE;
        }
    }

    public boolean isTaskStarted(String taskId) {
        return user.isTaskStarted(courseId + "/" + modeId + "/" + taskCollectionId + "/" + taskId);
    }

    public boolean isTaskUnavailable(String taskId) {
        return unavailable || unavailableTaskIds.contains(taskId);
    }

    public TaskDescription getTaskDescription(String taskId) {
        return taskDescriptions.get(taskId);
    }

    public List<MappedElement> getPossibleTasks() {
        return possibleTasks;
    }

    public int getCompletedTasksCount() {
        int count = 0;
        for (MappedElement element : possibleTasks) {
            if (user.isTaskCompleted(courseId + "/" + modeId + "/" + taskCollectionId + "/" + element.getId())) {
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

    public List<String> getTaskPrerequisites(String taskId) {
        return cachedTaskPrerequisites.get(taskId);
    }

}
