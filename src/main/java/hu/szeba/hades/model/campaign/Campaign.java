package hu.szeba.hades.model.campaign;

import hu.szeba.hades.meta.Options;
import hu.szeba.hades.model.task.*;
import hu.szeba.hades.model.task.languages.SupportedLanguages;
import hu.szeba.hades.model.task.languages.UnsupportedProgrammingLanguageException;
import hu.szeba.hades.model.task.taskfactory.TaskFactory;
import hu.szeba.hades.model.task.taskfactory.TaskFactoryDecider;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

import java.io.File;
import java.util.Arrays;

public class Campaign {

    private File campaignDirectory;
    private File campaignWorkingDirectory;
    private String campaignName;
    private String language;
    private String[] taskNames;

    public Campaign(String campaignName) {
        this.campaignDirectory =
                new File(Options.getCampaignDatabasePath(), "campaigns/" + campaignName);
        this.campaignWorkingDirectory =
                new File(Options.getWorkingDirectoryPath(), "campaigns/" + campaignName);
        this.campaignName = campaignName;

        // TODO: Replace with loading from campaign (module) metadata.
        language = SupportedLanguages.C;

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
        TaskFactory taskFactory = TaskFactoryDecider.decideFactory(language);
        return taskFactory.getTask(taskName);
    }

    public String[] getTaskNames() {
        return taskNames;
    }

}
