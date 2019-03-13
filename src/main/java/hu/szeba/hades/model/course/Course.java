package hu.szeba.hades.model.course;

import hu.szeba.hades.io.DataFile;
import hu.szeba.hades.io.TabbedFile;
import hu.szeba.hades.meta.Options;
import hu.szeba.hades.meta.User;
import hu.szeba.hades.model.task.TaskCollection;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Course {

    private User user;
    private String courseId;
    private List<String> possibleTaskCollectionIds;
    private List<String> possibleTaskCollectionTitles;
    private Map<String, String> taskCollectionTitleToId;
    private Map<String, TaskCollection> taskCollections;

    // Language cannot change!
    private final String language;

    public Course(User user, String courseId) throws IOException {
        this.user = user;
        this.courseId = courseId;

        possibleTaskCollectionIds = new ArrayList<>();
        File pathFile = new File(Options.getDatabasePath().getAbsolutePath(), courseId + "/task_collections");
        possibleTaskCollectionIds.addAll(Arrays.asList(pathFile.list()));

        possibleTaskCollectionTitles = new ArrayList<>();
        taskCollectionTitleToId = new HashMap<>();

        possibleTaskCollectionIds.forEach((id) -> {
            try {
                TabbedFile metaFile = new TabbedFile(new File(pathFile, id + "/title.dat"));
                possibleTaskCollectionTitles.add(metaFile.getData(0, 0));
                taskCollectionTitleToId.put(metaFile.getData(0, 0), id);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.taskCollections = new HashMap<>();

        DataFile courseMetaFile = new DataFile(new File(Options.getDatabasePath(), courseId  + "/meta.dat"), "=");
        this.language = courseMetaFile.getData(0, 1);
    }

    public List<String> getPossibleTaskCollectionTitles() {
        return possibleTaskCollectionTitles;
    }

    public String titleToId(String taskCollectionTitle) {
        return taskCollectionTitleToId.get(taskCollectionTitle);
    }

    public TaskCollection loadTaskCollection(String taskCollectionId) throws IOException, ParserConfigurationException, SAXException {
        if (taskCollections.containsKey(taskCollectionId)) {
            return taskCollections.get(taskCollectionId);
        } else {
            TaskCollection newTaskCollection = new TaskCollection(user, courseId, taskCollectionId, language);
            taskCollections.put(taskCollectionId, newTaskCollection);
            return newTaskCollection;
        }
    }

}
