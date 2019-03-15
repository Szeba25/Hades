package hu.szeba.hades.controller.task;

import hu.szeba.hades.model.course.Course;
import hu.szeba.hades.model.course.CourseDatabase;
import hu.szeba.hades.model.course.Mode;
import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.model.task.data.MissingResultFileException;
import hu.szeba.hades.model.task.languages.InvalidLanguageException;
import hu.szeba.hades.model.task.TaskCollection;
import hu.szeba.hades.view.BaseView;
import hu.szeba.hades.view.MappedElement;
import hu.szeba.hades.view.task.TaskSolvingView;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class TaskSelectorController {

    private CourseDatabase courseDatabase;
    private Course course;
    private Mode mode;
    private TaskCollection taskCollection;

    public TaskSelectorController(CourseDatabase courseDatabase) {
        this.courseDatabase = courseDatabase;
    }

    public void updateCourse(JList<MappedElement> taskList,
                             JComboBox<MappedElement> taskCollectionList,
                             JComboBox<MappedElement> modeList,
                             MappedElement selectedCourse)
            throws IOException, SAXException, ParserConfigurationException {

        course = courseDatabase.loadCourse(selectedCourse.getId());
        setModeListContents(modeList);
        updateMode(taskList, taskCollectionList, (MappedElement)modeList.getSelectedItem());
    }

    public void updateMode(JList<MappedElement> taskList,
                           JComboBox<MappedElement> taskCollectionList,
                           MappedElement selectedMode)
            throws IOException, ParserConfigurationException, SAXException {

        mode = course.loadMode(selectedMode.getId());
        setTaskCollectionListContents(taskCollectionList);
        updateTaskCollection(taskList, (MappedElement)taskCollectionList.getSelectedItem());
    }

    public void updateTaskCollection(JList<MappedElement> taskList, MappedElement selectedTaskCollection)
            throws ParserConfigurationException, SAXException, IOException {

        taskCollection = mode.loadTaskCollection(selectedTaskCollection.getId());
        setTaskListContents(taskList);
    }

    public void setCourseListContents(JComboBox<MappedElement> courseList) {
        courseList.removeAllItems();
        courseDatabase.getPossibleCourses().forEach(courseList::addItem);
    }

    public void setModeListContents(JComboBox<MappedElement> modeList) {
        modeList.removeAllItems();
        course.getPossibleModes().forEach(modeList::addItem);
    }

    public void setTaskCollectionListContents(JComboBox<MappedElement> taskCollectionList) {
        taskCollectionList.removeAllItems();
        mode.getPossibleTaskCollections().forEach(taskCollectionList::addItem);
    }

    public void setTaskListContents(JList<MappedElement> taskList) {
        taskList.setListData(taskCollection.getPossibleTasks().toArray(new MappedElement[0]));
    }

    public void loadNewTask(MappedElement selectedTask, BaseView parentView)
            throws InvalidLanguageException, IOException, MissingResultFileException {

        Task task = taskCollection.createTask(selectedTask.getId(), false);
        parentView.hideView();
        TaskSolvingView taskSolvingView = new TaskSolvingView(parentView, task);
        taskSolvingView.showViewMaximized();
    }

    public void continueTask(MappedElement selectedTask, BaseView parentView)
            throws InvalidLanguageException, IOException, MissingResultFileException {

        Task task = taskCollection.createTask(selectedTask.getId(), true);
        parentView.hideView();
        TaskSolvingView taskSolvingView = new TaskSolvingView(parentView, task);
        taskSolvingView.showViewMaximized();
    }

    public void setTaskShortDescription(MappedElement selectedTask, JEditorPane descriptionArea) {
        descriptionArea.setText(taskCollection.getTaskDescription(selectedTask.getId()).getShortDescription());
    }

    public boolean isTaskCompleted(MappedElement taskElement) {
        return taskCollection.isTaskCompleted(taskElement.getId());
    }

    public boolean isProgressExists(MappedElement taskElement) {
        return taskCollection.isProgressExists(taskElement.getId());
    }

    public boolean isTaskUnavailable(MappedElement taskElement) {
        return taskCollection.isTaskUnavailable(taskElement.getId());
    }

    public void generateUnavailableTaskIds() {
        taskCollection.generateUnavailableTaskIds();
    }

}
