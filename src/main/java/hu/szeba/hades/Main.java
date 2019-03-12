package hu.szeba.hades;

import hu.szeba.hades.meta.Options;
import hu.szeba.hades.meta.User;
import hu.szeba.hades.model.course.Course;
import hu.szeba.hades.model.course.CourseDatabase;
import hu.szeba.hades.model.task.TaskCollection;
import hu.szeba.hades.view.task.TaskSelectorView;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Main {

    private User user;
    private CourseDatabase courseDatabase;
    private Course course;
    private TaskCollection taskCollection;
    private TaskSelectorView taskSelectorView;

    private Main() {
        try {
            Options.initialize();
            user = new User("DEFAULT", "Someone");
            courseDatabase = new CourseDatabase(user);
            course = courseDatabase.loadCourse("prog_1");
            taskCollection = course.loadTaskCollection("collection_1");
            taskSelectorView = new TaskSelectorView(taskCollection);
            start();
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void start() {
        taskSelectorView.showView();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

}
