package hu.szeba.hades.view.task;

import java.util.Map;

public class TaskFilterData {

    private String titleFilter;
    private int difficultyFilter;
    private String statusFilter;
    private Map<String, Boolean> tagFilters;

    public TaskFilterData(String titleFilter, int difficultyFilter, String statusFilter, Map<String, Boolean> tagFilters) {
        this.titleFilter = titleFilter;
        this.difficultyFilter = difficultyFilter;
        this.statusFilter = statusFilter;
        this.tagFilters = tagFilters;
    }

    public String getTitleFilter() {
        return titleFilter;
    }

    public int getDifficultyFilter() {
        return difficultyFilter;
    }

    public String getStatusFilter() {
        return statusFilter;
    }

    public Map<String, Boolean> getTagFilters() {
        return tagFilters;
    }

}
