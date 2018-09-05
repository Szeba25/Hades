package hu.szeba.hades.model.campaign;

import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.model.task.TaskFactory;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Campaign {

    private File campaignDirectory;
    private String campaignName;
    private List<Task> tasks;

    public Campaign(File campaignDatabaseDirectory, String campaignName) {
        campaignDirectory = new File(campaignDatabaseDirectory, campaignName);
        this.campaignName = campaignName;
        loadTasks();
    }

    public String[] getTaskNames() {
        String[] taskNames = new String[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            taskNames[i] = tasks.get(i).getName();
        }
        return taskNames;
    }

    private void loadTasks() {
        tasks = new ArrayList<>();
        String[] taskNames = campaignDirectory.list(DirectoryFileFilter.INSTANCE);
        for (String taskName : taskNames) {
            tasks.add(TaskFactory.createTask(campaignDirectory, taskName));
        }
    }

}
