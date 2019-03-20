package hu.szeba.hades.wizard.controller;

import hu.szeba.hades.main.view.elements.MappedElement;
import hu.szeba.hades.wizard.model.WizardCourseDatabase;

import javax.swing.*;

public class CourseController {

    private WizardCourseDatabase database;

    public CourseController(WizardCourseDatabase database) {
        this.database = database;
    }

    public void setCourseListContent(JList<MappedElement> courseList) {
        DefaultListModel<MappedElement> model = (DefaultListModel<MappedElement>) courseList.getModel();
        model.removeAllElements();

        for (MappedElement element : database.getCourses()) {
            model.addElement(element);
        }
    }

}
