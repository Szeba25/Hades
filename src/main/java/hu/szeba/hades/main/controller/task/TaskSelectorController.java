package hu.szeba.hades.main.controller.task;

import hu.szeba.hades.main.form.task.TaskFilterForm;
import hu.szeba.hades.main.model.course.Course;
import hu.szeba.hades.main.model.course.CourseDatabase;
import hu.szeba.hades.main.model.course.Mode;
import hu.szeba.hades.main.model.course.TaskCollection;
import hu.szeba.hades.main.model.task.Task;
import hu.szeba.hades.main.model.task.data.MissingResultFileException;
import hu.szeba.hades.main.model.task.languages.InvalidLanguageException;
import hu.szeba.hades.main.view.ViewableFrame;
import hu.szeba.hades.main.view.elements.AbstractState;
import hu.szeba.hades.main.view.elements.MappedElement;
import hu.szeba.hades.main.view.elements.StatefulElement;
import hu.szeba.hades.main.view.elements.TaskElement;
import hu.szeba.hades.main.view.task.TaskFilterData;
import hu.szeba.hades.main.view.task.TaskSolvingView;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

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

    public void updateMode(JList<StatefulElement> taskCollectionList, MappedElement selectedMode)
            throws IOException {

        mode = course.loadMode(selectedMode.getId());
        setTaskCollectionListContents(taskCollectionList);
    }

    public void updateTaskCollection(JList<TaskElement> taskList,
                                     StatefulElement selectedTaskCollection)
            throws ParserConfigurationException, SAXException, IOException {

        taskCollection = mode.loadTaskCollection(selectedTaskCollection.getId(),
                selectedTaskCollection.getState() == AbstractState.UNAVAILABLE);
        taskFilterForm.addAllTags(taskCollection);
        setTaskListContents(taskList);
    }

    public void setCourseListContents(JComboBox<MappedElement> courseList) {
        courseList.removeAllItems();
        for (MappedElement element : courseDatabase.getPossibleCourses()) {
            courseList.addItem(element);
        }
    }

    private void setModeListContents(JComboBox<MappedElement> modeList) {
        modeList.removeAllItems();
        for (MappedElement element : course.getPossibleModes()) {
            modeList.addItem(element);
        }
    }

    private void setTaskCollectionListContents(JList<StatefulElement> taskCollectionList) {
        DefaultListModel<StatefulElement> model = (DefaultListModel<StatefulElement>) taskCollectionList.getModel();
        model.removeAllElements();
        for (StatefulElement element : mode.getPossibleTaskCollections()) {
            model.addElement(element);
        }
        taskCollectionList.setSelectedIndex(0);
    }

    private void setTaskListContents(JList<TaskElement> taskList) {
        DefaultListModel<TaskElement> model = (DefaultListModel<TaskElement>) taskList.getModel();
        model.removeAllElements();
        for (TaskElement element : taskCollection.getPossibleTasks()) {
            // Filter by task collection
            if (taskFilterData.matches(element)) {
                // Matches the task!
                model.addElement(element);
            }
        }
    }

    public void loadNewTask(TaskElement selectedTask, ViewableFrame viewableFrame)
            throws InvalidLanguageException, IOException, MissingResultFileException {

        Task task = taskCollection.createTask(selectedTask, false);
        viewableFrame.hideView();
        TaskSolvingView taskSolvingView = new TaskSolvingView(viewableFrame, task);
        taskSolvingView.showViewMaximized();
        taskSolvingView.showStoryDialog();
    }

    public void continueTask(TaskElement selectedTask, ViewableFrame viewableFrame)
            throws InvalidLanguageException, IOException, MissingResultFileException {

        Task task = taskCollection.createTask(selectedTask, true);
        viewableFrame.hideView();
        TaskSolvingView taskSolvingView = new TaskSolvingView(viewableFrame, task);
        taskSolvingView.showViewMaximized();
        taskSolvingView.showStoryDialog();
    }

    public void setTaskCollectionInfo(StatefulElement selectedTaskCollection, JTextField statusField,
                                      JTextField percentField, JTextField progressField, JTextField taskCountField,
                                      JList<String> prerequisitesList) {

        statusField.setText(selectedTaskCollection.getState().toString());
        percentField.setText((int)(taskCollection.getCompletionThreshold() * 100) + "%");
        progressField.setText(taskCollection.getCompletedTasksCount() + " / " + taskCollection.getTaskCompletionCount());
        taskCountField.setText(String.valueOf(taskCollection.getPossibleTasks().size()));

        DefaultListModel<String> model = (DefaultListModel<String>) prerequisitesList.getModel();
        model.removeAllElements();
        for (String req : selectedTaskCollection.getPrerequisites()) {
            model.addElement(req);
        }
    }

    public void setTaskInfo(TaskElement selectedTask, JTextField statusField,
                            JTextField difficultyField, JTextField lengthField,
                            JList<String> prerequisitesList) {

        statusField.setText(selectedTask.getState().toString());
        difficultyField.setText(selectedTask.getDescription().getDifficulty());
        lengthField.setText(selectedTask.getDescription().getLength());

        DefaultListModel<String> model = (DefaultListModel<String>) prerequisitesList.getModel();
        model.removeAllElements();
        for (String req : selectedTask.getPrerequisites()) {
            model.addElement(req);
        }
    }

    public void generateCachedData() {
        mode.generateCachedData();
    }

    public void showTaskFilterView(JList<TaskElement> taskList, JFrame parentView) {
        taskFilterForm.setLocationRelativeTo(parentView);
        taskFilterForm.setVisible(true);
        // Blocks until closed
        setTaskListContents(taskList);
    }

}
