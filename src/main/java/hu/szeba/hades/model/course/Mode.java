package hu.szeba.hades.model.course;

import hu.szeba.hades.io.ConfigFile;
import hu.szeba.hades.io.GraphFile;
import hu.szeba.hades.io.TabbedFile;
import hu.szeba.hades.meta.Options;
import hu.szeba.hades.meta.User;
import hu.szeba.hades.model.task.TaskCollection;
import hu.szeba.hades.model.task.graph.AdjacencyMatrix;
import hu.szeba.hades.view.MappedElement;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Mode {

    private User user;
    private String courseId;
    private String modeId;
    private AdjacencyMatrix taskCollectionMatrix;
    private List<MappedElement> possibleTaskCollections;
    private ModeData modeData;
    private Set<String> unavailableTaskCollectionIds;
    private Map<String, TaskCollection> taskCollections;

    private final String language;

    public Mode(User user, String courseId, String modeId, String language) throws IOException {
        this.user = user;
        this.courseId = courseId;
        this.modeId = modeId;

        File modeDirectory = new File(Options.getDatabasePath(), courseId + "/modes/" + modeId);
        GraphFile graphFile = new GraphFile(new File(modeDirectory, "task_collections.graph"));
        this.taskCollectionMatrix = new AdjacencyMatrix(graphFile.getTuples());

        possibleTaskCollections = new ArrayList<>();
        for (String id : taskCollectionMatrix.getNodeNames()) {
            TabbedFile titleFile = new TabbedFile(new File(Options.getDatabasePath(), courseId + "/task_collections/" + id + "/title.dat"));
            possibleTaskCollections.add(new MappedElement(id, titleFile.getData(0, 0)));
        }

        taskCollections = new HashMap<>();

        ConfigFile metaFile = new ConfigFile(new File(modeDirectory, "meta.conf"));
        modeData = new ModeData(
                Boolean.parseBoolean(metaFile.getData(0, 1)),  // ignore dependency
                Boolean.parseBoolean(metaFile.getData(1, 1)),  // ignore story
                Boolean.parseBoolean(metaFile.getData(2, 1))); // iron man

        unavailableTaskCollectionIds = new HashSet<>();
        generateUnavailableTaskCollectionIds();

        this.language = language;
    }

    public void generateUnavailableTaskCollectionIds() {
        // Only generate, if we don't ignore dependencies
        unavailableTaskCollectionIds.clear();
        if (!modeData.isIgnoreDependency()) {
            for (String taskCollectionId : taskCollectionMatrix.getNodeNames()) {
                boolean taskCollectionAvailable = true;
                List<String> list = taskCollectionMatrix.getParentNodes(taskCollectionId);
                for (String parents : list) {
                    taskCollectionAvailable = taskCollectionAvailable &&
                            (user.isTaskCollectionCompleted(courseId + "/" + modeId + "/" + parents));
                }
                if (!taskCollectionAvailable) {
                    unavailableTaskCollectionIds.add(taskCollectionId);
                }
            }
        }
        // Notify existing collections about availability
        for (MappedElement taskCollection : possibleTaskCollections) {
            TaskCollection collection = taskCollections.get(taskCollection.getId());
            if (collection != null) {
                collection.updateAvailability(!unavailableTaskCollectionIds.contains(taskCollection.getId()));
            }
        }
    }

    public boolean isTaskCollectionCompleted(String taskCollectionId) {
        return user.isTaskCollectionCompleted(courseId + "/" + modeId + "/" + taskCollectionId);
    }

    public boolean isTaskCollectionUnavailable(String taskCollectionId) {
        return unavailableTaskCollectionIds.contains(taskCollectionId);
    }

    public List<MappedElement> getPossibleTaskCollections() {
        return possibleTaskCollections;
    }

    public TaskCollection loadTaskCollection(String taskCollectionId) throws IOException, ParserConfigurationException, SAXException {
        if (taskCollections.containsKey(taskCollectionId)) {
            return taskCollections.get(taskCollectionId);
        } else {
            TaskCollection newTaskCollection = new TaskCollection(user, courseId, modeId, taskCollectionId, modeData,
                    language, !unavailableTaskCollectionIds.contains(taskCollectionId));
            taskCollections.put(taskCollectionId, newTaskCollection);
            return newTaskCollection;
        }
    }

}
