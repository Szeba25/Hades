package hu.szeba.hades.model.task.data;

public class TaskDescription {

    private final String taskId;
    private final String taskTitle;
    private final String shortDescription;
    private final String longDescription;

    public TaskDescription(String taskId, String taskTitle, String shortDescription, String longDescription) {
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }
}
