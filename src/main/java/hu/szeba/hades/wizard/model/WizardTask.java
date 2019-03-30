package hu.szeba.hades.wizard.model;

import hu.szeba.hades.main.io.DescriptionFile;

import java.io.File;

public class WizardTask {

    private String taskId;
    private File taskPath;
    private DescriptionFile description;

    public WizardTask(String taskId, File taskPath, DescriptionFile descriptionFile) {
        this.taskId = taskId;
        this.taskPath = taskPath;
        this.description = descriptionFile;
    }

    public void save() {
        // TODO
    }

    public String getTitle() {
        return description.getTitle();
    }

    public void setTitle(String title) {
        description.setTitle(title);
    }

}
