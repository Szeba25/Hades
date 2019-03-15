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
import hu.szeba.hades.view.task.TaskFilterData;
import hu.szeba.hades.form.task.TaskFilterForm;
import hu.szeba.hades.view.task.TaskSolvingView;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public class TaskSelectorController {

    private CourseDatabase courseDatabase;
    private Course course;
    private Mode mode;
    private TaskCollection taskCollection;

    private TaskFilterForm taskFilterForm;
    private TaskFilterData taskFilterData;

    public TaskSelectorController(CourseDatabase courseDatabase) {
        this.courseDatabase = courseDatabase;
        this.taskFilterData = new TaskFilterData();
        this.taskFilterForm = new TaskFilterForm(taskFilterData);
    }

    public void updateCourse(JComboBox<MappedElement> modeList,
                             MappedElement selectedCourse)
            throws IOException {

        course = courseDatabase.loadCourse(selectedCourse.getId());
        setModeListContents(modeList);
    }

    public void updateMode(JComboBox<MappedElement> taskCollectionList,
                           MappedElement selectedMode)
            throws IOException {

        mode = course.loadMode(selectedMode.getId());
        setTaskCollectionListContents(taskCollectionList);
    }

    public void updateTaskCollection(JList<MappedElement> taskList,
                                     MappedElement selectedTaskCollection)
            throws ParserConfigurationException, SAXException, IOException {

        taskCollection = mode.loadTaskCollection(selectedTaskCollection.getId());
        taskFilterForm.addAllTags(taskCollection);
        setTaskListContents(taskList);
    }

    public void setCourseListContents(JComboBox<MappedElement> courseList) {
        courseList.removeAllItems();
        for (MappedElement element : courseDatabase.getPossibleCourses()) {
            courseList.addItem(element);
        }
    }

    public void setModeListContents(JComboBox<MappedElement> modeList) {
        modeList.removeAllItems();
        for (MappedElement element : course.getPossibleModes()) {
            modeList.addItem(element);
        }
    }

    public void setTaskCollectionListContents(JComboBox<MappedElement> taskCollectionList) {
        taskCollectionList.removeAllItems();
        for (MappedElement element : mode.getPossibleTaskCollections()) {
            taskCollectionList.addItem(element);
        }
    }

    public void setTaskListContents(JList<MappedElement> taskList) {
        DefaultListModel<MappedElement> model = (DefaultListModel<MappedElement>) taskList.getModel();
        model.removeAllElements();
        List<MappedElement> elements = taskCollection.getPossibleTasks();
        for (MappedElement me : elements) {
            // Filter by task collection
            if (taskFilterData.matches(taskCollection, me.getId())) {
                // Matches the task!
                model.addElement(me);
            }
        }
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

    public void setTaskInfo(MappedElement selectedTask, JEditorPane infoArea) {
        infoArea.setText(taskCollection.getTaskDescription(selectedTask.getId()).getInfoHTML());
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

    public TaskFilterData getTaskFilterData() {
        return taskFilterData;
    }

    public void showTaskFilterView(JList<MappedElement> taskList) {
        taskFilterForm.setVisible(true);
        // Blocks until closed
        setTaskListContents(taskList);
    }

}
