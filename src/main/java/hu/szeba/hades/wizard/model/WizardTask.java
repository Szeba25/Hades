package hu.szeba.hades.wizard.model;

import hu.szeba.hades.main.io.DescriptionFile;
import hu.szeba.hades.main.model.task.data.SourceFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class WizardTask {

    private String taskId;
    private File taskPath;
    private DescriptionFile description;
    private SourceFile regExIncludeFile;
    private SourceFile regExExcludeFile;

    public WizardTask(String taskId, File taskPath, DescriptionFile description) throws IOException {
        this.taskId = taskId;
        this.taskPath = taskPath;
        this.description = description;
        this.regExIncludeFile = new SourceFile(new File(taskPath, "regex/include.txt"), false);
        this.regExExcludeFile = new SourceFile(new File(taskPath, "regex/exclude.txt"), false) ;
    }

    public void save() throws IOException {
        description.save();
        regExIncludeFile.save();
        regExExcludeFile.save();
    }

    public String getTitle() {
        return description.getTitle();
    }

    public String getDifficulty() {
        return description.getDifficulty();
    }

    public String getLength() {
        return description.getLength();
    }

    public String getTags() {
        return String.join("\n", description.getTags());
    }

    public String getRegExIncludeData() {
        return regExIncludeFile.getData();
    }

    public String getRegExExcludeData() {
        return regExExcludeFile.getData();
    }

    public void setTitle(String title) {
        description.setTitle(title);
    }

    public void setDifficulty(String difficulty) {
        description.setDifficulty(difficulty);
    }

    public void setLength(String length) {
        description.setLength(length);
    }

    public void setTags(String text) {
        String[] tags = text.split("\n");
        description.getTags().clear();
        description.getTags().addAll(Arrays.asList(tags));
    }

    public void setRegExIncludeData(String data) {
        regExIncludeFile.setData(data);
    }

    public void setRegExExcludeData(String data) {
        regExExcludeFile.setData(data);
    }

}
