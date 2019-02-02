package hu.szeba.hades.model.task.data;

public class TaskDescription {

    private final String taskName;
    private final String title;
    private final String shortDescription;
    private final String longDescription;

    public TaskDescription(String taskName, String title, String shortDescription, String longDescription) {
        this.taskName = taskName;
        this.title = title;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTitle() {
        return title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }
}
