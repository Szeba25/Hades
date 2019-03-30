package hu.szeba.hades.wizard.model;

import hu.szeba.hades.main.io.ConfigFile;
import hu.szeba.hades.main.io.DescriptionFile;
import hu.szeba.hades.main.io.TabbedFile;
import hu.szeba.hades.main.meta.Options;
import hu.szeba.hades.main.util.SortUtilities;
import hu.szeba.hades.main.view.elements.MappedElement;
import hu.szeba.hades.wizard.view.elements.DescriptiveElement;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WizardCourse {

    private String courseId;

    private TabbedFile titleFile;
    private ConfigFile metaFile;

    private File modesPath;
    private File taskCollectionsPath;
    private File tasksPath;

    private List<MappedElement> possibleModes;
    private Map<String, String> modeIdToTitle;
    private Map<String, WizardMode> modes;

    private List<MappedElement> possibleTaskCollections;
    private Map<String, String> taskCollectionIdToTitle;
    private Map<String, WizardTaskCollection> taskCollections;

    private List<MappedElement> possibleTasks;
    private Map<String, String> taskIdToTitle;
    private Map<String, WizardTask> tasks;

    public WizardCourse(String courseId)
            throws IOException, ParserConfigurationException, SAXException {

        this.courseId = courseId;

        titleFile = new TabbedFile(new File(Options.getDatabasePath(), courseId + "/title.dat"));
        metaFile = new ConfigFile(new File(Options.getDatabasePath(), courseId  + "/meta.conf"));

        possibleModes = new ArrayList<>();
        modeIdToTitle = new HashMap<>();
        possibleTaskCollections = new ArrayList<>();
        taskCollectionIdToTitle = new HashMap<>();
        possibleTasks = new ArrayList<>();
        taskIdToTitle = new HashMap<>();

        modes = new HashMap<>();
        taskCollections = new HashMap<>();
        tasks = new HashMap<>();

        modesPath = new File(Options.getDatabasePath(), courseId + "/modes");
        for (String modeId : modesPath.list()) {
            TabbedFile metaFile = new TabbedFile(new File(modesPath, modeId + "/title.dat"));
            possibleModes.add(new DescriptiveElement(modeId, metaFile.getData(0, 0)));
            modeIdToTitle.put(modeId, metaFile.getData(0, 0));
            modes.put(modeId, new WizardMode(modesPath, modeId));
        }
        possibleModes.sort(SortUtilities::mappedElementIntegerComparator);

        taskCollectionsPath = new File(Options.getDatabasePath(), courseId + "/task_collections");
        for (String taskCollectionId : taskCollectionsPath.list()) {
            TabbedFile metaFile = new TabbedFile(new File(taskCollectionsPath, taskCollectionId + "/title.dat"));
            possibleTaskCollections.add(new DescriptiveElement(taskCollectionId, metaFile.getData(0, 0)));
            taskCollectionIdToTitle.put(taskCollectionId, metaFile.getData(0, 0));
            taskCollections.put(taskCollectionId, new WizardTaskCollection(taskCollectionsPath, taskCollectionId));
        }
        possibleTaskCollections.sort(SortUtilities::mappedElementIntegerComparator);

        tasksPath = new File(Options.getDatabasePath(), courseId + "/tasks");
        for (String taskId : tasksPath.list()) {
            DescriptionFile taskDescription = new DescriptionFile(new File(tasksPath, taskId + "/description.xml"), false);
            possibleTasks.add(new DescriptiveElement(taskId, taskDescription.getTitle()));
            taskIdToTitle.put(taskId, taskDescription.getTitle());
            tasks.put(taskId, new WizardTask(taskId, new File(tasksPath, taskId), taskDescription));
        }
        possibleTasks.sort(SortUtilities::mappedElementIntegerComparator);

    }

    public void save() throws IOException {
        titleFile.save();
        metaFile.save();

        for (WizardMode mode : modes.values()) {
            mode.save();
        }
        for (WizardTaskCollection taskCollection : taskCollections.values()) {
            taskCollection.save();
        }
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseTitle() {
        return titleFile.getData(0, 0);
    }

    public String getLanguage() {
        return metaFile.getData(0, 1);
    }

    public List<MappedElement> getPossibleModes() {
        return possibleModes;
    }

    public Map<String, String> getModeIdToTitle() {
        return modeIdToTitle;
    }

    public List<MappedElement> getPossibleTaskCollections() {
        return possibleTaskCollections;
    }

    public Map<String, String> getTaskCollectionIdToTitle() {
        return taskCollectionIdToTitle;
    }

    public List<MappedElement> getPossibleTasks() {
        return possibleTasks;
    }

    public Map<String, String> getTaskIdToTitle() {
        return taskIdToTitle;
    }

    public Map<String, WizardMode> getModes() {
        return modes;
    }

    public Map<String, WizardTaskCollection> getTaskCollections() {
        return taskCollections;
    }

    public Map<String, WizardTask> getTasks() {
        return tasks;
    }
}
