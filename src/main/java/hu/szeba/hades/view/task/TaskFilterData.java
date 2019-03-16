package hu.szeba.hades.view.task;

import hu.szeba.hades.model.task.TaskCollection;
import hu.szeba.hades.model.task.data.TaskDescription;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskFilterData {

    public enum TaskStatus {
        ALL, AVAILABLE, COMPLETED, IN_PROGRESS, UNAVAILABLE;
        public String toString() {
            return (name().charAt(0) + name().toLowerCase().substring(1)).replace("_", " ");
        }
    }

    private String titleFilter;
    private String difficultyFilter;
    private String lengthFilter;
    private TaskStatus statusFilter;
    private Map<String, Boolean> tagFilters;

    public TaskFilterData() {
        tagFilters = new HashMap<>();
        reset();
    }

    public void set(String titleFilter, String difficultyFilter, String lengthFilter, TaskStatus statusFilter,
                    Map<String, Boolean> tagFilters) {
        this.titleFilter = titleFilter;
        this.difficultyFilter = difficultyFilter;
        this.lengthFilter = lengthFilter;
        this.statusFilter = statusFilter;
        this.tagFilters = tagFilters;
    }

    public void reset() {
        this.titleFilter = "";
        this.difficultyFilter = "All";
        this.lengthFilter = "All";
        this.statusFilter = TaskStatus.ALL;
        this.tagFilters.clear();
    }

    public String getTitleFilter() {
        return titleFilter;
    }

    public String getDifficultyFilter() {
        return difficultyFilter;
    }

    public String getLengthFilter() {
        return lengthFilter;
    }

    public TaskStatus getStatusFilter() {
        return statusFilter;
    }

    public Map<String, Boolean> getTagFilters() {
        return tagFilters;
    }

    public boolean matches(TaskCollection collection, String taskId) {
        TaskDescription description = collection.getTaskDescription(taskId);

        boolean matchesTitle = description.getTaskTitle().toLowerCase().contains(titleFilter);

        boolean matchesDifficulty = difficultyFilter.equals("All") ||
                description.getDifficulty().equals(difficultyFilter);

        boolean matchesLength = lengthFilter.equals("All") ||
                description.getLength().equals(lengthFilter);

        boolean matchesStatus = false;
        switch (statusFilter) {
            case ALL:
                matchesStatus = true;
                break;
            case AVAILABLE:
                matchesStatus = !collection.isTaskUnavailable(taskId);
                break;
            case COMPLETED:
                matchesStatus = collection.isTaskCompleted(taskId);
                break;
            case IN_PROGRESS:
                matchesStatus = collection.isTaskStarted(taskId) && !collection.isTaskCompleted(taskId);
                break;
            case UNAVAILABLE:
                matchesStatus = collection.isTaskUnavailable(taskId);
                break;
        }

        boolean matchesTags = false;
        List<String> tags = description.getTags();
        for (String tag : tags) {
            Boolean filterValue = tagFilters.get(tag);
            matchesTags = matchesTags || (filterValue == null || filterValue);
        }

        return matchesTitle && matchesDifficulty && matchesLength && matchesStatus && matchesTags;
    }
}
