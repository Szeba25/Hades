package hu.szeba.hades.wizard.model;

import hu.szeba.hades.main.io.ConfigFile;
import hu.szeba.hades.main.io.DescriptionXMLFile;
import hu.szeba.hades.main.io.TabbedFile;
import hu.szeba.hades.main.meta.Options;
import hu.szeba.hades.main.model.task.data.TaskDescription;
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
    private String courseTitle;
    private String language;

    private File modesPath;
    private File taskCollectionsPath;
    private File tasksPath;

    private List<MappedElement> possibleModes;
    private Map<String, WizardMode> modes;

    private List<MappedElement> possibleTaskCollections;
    private Map<String, WizardTaskCollection> taskCollections;

    private List<MappedElement> possibleTasks;
    private Map<String, WizardTask> tasks;

    public WizardCourse(String courseId)
            throws IOException, ParserConfigurationException, SAXException {

        this.courseId = courseId;

        TabbedFile titleFile = new TabbedFile(new File(Options.getDatabasePath(), courseId + "/title.dat"));
        this.courseTitle = titleFile.getData(0, 0);

        ConfigFile courseMetaFile = new ConfigFile(new File(Options.getDatabasePath(), courseId  + "/meta.conf"));
        this.language = courseMetaFile.getData(0, 1);

        possibleModes = new ArrayList<>();
        possibleTaskCollections = new ArrayList<>();
        possibleTasks = new ArrayList<>();

        modes = new HashMap<>();
        taskCollections = new HashMap<>();
        tasks = new HashMap<>();

        modesPath = new File(Options.getDatabasePath(), courseId + "/modes");
        for (String modeId : modesPath.list()) {
            TabbedFile metaFile = new TabbedFile(new File(modesPath, modeId + "/title.dat"));
            possibleModes.add(new DescriptiveElement(modeId, metaFile.getData(0, 0)));
            modes.put(modeId, new WizardMode(modesPath, modeId));
        }

        taskCollectionsPath = new File(Options.getDatabasePath(), courseId + "/task_collections");
        for (String taskCollectionId : taskCollectionsPath.list()) {
            TabbedFile metaFile = new TabbedFile(new File(taskCollectionsPath, taskCollectionId + "/title.dat"));
            possibleTaskCollections.add(new DescriptiveElement(taskCollectionId, metaFile.getData(0, 0)));
            taskCollections.put(taskCollectionId, new WizardTaskCollection());
        }

        tasksPath = new File(Options.getDatabasePath(), courseId + "/tasks");
        for (String taskId : tasksPath.list()) {
            DescriptionXMLFile descriptionFile = new DescriptionXMLFile(new File(tasksPath, taskId + "/description.xml"));
            TaskDescription taskDescription = descriptionFile.parse(false);
            possibleTasks.add(new DescriptiveElement(taskId, taskDescription.getTaskTitle()));
            tasks.put(taskId, new WizardTask());
        }

    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getLanguage() {
        return language;
    }

    public List<MappedElement> getPossibleModes() {
        return possibleModes;
    }

    public List<MappedElement> getPossibleTaskCollections() {
        return possibleTaskCollections;
    }

    public List<MappedElement> getPossibleTasks() {
        return possibleTasks;
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
