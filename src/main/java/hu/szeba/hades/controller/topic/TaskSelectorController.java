package hu.szeba.hades.controller.topic;

import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.model.task.data.MissingResultFileException;
import hu.szeba.hades.model.task.languages.InvalidLanguageException;
import hu.szeba.hades.model.task.TaskCollection;
import hu.szeba.hades.view.BaseView;
import hu.szeba.hades.view.task.TaskSolvingView;

import javax.swing.*;
import java.io.IOException;

public class TaskSelectorController {

    private TaskCollection taskCollection;

    public TaskSelectorController(TaskCollection taskCollection) {
        this.taskCollection = taskCollection;
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
