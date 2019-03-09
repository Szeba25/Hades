package hu.szeba.hades.model.task.data;

public class TaskStory {

    private final String taskId;
    private final String text;

    public TaskStory(String taskId, String text) {
        this.taskId = taskId;
        this.text = text;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getText() {
        return text;
    }
}
