package hu.szeba.hades.wizard.controller;

import hu.szeba.hades.main.meta.Languages;
import hu.szeba.hades.main.view.components.DialogFactory;
import hu.szeba.hades.main.view.elements.MappedElement;
import hu.szeba.hades.wizard.model.WizardCourse;
import hu.szeba.hades.wizard.view.elements.DescriptiveElement;
import hu.szeba.hades.wizard.view.panels.ModeEditorPanel;
import hu.szeba.hades.wizard.view.panels.TaskCollectionEditorPanel;
import hu.szeba.hades.wizard.view.panels.TaskEditorPanel;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class CourseEditorController {

    private WizardCourse course;

    public CourseEditorController(WizardCourse course) {
        this.course = course;
    }

    public void setModeListContents(JList<MappedElement> modeList) {
        MappedElement selectedElement = modeList.getSelectedValue();

        DefaultListModel<MappedElement> model = (DefaultListModel<MappedElement>) modeList.getModel();
        model.removeAllElements();

        for (MappedElement element : course.getPossibleModes()) {
            model.addElement(element);
        }

        modeList.setSelectedValue(selectedElement, true);
    }

    public void setTaskCollectionListContents(JList<MappedElement> taskCollectionList) {
        MappedElement selectedElement = taskCollectionList.getSelectedValue();

        DefaultListModel<MappedElement> model = (DefaultListModel<MappedElement>) taskCollectionList.getModel();
        model.removeAllElements();

        for (MappedElement element : course.getPossibleTaskCollections()) {
            model.addElement(element);
        }

        taskCollectionList.setSelectedValue(selectedElement, true);
    }

    public void setTaskListContents(JList<MappedElement> taskList) {
        MappedElement selectedElement = taskList.getSelectedValue();

        DefaultListModel<MappedElement> model = (DefaultListModel<MappedElement>) taskList.getModel();
        model.removeAllElements();

        for (MappedElement element : course.getPossibleTasks()) {
            model.addElement(element);
        }

        taskList.setSelectedValue(selectedElement, true);
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

    public void setCurrentTask(TaskEditorPanel taskEditorPanel, DescriptiveElement element, TaskCollectionEditorPanel taskCollectionEditor) {
        taskEditorPanel.setCurrentTask(
                course.getTasks().get(element.getId()),
                element,
                course.getTaskIdToTitle(),
                taskCollectionEditor
        );
    }

    public void save() throws IOException {
        course.save();
    }

    public void newMode() throws IOException {
        System.out.println("New mode created with id: " + course.createNewMode());
    }

    public void newTaskCollection() throws IOException {
        System.out.println("New task collection created with id: " + course.createNewTaskCollection());
    }

    public void newTask() throws IOException, ParserConfigurationException, SAXException {
        System.out.println("New task created with id: " + course.createNewTask());
    }

    public void deleteMode(JList<MappedElement> modeList, ModeEditorPanel modeEditor) {
        MappedElement element = modeList.getSelectedValue();
        if (element != null) {
            int result = JOptionPane.showConfirmDialog(new JFrame(), "Delete \"" + element.toString() + "\"?", "Delete mode...", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                course.deleteMode(element.getId());
                setModeListContents(modeList);
                modeEditor.hideAndDisable();
            }
        }
    }

    public void deleteTaskCollection(JList<MappedElement> taskCollectionList, TaskCollectionEditorPanel taskCollectionEditor) {
        MappedElement element = taskCollectionList.getSelectedValue();
        if (element != null) {
            int result = JOptionPane.showConfirmDialog(new JFrame(), "Delete \"" + element.toString() + "\"?", "Delete task collection...", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                if (course.deleteTaskCollection(element.getId())) {
                    setTaskCollectionListContents(taskCollectionList);
                    taskCollectionEditor.hideAndDisable();
                }
            }
        }
    }

    public void deleteTask(JList<MappedElement> taskList, TaskEditorPanel taskEditor) {
        MappedElement element = taskList.getSelectedValue();
        if (element != null) {
            int result = DialogFactory.showCustomChoiceDialog(
                    Languages.translate("Delete this task?"),
                    Languages.translate("Delete task..."),
                    Languages.translate("Yes"),
                    Languages.translate("No"));
            if (result == JOptionPane.YES_OPTION) {
                if (course.deleteTask(element.getId())) {
                    setTaskListContents(taskList);
                    taskEditor.hideAndDisable();
                }
            }
        }
    }
}
