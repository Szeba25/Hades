package hu.szeba.hades.model.campaign;

import hu.szeba.hades.meta.Options;
import hu.szeba.hades.model.task.Task;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

import java.io.File;
import java.util.Arrays;

public class Campaign {

    private Options options;
    private File campaignDirectory;
    private File campaignWorkingDirectory;
    private String campaignName;
    private String[] taskNames;

    public Campaign(Options options, String campaignName) {
        this.options = options;
        this.campaignDirectory = new File(options.getCampaignDatabasePath(), campaignName);
        this.campaignWorkingDirectory = new File(options.getWorkingDirectoryPath(), campaignName);
        this.campaignName = campaignName;
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

    public String[] getTaskNames() {
        return taskNames;
    }

    public Task createTask(String taskName) {
        return new Task(options, campaignDirectory, campaignWorkingDirectory, taskName);
    }

}
