package hu.szeba.hades.controller.topic;

import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.model.task.data.MissingResultFileException;
import hu.szeba.hades.model.task.languages.InvalidLanguageException;
import hu.szeba.hades.model.topic.Topic;
import hu.szeba.hades.view.BaseView;
import hu.szeba.hades.view.task.TaskSolvingView;

import javax.swing.*;
import java.io.IOException;
import java.util.Set;

public class TaskSelectorController {

    private Topic topic;

    public TaskSelectorController(Topic topic) {
        this.topic = topic;
    }

    public void setTaskListContents(JList<String> taskList) {
        taskList.setListData(topic.getTaskTitles().toArray(new String[0]));
    }

    public Set<String> getUnavailableTaskIds() {
        return topic.getUnavailableTaskIds();
    }

    public void loadNewTask(String selectedTaskId,
                            BaseView parentView) throws InvalidLanguageException, IOException, MissingResultFileException {
        Task task = topic.createTask(selectedTaskId, false);
        parentView.hideView();
        TaskSolvingView taskSolvingView = new TaskSolvingView(parentView, task);
        taskSolvingView.showViewMaximized();
    }

    public void continueTask(String selectedTaskId,
                             BaseView parentView) throws InvalidLanguageException, IOException, MissingResultFileException {
        Task task = topic.createTask(selectedTaskId, true);
        parentView.hideView();
        TaskSolvingView taskSolvingView = new TaskSolvingView(parentView, task);
        taskSolvingView.showViewMaximized();
    }

    public void setTaskShortDescription(String taskId, JEditorPane descriptionArea) {
        descriptionArea.setText(topic.getTaskDescription(taskId).getShortDescription());
    }

    public String getTaskIdByTaskTitle(String taskTitle) {
        return topic.getTaskIdByTaskTitle(taskTitle);
    }

    public boolean progressExists(String taskId) {
        return topic.progressExistsForTask(taskId);
    }

    public void generateUnavailableTaskIds() {
        topic.generateUnavailableTaskIds();
    }
}
