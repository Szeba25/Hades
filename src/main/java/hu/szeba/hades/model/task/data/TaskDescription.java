package hu.szeba.hades.model.task.data;

public class TaskDescription {

    private final String taskName;
    private final String taskTitle;
    private final String shortDescription;
    private final String longDescription;

    public TaskDescription(String taskName, String taskTitle, String shortDescription, String longDescription) {
        this.taskName = taskName;
        this.taskTitle = taskTitle;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
    }

    public String getTaskName() {
        return taskName;
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
