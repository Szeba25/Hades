package hu.szeba.hades;

import hu.szeba.hades.io.StoryXMLFile;
import hu.szeba.hades.io.TaskGraphFile;
import hu.szeba.hades.model.compiler.ProgramCompiler;
import hu.szeba.hades.model.compiler.ProgramCompilerC;
import hu.szeba.hades.model.course.Course;
import hu.szeba.hades.model.course.CourseDatabase;
import hu.szeba.hades.model.task.graph.AdjacencyMatrix;
import hu.szeba.hades.view.campaign.TaskSelectorView;
import hu.szeba.hades.meta.Options;
import hu.szeba.hades.model.campaign.Campaign;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;

public class Main {

    private CourseDatabase courseDatabase;
    private Course course;
    private Campaign campaign;
    private TaskSelectorView taskSelectorView;

    private Main() {
        try {
            Options.initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }

        courseDatabase = new CourseDatabase();
        course = courseDatabase.loadCourse("prog1");
        campaign = course.loadCampaign("practice");

        // GUI
        /*
        taskSelectorView = new TaskSelectorView(campaign);
        start();
        */

        // TESTS
        tests();
    }

    private void start() {
        taskSelectorView.showView();
    }

    private void tests() {
        // Story xml test:
        try {
            System.out.println("---> XML parser:");
            StoryXMLFile storyXmlFile = new StoryXMLFile(
                    new File(Main.class.getResource("story_test.xml").getFile()));
            storyXmlFile.printStoryContents();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        // Task graph test:
        try {
            System.out.println("---> Task graph file parser:");
            TaskGraphFile taskGraphFile = new TaskGraphFile(
                    new File(Main.class.getResource("tasks_test.graph").getFile()));
            taskGraphFile.printContents();
            System.out.println("---> Adjacency matrix:");
            AdjacencyMatrix matrix = new AdjacencyMatrix(taskGraphFile.getTuples());
            matrix.print();
            matrix.printConnectedNodes("task3");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Compiler test:
        ProgramCompiler programCompiler = new ProgramCompilerC(Options.getPathTo("compiler_c"));
        try {
            programCompiler.compile(null, Options.getWorkingDirectoryPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

}
