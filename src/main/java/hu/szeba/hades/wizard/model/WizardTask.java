package hu.szeba.hades.wizard.model;

import hu.szeba.hades.main.io.DescriptionXMLFile;
import hu.szeba.hades.main.model.task.data.TaskDescription;

import java.io.File;

public class WizardTask {

    private String taskId;
    private File taskPath;
    private DescriptionXMLFile descriptionXMLFile;
    private TaskDescription description;

    public WizardTask(String taskId, File taskPath, DescriptionXMLFile descriptionXMLFile) {
        this.taskId = taskId;
        this.taskPath = taskPath;
        this.descriptionXMLFile = descriptionXMLFile;

        description = descriptionXMLFile.parse(false);
    }

    public void save() {
        // TODO
    }

    public String getTitle() {
        return description.getTaskTitle();
    }

    public void setTitle(String title) {
        description.setTitle(title);
    }

}
