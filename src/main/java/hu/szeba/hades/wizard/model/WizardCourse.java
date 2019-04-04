package hu.szeba.hades.wizard.model;

import hu.szeba.hades.main.io.ConfigFile;
import hu.szeba.hades.main.io.DescriptionFile;
import hu.szeba.hades.main.io.TabbedFile;
import hu.szeba.hades.main.meta.Languages;
import hu.szeba.hades.main.meta.Options;
import hu.szeba.hades.main.util.SortUtilities;
import hu.szeba.hades.main.view.components.DialogFactory;
import hu.szeba.hades.main.view.elements.MappedElement;
import hu.szeba.hades.wizard.view.elements.DescriptiveElement;
import org.apache.commons.io.FileUtils;
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
    private ConfigFile indicesFile;

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
        indicesFile = new ConfigFile(new File(Options.getDatabasePath(), courseId + "/indices.conf"));

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
            tasks.put(taskId, new WizardTask(tasksPath, taskId, taskDescription));
        }
        possibleTasks.sort(SortUtilities::mappedElementIntegerComparator);

    }

    public void save() throws IOException {
        titleFile.save();
        metaFile.save();

        // Clean up directories
        FileUtils.cleanDirectory(modesPath);
        FileUtils.cleanDirectory(taskCollectionsPath);
        FileUtils.cleanDirectory(tasksPath);

        // Save everything!
        for (WizardMode mode : modes.values()) {
            mode.save();
        }
        for (WizardTaskCollection taskCollection : taskCollections.values()) {
            taskCollection.save();
        }
        for (WizardTask task : tasks.values()) {
            task.save();
        }

        indicesFile.save();

        System.out.println("Course " + courseId + " saved!");
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

    public int createNewMode() throws IOException {
        String sid = indicesFile.getData(0, 1);
        int id = Integer.parseInt(sid);

        WizardMode newMode = new WizardMode(modesPath, sid);
        newMode.fillWithDefaults();
        possibleModes.add(new DescriptiveElement(sid, ""));
        modeIdToTitle.put(sid, "");
        modes.put(sid, newMode);

        indicesFile.setData(0, 1, String.valueOf(id+1));
        return id;
    }

    public int createNewTaskCollection() throws IOException {
        String sid = indicesFile.getData(1, 1);
        int id = Integer.parseInt(sid);

        WizardTaskCollection newTaskCollection = new WizardTaskCollection(taskCollectionsPath, sid);
        newTaskCollection.fillWithDefaults();
        possibleTaskCollections.add(new DescriptiveElement(sid, ""));
        taskCollectionIdToTitle.put(sid, "");
        taskCollections.put(sid, newTaskCollection);

        indicesFile.setData(1, 1, String.valueOf(id+1));
        return id;
    }

    public int createNewTask() throws IOException, ParserConfigurationException, SAXException {
        String sid = indicesFile.getData(2, 1);
        int id = Integer.parseInt(sid);

        DescriptionFile description = new DescriptionFile(new File(tasksPath, sid + "/description.xml"), false);
        WizardTask newTask = new WizardTask(tasksPath, sid, description);
        possibleTasks.add(new DescriptiveElement(sid, ""));
        taskIdToTitle.put(sid, "");
        tasks.put(sid, newTask);

        indicesFile.setData(2, 1, String.valueOf(id+1));
        return id;
    }

    public void deleteMode(String modeId) {
        for (int i = possibleModes.size()-1; i >= 0; i--) {
            if (possibleModes.get(i).getId().equals(modeId)) {
                possibleModes.remove(i);
                break;
            }
        }
        modeIdToTitle.remove(modeId);
        modes.remove(modeId);
    }

    public boolean deleteTaskCollection(String taskCollectionId) {
        boolean canBeDeleted = true;
        StringBuilder builder = new StringBuilder();
        builder.append(Languages.translate("The following modes refer to this:"));
        builder.append("\n");
        for (WizardMode mode : modes.values()) {
            if (mode.getGraph().getNodes().contains(taskCollectionId)) {
                builder.append("> ");
                builder.append(mode.getModeId());
                builder.append(": ");
                builder.append(mode.getTitle());
                builder.append("\n");
                canBeDeleted = false;
            }
        }
        if (!canBeDeleted) {
            DialogFactory.showCustomError(builder.toString(), Languages.translate("Can't delete task collection!"));
        } else {
            for (int i = possibleTaskCollections.size() - 1; i >= 0; i--) {
                if (possibleTaskCollections.get(i).getId().equals(taskCollectionId)) {
                    possibleTaskCollections.remove(i);
                    break;
                }
            }
            taskCollectionIdToTitle.remove(taskCollectionId);
            taskCollections.remove(taskCollectionId);
        }
        return canBeDeleted;
    }

    public boolean deleteTask(String taskId) {
        boolean canBeDeleted = true;
        StringBuilder builder = new StringBuilder();
        builder.append(Languages.translate("The following task collections refer to this:"));
        builder.append("\n");
        for (WizardTaskCollection taskCollection : taskCollections.values()) {
            if (taskCollection.getGraph().getNodes().contains(taskId)) {
                builder.append("> ");
                builder.append(taskCollection.getTaskCollectionId());
                builder.append(": ");
                builder.append(taskCollection.getTitle());
                builder.append("\n");
                canBeDeleted = false;
            }
        }
        if (!canBeDeleted) {
            DialogFactory.showCustomError(builder.toString(), Languages.translate("Can't delete task!"));
        } else {
            for (int i = possibleTasks.size() - 1; i >= 0; i--) {
                if (possibleTasks.get(i).getId().equals(taskId)) {
                    possibleTasks.remove(i);
                    break;
                }
            }
            taskIdToTitle.remove(taskId);
            tasks.remove(taskId);
        }
        return canBeDeleted;
    }
}
