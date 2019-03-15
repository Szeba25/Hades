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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mode {

    private User user;
    private String courseId;
    private String modeId;
    private File modeDirectory;
    private AdjacencyMatrix taskCollectionMatrix;
    private List<MappedElement> possibleTaskCollections;
    private List<String> unavailableTaskCollectionIds;
    private Map<String, TaskCollection> taskCollections;

    private ModeData modeData;

    private final String language;

    public Mode(User user, String courseId, String modeId, String language) throws IOException {
        this.user = user;
        this.courseId = courseId;
        this.modeId = modeId;

        this.modeDirectory = new File(Options.getDatabasePath(), courseId + "/modes/" + modeId);
        GraphFile graphFile = new GraphFile(new File(modeDirectory, "task_collections.graph"));
        this.taskCollectionMatrix = new AdjacencyMatrix(graphFile.getTuples());

        possibleTaskCollections = new ArrayList<>();
        for (String id : taskCollectionMatrix.getNodeNames()) {
            TabbedFile titleFile = new TabbedFile(new File(Options.getDatabasePath(), courseId + "/task_collections/" + id + "/title.dat"));
            possibleTaskCollections.add(new MappedElement(id, titleFile.getData(0, 0)));
        }

        unavailableTaskCollectionIds = new ArrayList<>();
        taskCollections = new HashMap<>();

        ConfigFile metaFile = new ConfigFile(new File(modeDirectory, "meta.dat"));
        modeData = new ModeData(
                Boolean.parseBoolean(metaFile.getData(0, 1)),  // ignore dependency
                Boolean.parseBoolean(metaFile.getData(1, 1)),  // ignore story
                Boolean.parseBoolean(metaFile.getData(2, 1))); // iron man

        this.language = language;
    }

    public List<MappedElement> getPossibleTaskCollections() {
        return possibleTaskCollections;
    }

    public TaskCollection loadTaskCollection(String taskCollectionId) throws IOException, ParserConfigurationException, SAXException {
        if (taskCollections.containsKey(taskCollectionId)) {
            return taskCollections.get(taskCollectionId);
        } else {
            TaskCollection newTaskCollection = new TaskCollection(user, courseId, modeId, taskCollectionId, modeData, language);
            taskCollections.put(taskCollectionId, newTaskCollection);
            return newTaskCollection;
        }
    }

}
