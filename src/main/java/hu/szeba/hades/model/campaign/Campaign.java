package hu.szeba.hades.model.campaign;

import hu.szeba.hades.meta.Options;
import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.model.task.TaskFactory;
import hu.szeba.hades.model.task.TaskFactoryDecider;
import hu.szeba.hades.model.task.UnsupportedProgrammingLanguageException;
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
        this.campaignDirectory = new File(Options.getCampaignDatabasePath(), campaignName);
        this.campaignWorkingDirectory = new File(Options.getWorkingDirectoryPath(), campaignName);
        this.campaignName = campaignName;

        // TODO: Replace with loading from campaign (module) metadata.
        language = TaskFactoryDecider.SupportedLanguages.C;

        loadTaskNames();
    }

    private void loadTaskNames() {
        taskNames = campaignDirectory.list(DirectoryFileFilter.INSTANCE);
        int i = 0;
        for (String taskName : taskNames) {
            taskNames[i] = taskName;
            i++;
        }
        Arrays.sort(taskNames);
    }

    public Task createTask(String taskName) throws UnsupportedProgrammingLanguageException {
        TaskFactory taskFactory = TaskFactoryDecider.decideFactory(language);
        return taskFactory.getTask(campaignDirectory, campaignWorkingDirectory, taskName);
    }

    public String[] getTaskNames() {
        return taskNames;
    }

}
