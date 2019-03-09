package hu.szeba.hades;

import hu.szeba.hades.meta.Options;
import hu.szeba.hades.meta.User;
import hu.szeba.hades.model.course.Course;
import hu.szeba.hades.model.course.CourseDatabase;
import hu.szeba.hades.model.topic.Topic;
import hu.szeba.hades.view.topic.TaskSelectorView;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Main {

    private User user;
    private CourseDatabase courseDatabase;
    private Course course;
    private Topic topic;
    private TaskSelectorView taskSelectorView;

    private Main() {
        try {
            Options.initialize();
            user = new User("DEFAULT", "Someone");
            courseDatabase = new CourseDatabase(user);
            course = courseDatabase.loadCourse("programozas_1");
            topic = course.loadTopic("gyakorlas");
            taskSelectorView = new TaskSelectorView(topic);
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
