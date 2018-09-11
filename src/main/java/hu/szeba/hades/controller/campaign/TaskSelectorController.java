package hu.szeba.hades.controller.campaign;

import hu.szeba.hades.controller.task.TaskSolvingController;
import hu.szeba.hades.model.campaign.Campaign;
import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.view.campaign.TaskSelectorView;
import hu.szeba.hades.view.task.TaskSolvingView;

public class TaskSelectorController {

    private TaskSelectorView taskSelectorView;
    private Campaign campaign;

    public TaskSelectorController(TaskSelectorView taskSelectorView, Campaign campaign) {
        this.taskSelectorView = taskSelectorView;
        this.campaign = campaign;
        setTaskListContents();
    }

    private void setTaskListContents() {
        taskSelectorView.setTaskListContents(campaign.getTaskNames());
    }

    public void loadNewTask() {
        String selectedTaskName = taskSelectorView.getSelectedTaskName();
        if (selectedTaskName != null) {
            taskSelectorView.hide();
            Task task = campaign.createTask(selectedTaskName);
            TaskSolvingView taskSolvingView = new TaskSolvingView(taskSelectorView);
            TaskSolvingController taskSolvingController = new TaskSolvingController(taskSolvingView, task);
            taskSolvingView.registerController(taskSolvingController);
            taskSolvingView.show();
        }
    }
}
