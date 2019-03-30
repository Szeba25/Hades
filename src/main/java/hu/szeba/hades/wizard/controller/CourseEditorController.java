package hu.szeba.hades.wizard.controller;

import hu.szeba.hades.main.view.elements.MappedElement;
import hu.szeba.hades.wizard.model.WizardCourse;
import hu.szeba.hades.wizard.view.elements.DescriptiveElement;
import hu.szeba.hades.wizard.view.panels.ModeEditorPanel;
import hu.szeba.hades.wizard.view.panels.TaskCollectionEditorPanel;

import javax.swing.*;
import java.io.IOException;

public class CourseEditorController {

    private WizardCourse course;

    public CourseEditorController(WizardCourse course) {
        this.course = course;
    }

    public void setModeListContents(JList<MappedElement> modeList) {
        DefaultListModel<MappedElement> model = (DefaultListModel<MappedElement>) modeList.getModel();
        model.removeAllElements();

        for (MappedElement element : course.getPossibleModes()) {
            model.addElement(element);
        }
    }

    public void setTaskCollectionListContents(JList<MappedElement> taskCollectionList) {
        DefaultListModel<MappedElement> model = (DefaultListModel<MappedElement>) taskCollectionList.getModel();
        model.removeAllElements();

        for (MappedElement element : course.getPossibleTaskCollections()) {
            model.addElement(element);
        }
    }

    public void setTaskListContents(JList<MappedElement> taskList) {
        DefaultListModel<MappedElement> model = (DefaultListModel<MappedElement>) taskList.getModel();
        model.removeAllElements();

        for (MappedElement element : course.getPossibleTasks()) {
            model.addElement(element);
        }
    }

    public void setCurrentMode(ModeEditorPanel modeEditor, DescriptiveElement element) {
        modeEditor.setCurrentMode(
                course.getModes().get(element.getId()),
                element,
                course.getTaskCollectionIdToTitle(),
                course.getPossibleTaskCollections());
    }

    public void setCurrentTaskCollection(TaskCollectionEditorPanel taskCollectionEditor, DescriptiveElement element, ModeEditorPanel modeEditor) {
        taskCollectionEditor.setCurrentTaskCollection(
                course.getTaskCollections().get(element.getId()),
                element,
                course.getTaskIdToTitle(),
                course.getPossibleTasks(),
                course.getTaskCollectionIdToTitle(),
                modeEditor);
    }

    public void save() throws IOException {
        course.save();
    }

}
