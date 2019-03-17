package hu.szeba.hades.model.course;

import hu.szeba.hades.io.ConfigFile;
import hu.szeba.hades.io.GraphFile;
import hu.szeba.hades.io.TabbedFile;
import hu.szeba.hades.meta.Options;
import hu.szeba.hades.meta.User;
import hu.szeba.hades.model.task.graph.AdjacencyMatrix;
import hu.szeba.hades.view.elements.AbstractState;
import hu.szeba.hades.view.elements.StatefulElement;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mode {

    private User user;
    private String courseId;
    private String modeId;
    private AdjacencyMatrix taskCollectionMatrix;
    private List<StatefulElement> possibleTaskCollections;
    private Map<String, String> idToTitleMap;

    private ModeData modeData;
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
        idToTitleMap = new HashMap<>();
        for (String id : taskCollectionMatrix.getNodeNames()) {
            TabbedFile titleFile = new TabbedFile(new File(Options.getDatabasePath(), courseId + "/task_collections/" + id + "/title.dat"));
            possibleTaskCollections.add(new StatefulElement(id, titleFile.getData(0, 0)));
            idToTitleMap.put(id, titleFile.getData(0, 0));
        }

        taskCollections = new HashMap<>();

        ConfigFile metaFile = new ConfigFile(new File(modeDirectory, "meta.conf"));
        modeData = new ModeData(
                Boolean.parseBoolean(metaFile.getData(0, 1)),  // ignore dependency
                Boolean.parseBoolean(metaFile.getData(1, 1)),  // ignore story
                Boolean.parseBoolean(metaFile.getData(2, 1))); // iron man

        generateCachedData();

        this.language = language;
    }

    public void generateCachedData() {
        // Only generate, if we don't ignore dependencies
        if (!modeData.isIgnoreDependency()) {
            for (StatefulElement element : possibleTaskCollections) {
                // If the task collection is completed, set COMPLETED status
                if (user.isTaskCollectionCompleted(courseId + "/" + modeId + "/" + element.getId())) {
                    element.setState(AbstractState.COMPLETED);
                } else {
                    // Assume that the collection is available
                    boolean available = true;

                    // Get the parent node names, and create an empty list for the prerequisites
                    List<String> parentList = taskCollectionMatrix.getParentNodes(element.getId());
                    List<String> prerequisites = new ArrayList<>();

                    // Loop in the parent list
                    for (String parentId : parentList) {
                        // If the parent is not completed, add its title to the prerequisites
                        boolean parentCompleted = user.isTaskCollectionCompleted(courseId + "/" + modeId + "/" + parentId);
                        if (!parentCompleted) {
                            prerequisites.add(idToTitleMap.get(parentId));
                        }
                        // The collection is available only if all of its parents are available
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
            }
        }

        // Notify existing collections about the unavailability!
        for (StatefulElement element : possibleTaskCollections) {
            TaskCollection collection = taskCollections.get(element.getId());
            if (collection != null) {
                collection.setCollectionUnavailable(element.getState() == AbstractState.UNAVAILABLE);
                collection.generateCachedData();
            }
        }
    }

    public List<StatefulElement> getPossibleTaskCollections() {
        return possibleTaskCollections;
    }

    public TaskCollection loadTaskCollection(String taskCollectionId, boolean unavailable)
            throws IOException, ParserConfigurationException, SAXException {

        if (taskCollections.containsKey(taskCollectionId)) {
            return taskCollections.get(taskCollectionId);
        } else {
            TaskCollection newTaskCollection = new TaskCollection(user, courseId, modeId, taskCollectionId,
                    modeData, language, unavailable);
            taskCollections.put(taskCollectionId, newTaskCollection);
            return newTaskCollection;
        }
    }

}
