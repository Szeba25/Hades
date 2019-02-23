package hu.szeba.hades.controller.topic;

import hu.szeba.hades.model.task.data.MissingResultFileException;
import hu.szeba.hades.model.topic.Topic;
import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.model.task.languages.InvalidLanguageException;
import hu.szeba.hades.view.BaseView;
import hu.szeba.hades.view.task.TaskSolvingView;

import javax.swing.*;
import java.io.IOException;

public class TaskSelectorController {

    private Topic topic;

    public TaskSelectorController(Topic topic) {
        this.topic = topic;
    }

    public void setTaskListContents(JList<String> taskList) {
        taskList.setListData(topic.getTaskNames().toArray(new String[0]));
    }

    public void loadNewTask(String selectedTaskName,
                            BaseView parentView) throws InvalidLanguageException, IOException, MissingResultFileException {
        Task task = topic.createTask(selectedTaskName, false);
        parentView.hideView();
        TaskSolvingView taskSolvingView = new TaskSolvingView(parentView, task);
        taskSolvingView.showViewMaximized();
    }

    public void continueTask(String selectedTaskName,
                             BaseView parentView) throws InvalidLanguageException, IOException, MissingResultFileException {
        Task task = topic.createTask(selectedTaskName, true);
        parentView.hideView();
        TaskSolvingView taskSolvingView = new TaskSolvingView(parentView, task);
        taskSolvingView.showViewMaximized();

    }

    public boolean progressExists(String taskName) {
        return topic.progressExists(taskName);
    }
}
