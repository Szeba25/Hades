package hu.szeba.hades;

import hu.szeba.hades.io.StoryXMLFile;
import hu.szeba.hades.io.TaskGraphFile;
import hu.szeba.hades.model.course.Course;
import hu.szeba.hades.model.course.CourseDatabase;
import hu.szeba.hades.view.campaign.TaskSelectorView;
import hu.szeba.hades.meta.Options;
import hu.szeba.hades.model.campaign.Campaign;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class Main {

    private CourseDatabase courseDatabase;
    private Course course;
    private Campaign campaign;
    private TaskSelectorView taskSelectorView;

    private Main() {
        Options.initialize();

        courseDatabase = new CourseDatabase();
        course = courseDatabase.loadCourse("prog1");
        campaign = course.loadCampaign("practice");

        taskSelectorView = new TaskSelectorView(campaign);

        //start();
        tests();
    }

    private void start() {
        taskSelectorView.showView();
    }

    private void tests() {
        // Story xml test:
        try {
            StoryXMLFile storyXmlFile = new StoryXMLFile(
                    new File("D:/Egyetem/Szakdolgozat/hades_Database/courses/prog1/practice/story.xml"));
            storyXmlFile.printStoryContents();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        // Task graph test:
        try {
            TaskGraphFile taskGraphFile = new TaskGraphFile(
                    new File("D:/Egyetem/Szakdolgozat/hades_Database/courses/prog1/practice/tasks.dat"));
            taskGraphFile.printContents();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

}
