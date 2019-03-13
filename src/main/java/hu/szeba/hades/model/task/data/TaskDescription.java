package hu.szeba.hades.model.task.data;

import java.util.List;

public class TaskDescription {

    private final String taskTitle;
    private final String shortDescription;
    private final String instructions;
    private final String story;
    private final int difficulty;
    private final int length;
    private final List<String> tags;

    public TaskDescription(String taskTitle, String shortDescription, String instructions, String story,
                           int difficulty, int length, List<String> tags) {
        this.taskTitle = taskTitle;
        this.shortDescription = shortDescription;
        this.instructions = instructions;
        this.story = story;
        this.difficulty = difficulty;
        this.length = length;
        this.tags = tags;
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

    public int getDifficulty() {
        return difficulty;
    }

    public int getLength() {
        return length;
    }

    public List<String> getTags() {
        return tags;
    }

}
