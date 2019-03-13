package hu.szeba.hades.model.course;

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
    private String courseName;
    private List<String> possibleTaskCollections;
    private Map<String, TaskCollection> taskCollections;

    // Language cannot change!
    private final String language;

    public Course(User user, String courseName) {
        this.user = user;
        this.courseName = courseName;

        possibleTaskCollections = new ArrayList<>();
        File pathFile = new File(Options.getDatabasePath().getAbsolutePath(), courseName + "/task_collections");
        possibleTaskCollections.addAll(Arrays.asList(pathFile.list()));

        this.taskCollections = new HashMap<>();

        // TODO: Read from config file!
        this.language = "C";
    }

    public List<String> getPossibleTaskCollections() {
        return possibleTaskCollections;
    }

    public TaskCollection loadTaskCollection(String taskCollectionName) throws IOException, ParserConfigurationException, SAXException {
        if (taskCollections.containsKey(taskCollectionName)) {
            return taskCollections.get(taskCollectionName);
        } else {
            TaskCollection newTaskCollection = new TaskCollection(user, courseName, taskCollectionName, language);
            taskCollections.put(taskCollectionName, newTaskCollection);
            return newTaskCollection;
        }
    }

}
