package hu.szeba.hades.controller.topic;

import hu.szeba.hades.model.task.data.MissingResultFileForProgramInputException;
import hu.szeba.hades.model.topic.Topic;
import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.model.task.languages.UnsupportedProgrammingLanguageException;
import hu.szeba.hades.view.BaseView;
import hu.szeba.hades.view.task.TaskSolvingView;

import javax.swing.*;
import java.io.IOException;

public class TaskSelectorController {

    private Topic topic;

    public TaskSelectorController(Topic topic) {
        this.topic = topic;
    }

    public void setTaskListContents(JList taskList) {
        taskList.setListData(topic.getTaskNames().toArray());
    }

    public void loadNewTask(String selectedTaskName,
                            BaseView parentView) throws UnsupportedProgrammingLanguageException, IOException, MissingResultFileForProgramInputException {
        if (selectedTaskName != null) {
            Task task = topic.createTask(selectedTaskName, false);
            parentView.hideView();
            TaskSolvingView taskSolvingView = new TaskSolvingView(parentView, task);
            taskSolvingView.showViewMaximized();
        }
    }

    public void continueTask(String selectedTaskName,
                             BaseView parentView) throws UnsupportedProgrammingLanguageException, IOException, MissingResultFileForProgramInputException {
        if (selectedTaskName != null) {
            Task task = topic.createTask(selectedTaskName, true);
            parentView.hideView();
            TaskSolvingView taskSolvingView = new TaskSolvingView(parentView, task);
            taskSolvingView.showViewMaximized();
        }
    }
}
