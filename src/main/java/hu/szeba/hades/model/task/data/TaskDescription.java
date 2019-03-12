package hu.szeba.hades.model.task.data;

public class TaskDescription {

    private final String taskTitle;
    private final String shortDescription;
    private final String instructions;
    private final String story;

    public TaskDescription(String taskTitle, String shortDescription, String instructions, String story) {
        this.taskTitle = taskTitle;
        this.shortDescription = shortDescription;
        this.instructions = instructions;
        this.story = story;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getStory() {
        return story;
    }

}
