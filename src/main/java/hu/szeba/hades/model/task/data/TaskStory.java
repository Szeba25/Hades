package hu.szeba.hades.model.task.data;

public class TaskStory {

    private final String taskId;
    private final String taskStory;

    public TaskStory(String taskId, String taskStory) {
        this.taskId = taskId;
        this.taskStory = taskStory;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTaskStory() {
        return taskStory;
    }
}
