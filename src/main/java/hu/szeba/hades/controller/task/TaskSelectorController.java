package hu.szeba.hades.controller.task;

import hu.szeba.hades.model.course.Course;
import hu.szeba.hades.model.course.CourseDatabase;
import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.model.task.data.MissingResultFileException;
import hu.szeba.hades.model.task.languages.InvalidLanguageException;
import hu.szeba.hades.model.task.TaskCollection;
import hu.szeba.hades.view.BaseView;
import hu.szeba.hades.view.task.TaskSolvingView;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class TaskSelectorController {

    private CourseDatabase courseDatabase;
    private Course course;
    private TaskCollection taskCollection;

    public TaskSelectorController(CourseDatabase courseDatabase) throws ParserConfigurationException, SAXException, IOException {
        this.courseDatabase = courseDatabase;
        this.course = courseDatabase.loadCourse("prog_1");
        this.taskCollection = course.loadTaskCollection("collection_1");
    }

    public void updateCourse(JList<String> taskList, JComboBox<String> taskCollectionList, String newCourse)
            throws IOException, SAXException, ParserConfigurationException {
        course = courseDatabase.loadCourse(newCourse);
        setTaskCollectionListContents(taskCollectionList);
        updateTaskCollection(taskList, (String)taskCollectionList.getSelectedItem());
    }

    public void updateTaskCollection(JList<String> taskList, String newTaskCollection)
            throws ParserConfigurationException, SAXException, IOException {
        taskCollection = course.loadTaskCollection(newTaskCollection);
        setTaskListContents(taskList);
    }

    public void setCourseListContents(JComboBox<String> courseList) {
        courseList.removeAllItems();
        courseDatabase.getPossibleCourses().forEach(courseList::addItem);
    }

    public void setTaskCollectionListContents(JComboBox<String> taskCollectionList) {
        taskCollectionList.removeAllItems();
        course.getPossibleTaskCollections().forEach(taskCollectionList::addItem);
    }

    public void setTaskListContents(JList<String> taskList) {
        taskList.setListData(taskCollection.getTaskTitles().toArray(new String[0]));
    }

    public void loadNewTask(String selectedTaskId,
                            BaseView parentView) throws InvalidLanguageException, IOException, MissingResultFileException {
        Task task = taskCollection.createTask(selectedTaskId, false);
        parentView.hideView();
        TaskSolvingView taskSolvingView = new TaskSolvingView(parentView, task);
        taskSolvingView.showViewMaximized();
    }

    public void continueTask(String selectedTaskId,
                             BaseView parentView) throws InvalidLanguageException, IOException, MissingResultFileException {
        Task task = taskCollection.createTask(selectedTaskId, true);
        parentView.hideView();
        TaskSolvingView taskSolvingView = new TaskSolvingView(parentView, task);
        taskSolvingView.showViewMaximized();
    }

    public void setTaskShortDescription(String taskId, JEditorPane descriptionArea) {
        descriptionArea.setText(taskCollection.getTaskDescription(taskId).getShortDescription());
    }

    public String getTaskIdByTaskTitle(String taskTitle) {
        return taskCollection.getTaskIdByTaskTitle(taskTitle);
    }

    public boolean isTaskCompleted(String taskId) {
        return taskCollection.isTaskCompleted(taskId);
    }

    public boolean isProgressExists(String taskId) {
        return taskCollection.isProgressExists(taskId);
    }

    public boolean isTaskUnavailable(String taskId) {
        return taskCollection.isTaskUnavailable(taskId);
    }

    public void generateUnavailableTaskIds() {
        taskCollection.generateUnavailableTaskIds();
    }

}
