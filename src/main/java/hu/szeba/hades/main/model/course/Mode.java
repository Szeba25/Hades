package hu.szeba.hades.main.model.course;

import hu.szeba.hades.main.io.ConfigFile;
import hu.szeba.hades.main.io.TabbedFile;
import hu.szeba.hades.main.meta.Options;
import hu.szeba.hades.main.meta.User;
import hu.szeba.hades.main.model.helper.ModeData;
import hu.szeba.hades.main.model.task.graph.Graph;
import hu.szeba.hades.main.model.task.graph.AdjacencyList;
import hu.szeba.hades.main.view.elements.AbstractState;
import hu.szeba.hades.main.view.elements.MappedElement;
import hu.szeba.hades.main.view.elements.StatefulElement;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Mode {

    private User user;
    private String courseId;
    private String modeId;
    private Graph taskCollectionGraph;
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
        this.taskCollectionGraph = new AdjacencyList(new File(modeDirectory, "task_collections.graph"));

        possibleTaskCollections = new ArrayList<>();
        idToTitleMap = new HashMap<>();
        for (String id : taskCollectionGraph.getNodes()) {
            TabbedFile titleFile = new TabbedFile(new File(Options.getDatabasePath(), courseId + "/task_collections/" + id + "/title.dat"));
            possibleTaskCollections.add(new StatefulElement(id, titleFile.getData(0, 0)));
            idToTitleMap.put(id, titleFile.getData(0, 0));
        }
        possibleTaskCollections.sort(Comparator.comparing(MappedElement::getId));

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
        for (StatefulElement element : possibleTaskCollections) {
            // If the task collection is completed, set COMPLETED status
            if (user.isTaskCollectionCompleted(courseId + "/" + modeId + "/" + element.getId())) {
                element.setState(AbstractState.COMPLETED);
            } else if (!modeData.isIgnoreDependency()) {
                // Only generate, if we don't ignore dependencies
                // Assume that the collection is available
                boolean available = true;

                // Get the parent node names, and create an empty list for the prerequisites
                Set<String> parentSet = taskCollectionGraph.getParentNodes(element.getId());
                List<String> prerequisites = new ArrayList<>();

                // Loop in the parent list
                for (String parentId : parentSet) {
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
            } else {
                // Otherwise set to available
                element.setState(AbstractState.AVAILABLE);
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
