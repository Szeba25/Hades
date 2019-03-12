package hu.szeba.hades.model.course;

import hu.szeba.hades.meta.User;
import hu.szeba.hades.model.task.TaskCollection;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Course {

    private User user;
    private String courseName;
    private Map<String, TaskCollection> taskCollections;

    // Language cannot change!
    private final String language;

    public Course(User user, String courseName) {
        this.user = user;
        this.courseName = courseName;
        this.taskCollections = new HashMap<>();

        // TODO: Read from config file!
        this.language = "C";
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
