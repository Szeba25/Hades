package hu.szeba.hades.wizard.model;

import hu.szeba.hades.main.io.ConfigFile;
import hu.szeba.hades.main.io.DescriptionXMLFile;
import hu.szeba.hades.main.io.TabbedFile;
import hu.szeba.hades.main.meta.Options;
import hu.szeba.hades.main.model.task.data.TaskDescription;
import hu.szeba.hades.main.view.elements.MappedElement;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WizardCourse {

    private String courseId;
    private String courseTitle;
    private String language;

    private File modesPath;
    private File taskCollectionsPath;
    private File tasksPath;

    private List<MappedElement> modes;
    private List<MappedElement> taskCollections;
    private List<MappedElement> tasks;

    public WizardCourse(String courseId, String courseTitle)
            throws IOException, ParserConfigurationException, SAXException {

        this.courseId = courseId;
        this.courseTitle = courseTitle;

        ConfigFile courseMetaFile = new ConfigFile(new File(Options.getDatabasePath(), courseId  + "/meta.conf"));
        this.language = courseMetaFile.getData(0, 1);

        modes = new ArrayList<>();
        taskCollections = new ArrayList<>();
        tasks = new ArrayList<>();

        modesPath = new File(Options.getDatabasePath(), courseId + "/modes");
        for (String modeId : modesPath.list()) {
            TabbedFile metaFile = new TabbedFile(new File(modesPath, modeId + "/title.dat"));
            modes.add(new MappedElement(modeId, metaFile.getData(0, 0)));
        }

        taskCollectionsPath = new File(Options.getDatabasePath(), courseId + "/task_collections");
        for (String taskCollectionId : taskCollectionsPath.list()) {
            TabbedFile metaFile = new TabbedFile(new File(taskCollectionsPath, taskCollectionId + "/title.dat"));
            modes.add(new MappedElement(taskCollectionId, metaFile.getData(0, 0)));
        }

        tasksPath = new File(Options.getDatabasePath(), courseId + "/tasks");
        for (String taskId : tasksPath.list()) {
            DescriptionXMLFile descriptionFile = new DescriptionXMLFile(new File(tasksPath, taskId + "/descriptions.xml"));
            TaskDescription taskDescription = descriptionFile.parse(false);
            modes.add(new MappedElement(taskId, taskDescription.getTaskTitle()));
        }

    }

    public List<MappedElement> getModes() {
        return modes;
    }

    public List<MappedElement> getTaskCollections() {
        return taskCollections;
    }

    public List<MappedElement> getTasks() {
        return tasks;
    }

}
