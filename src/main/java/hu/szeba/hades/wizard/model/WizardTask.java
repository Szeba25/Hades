package hu.szeba.hades.wizard.model;

import hu.szeba.hades.main.io.DescriptionFile;

import java.io.File;
import java.io.IOException;

public class WizardTask {

    private String taskId;
    private File taskPath;
    private DescriptionFile description;

    public WizardTask(String taskId, File taskPath, DescriptionFile description) {
        this.taskId = taskId;
        this.taskPath = taskPath;
        this.description = description;
    }

    public void save() throws IOException {
        description.save();
    }

    public String getTitle() {
        return description.getTitle();
    }

    public void setTitle(String title) {
        description.setTitle(title);
    }

}
