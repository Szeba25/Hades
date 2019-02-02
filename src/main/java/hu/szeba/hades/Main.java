package hu.szeba.hades;

import hu.szeba.hades.meta.Options;
import hu.szeba.hades.model.topic.Topic;
import hu.szeba.hades.model.course.Course;
import hu.szeba.hades.model.course.CourseDatabase;
import hu.szeba.hades.view.topic.TaskSelectorView;

import javax.swing.*;
import java.io.IOException;

public class Main {

    private CourseDatabase courseDatabase;
    private Course course;
    private Topic topic;
    private TaskSelectorView taskSelectorView;

    private Main() {
        try {
            Options.initialize();
            courseDatabase = new CourseDatabase();
            course = courseDatabase.loadCourse("prog1");
            topic = course.loadTopic("practice");
            taskSelectorView = new TaskSelectorView(topic);
            start();
        } catch (IOException e) {
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
