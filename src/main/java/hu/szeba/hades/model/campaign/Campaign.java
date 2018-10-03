package hu.szeba.hades.model.campaign;

import hu.szeba.hades.meta.Options;
import hu.szeba.hades.model.task.*;
import hu.szeba.hades.model.task.languages.SupportedLanguages;
import hu.szeba.hades.model.task.languages.UnsupportedProgrammingLanguageException;
import hu.szeba.hades.model.task.taskfactory.TaskFactory;
import hu.szeba.hades.model.task.taskfactory.TaskFactoryDecider;

import java.io.File;

public class Campaign {

    private File campaignDirectory;
    private File campaignWorkingDirectory;
    private String campaignName;
    private String[] taskNames;

    // Language cannot change!
    private final String language;

    public Campaign(String courseName, String campaignName, String language) {
        this.campaignDirectory =
                new File(Options.getDatabasePath(),
                        "courses/" + courseName + "/" + campaignName);
        this.campaignWorkingDirectory =
                new File(Options.getWorkingDirectoryPath(),
                        "courses/" + courseName + "/" + campaignName);
        this.campaignName = campaignName;

        this.language = language;

        loadTaskNames();

    }

    private void loadTaskNames() {
        // TODO: Read from meta file.
        taskNames = new String[3];
        taskNames[0] = "Task1";
        taskNames[1] = "Task2";
        taskNames[2] = "Task3";
    }

    public Task createTask(String taskName) throws UnsupportedProgrammingLanguageException {
        return TaskFactoryDecider.decideFactory(language).getTask(taskName);
    }

    public String[] getTaskNames() {
        return taskNames;
    }

}
