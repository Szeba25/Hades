package hu.szeba.hades.model.campaign;

import org.apache.commons.io.filefilter.DirectoryFileFilter;

import java.io.File;
import java.util.Arrays;

public class Campaign {

    private File campaignDirectory;
    private String campaignName;
    private String[] taskNames;

    public Campaign(File campaignDatabaseDirectory, String campaignName) {
        this.campaignDirectory = new File(campaignDatabaseDirectory, campaignName);
        this.campaignName = campaignName;
        loadTaskNames();
    }

    public String[] getTaskNames() {
        return taskNames;
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
}
