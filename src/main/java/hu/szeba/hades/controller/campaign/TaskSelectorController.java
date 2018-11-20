package hu.szeba.hades.controller.campaign;

import hu.szeba.hades.model.campaign.Campaign;
import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.model.task.languages.UnsupportedProgrammingLanguageException;
import hu.szeba.hades.view.BaseView;
import hu.szeba.hades.view.task.TaskSolvingView;

import javax.swing.*;
import java.io.IOException;

public class TaskSelectorController {

    private Campaign campaign;

    public TaskSelectorController(Campaign campaign) {
        this.campaign = campaign;
    }

    public void setTaskListContents(JList taskList) {
        taskList.setListData(campaign.getTaskNames().toArray());
    }

    public void loadNewTask(String selectedTaskName,
                            BaseView parentView) throws UnsupportedProgrammingLanguageException, IOException {
        if (selectedTaskName != null) {
            Task task = campaign.createTask(selectedTaskName, false);
            parentView.hideView();
            TaskSolvingView taskSolvingView = new TaskSolvingView(parentView, task);
            taskSolvingView.showViewMaximized();
        }
    }

    public void continueTask(String selectedTaskName,
                             BaseView parentView) throws UnsupportedProgrammingLanguageException, IOException {
        if (selectedTaskName != null) {
            Task task = campaign.createTask(selectedTaskName, true);
            parentView.hideView();
            TaskSolvingView taskSolvingView = new TaskSolvingView(parentView, task);
            taskSolvingView.showViewMaximized();
        }
    }
}
