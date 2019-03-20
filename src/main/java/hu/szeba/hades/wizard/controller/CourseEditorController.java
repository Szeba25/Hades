package hu.szeba.hades.wizard.controller;

import hu.szeba.hades.main.view.elements.MappedElement;
import hu.szeba.hades.wizard.model.WizardCourse;

import javax.swing.*;

public class CourseEditorController {

    private WizardCourse course;

    public CourseEditorController(WizardCourse course) {
        this.course = course;
    }

    public void setModeListContents(JList<MappedElement> modeList) {
        DefaultListModel<MappedElement> model = (DefaultListModel<MappedElement>) modeList.getModel();
        model.removeAllElements();

        for (MappedElement element : course.getModes()) {
            model.addElement(element);
        }
    }

    public void setTaskCollectionListContents(JList<MappedElement> taskCollectionList) {
        DefaultListModel<MappedElement> model = (DefaultListModel<MappedElement>) taskCollectionList.getModel();
        model.removeAllElements();

        for (MappedElement element : course.getTaskCollections()) {
            model.addElement(element);
        }
    }

    public void setTaskListContents(JList<MappedElement> taskList) {
        DefaultListModel<MappedElement> model = (DefaultListModel<MappedElement>) taskList.getModel();
        model.removeAllElements();

        for (MappedElement element : course.getTasks()) {
            model.addElement(element);
        }
    }

}
