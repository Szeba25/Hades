package hu.szeba.hades.wizard.model;

import hu.szeba.hades.main.io.DescriptionFile;
import hu.szeba.hades.main.io.TabbedFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class WizardTask {

    private String taskId;
    private File taskPath;
    private DescriptionFile description;
    private TabbedFile regExIncludeFile;
    private TabbedFile regExExcludeFile;

    public WizardTask(String taskId, File taskPath, DescriptionFile description) throws IOException {
        this.taskId = taskId;
        this.taskPath = taskPath;
        this.description = description;
        this.regExIncludeFile = new TabbedFile(new File(taskPath, "regex/include.txt"));
        this.regExExcludeFile = new TabbedFile(new File(taskPath, "regex/exclude.txt")) ;
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
        return regExIncludeFile.getAllData();
    }

    public String getRegExExcludeData() {
        return regExExcludeFile.getAllData();
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
        regExIncludeFile.setAllData(data);
    }

    public void setRegExExcludeData(String data) {
        regExExcludeFile.setAllData(data);
    }

}
