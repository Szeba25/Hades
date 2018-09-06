package hu.szeba.hades.model.campaign;

import hu.szeba.hades.model.task.Task;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

import java.io.File;
import java.util.*;

public class Campaign {

    private File campaignDirectory;
    private String campaignName;
    private Map<String, Task> tasks;

    public Campaign(File campaignDatabaseDirectory, String campaignName) {
        this.campaignDirectory = new File(campaignDatabaseDirectory, campaignName);
        this.campaignName = campaignName;
        loadTasks();
    }

    private void loadTasks() {
        tasks = new HashMap<>();
        String[] taskNames = campaignDirectory.list(DirectoryFileFilter.INSTANCE);
        for (String taskName : taskNames) {
            tasks.put(taskName, new Task(campaignDirectory, taskName));
        }
    }

    public String[] getTaskNames() {
        String[] taskNames = new String[tasks.size()];
        int i = 0;
        for (String taskName : tasks.keySet()) {
            taskNames[i] = taskName;
            i++;
        }
        Arrays.sort(taskNames);
        return taskNames;
    }

}
