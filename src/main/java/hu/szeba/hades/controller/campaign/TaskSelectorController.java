package hu.szeba.hades.controller.campaign;

import hu.szeba.hades.controller.task.TaskSolvingController;
import hu.szeba.hades.model.campaign.Campaign;
import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.view.campaign.TaskSelectorView;
import hu.szeba.hades.view.task.TaskSolvingView;

public class TaskSelectorController {

    private TaskSelectorView view;
    private Campaign campaign;

    public TaskSelectorController(TaskSelectorView view, Campaign campaign) {
        this.view = view;
        this.campaign = campaign;
        setTaskListContents();
    }

    private void setTaskListContents() {
        view.setTaskListContents(campaign.getTaskNames());
    }

    public void loadNewTask() {
        String selectedTaskName = view.getSelectedTaskName();
        if (selectedTaskName != null) {
            view.hide();
            Task task = campaign.createTask(selectedTaskName);
            TaskSolvingView taskSolvingView = new TaskSolvingView(view);
            TaskSolvingController taskSolvingController = new TaskSolvingController(taskSolvingView, task);
            taskSolvingView.registerController(taskSolvingController);
            taskSolvingView.show();
        }
    }
}
