package hu.szeba.hades;

import hu.szeba.hades.io.StoryXMLFile;
import hu.szeba.hades.io.TaskGraphFile;
import hu.szeba.hades.model.compiler.ProgramCompiler;
import hu.szeba.hades.model.compiler.ProgramCompilerC;
import hu.szeba.hades.model.course.Course;
import hu.szeba.hades.model.course.CourseDatabase;
import hu.szeba.hades.model.task.data.SourceFile;
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
import java.util.LinkedList;
import java.util.List;

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

        taskSelectorView = new TaskSelectorView(campaign);
        start();
    }

    private void start() {
        taskSelectorView.showView();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

}
