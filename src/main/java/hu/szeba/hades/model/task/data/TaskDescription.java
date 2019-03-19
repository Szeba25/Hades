package hu.szeba.hades.model.task.data;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.IOException;
import java.util.List;

public class TaskDescription {

    private final String taskTitle;
    private final String shortDescription;
    private final String instructions;
    private final String story;
    private final String difficulty;
    private final String length;
    private final List<String> tags;

    private HTMLDocument shortDocument;

    public TaskDescription(String taskTitle, String shortDescription, String instructions, String story,
                           String difficulty, String length, List<String> tags) {
        this.taskTitle = taskTitle;
        this.shortDescription = shortDescription +
                "<footer>" +
                "<p><b>Tags: </b>" + String.join(", ", tags) + "</p>" +
                "</footer>";
        this.instructions = instructions;
        this.story = story;
        this.difficulty = difficulty;
        this.length = length;
        this.tags = tags;
        this.shortDocument = null;
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

    public String getDifficulty() {
        return difficulty;
    }

    public String getLength() {
        return length;
    }

    public List<String> getTags() {
        return tags;
    }

    public Document getShortDocument(HTMLEditorKit kit) {
        if (shortDocument == null) {
            shortDocument = (HTMLDocument) kit.createDefaultDocument();
            try {
                kit.insertHTML(shortDocument, 0, shortDescription, 0, 0, null);
            } catch (BadLocationException | IOException e) {
                e.printStackTrace();
            }
        }
        return shortDocument;
    }

}
