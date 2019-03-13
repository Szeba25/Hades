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
    private String courseId;
    private List<String> possibleTaskCollections;
    private Map<String, TaskCollection> taskCollections;

    // Language cannot change!
    private final String language;

    public Course(User user, String courseId) {
        this.user = user;
        this.courseId = courseId;

        possibleTaskCollections = new ArrayList<>();
        File pathFile = new File(Options.getDatabasePath().getAbsolutePath(), courseId + "/task_collections");
        possibleTaskCollections.addAll(Arrays.asList(pathFile.list()));

        this.taskCollections = new HashMap<>();

        // TODO: Read from config file!
        this.language = "C";
    }

    public List<String> getPossibleTaskCollections() {
        return possibleTaskCollections;
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
