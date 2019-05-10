package hu.szeba.hades.wizard.controller;

import hu.szeba.hades.main.io.ConfigFile;
import hu.szeba.hades.main.io.SingleDataFile;
import hu.szeba.hades.main.meta.Options;
import hu.szeba.hades.main.view.elements.MappedElement;
import hu.szeba.hades.wizard.model.WizardCourseDatabase;
import hu.szeba.hades.wizard.view.elements.DescriptiveElement;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class CourseSelectorController {

    private WizardCourseDatabase database;

    public CourseSelectorController(WizardCourseDatabase database) {
        this.database = database;
    }

    public void setCourseListContent(JList<MappedElement> courseList) {
        DefaultListModel<MappedElement> model = (DefaultListModel<MappedElement>) courseList.getModel();
        model.removeAllElements();

        for (MappedElement element : database.getCourses()) {
            model.addElement(element);
        }
    }

    public void addCourse() throws IOException {
        ConfigFile indicesFile = new ConfigFile(new File("config/course_indices.conf"));
        String sid = indicesFile.getData(0, 1);
        int id = Integer.parseInt(sid);
        id++;
        database.getCourses().add(new DescriptiveElement(sid, "Course"));
        indicesFile.setData(0, 1, Integer.toString(id));
        indicesFile.save();

        // Create folder structure
        FileUtils.forceMkdir(new File(Options.getDatabasePath(), sid));
        FileUtils.forceMkdir(new File(Options.getDatabasePath(), sid + "/modes"));
        FileUtils.forceMkdir(new File(Options.getDatabasePath(), sid + "/task_collections"));
        FileUtils.forceMkdir(new File(Options.getDatabasePath(), sid + "/tasks"));

        ConfigFile courseIndices = new ConfigFile(new File(Options.getDatabasePath(), sid + "/indices.conf"));
        courseIndices.addData("modes", "0");
        courseIndices.addData("task_collections", "0");
        courseIndices.addData("tasks", "0");
        courseIndices.save();

        ConfigFile meta = new ConfigFile(new File(Options.getDatabasePath(), sid + "/meta.conf"));
        meta.addData("language", "C");
        meta.save();

        SingleDataFile name = new SingleDataFile(new File(Options.getDatabasePath(), sid + "/title.dat"));
        name.addData("Course");
        name.save();
    }

}
