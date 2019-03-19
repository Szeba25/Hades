package hu.szeba.hades.main.view.task;

import hu.szeba.hades.main.model.task.data.TaskDescription;
import hu.szeba.hades.main.view.elements.AbstractState;
import hu.szeba.hades.main.view.elements.TaskElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskFilterData {

    private String titleFilter;
    private String difficultyFilter;
    private String lengthFilter;
    private AbstractState stateFilter;
    private Map<String, Boolean> tagFilters;

    public TaskFilterData() {
        tagFilters = new HashMap<>();
        reset();
    }

    public void set(String titleFilter, String difficultyFilter, String lengthFilter, AbstractState stateFilter,
                    Map<String, Boolean> tagFilters) {
        this.titleFilter = titleFilter;
        this.difficultyFilter = difficultyFilter;
        this.lengthFilter = lengthFilter;
        this.stateFilter = stateFilter;
        this.tagFilters = tagFilters;
    }

    public void reset() {
        this.titleFilter = "";
        this.difficultyFilter = "All";
        this.lengthFilter = "All";
        this.stateFilter = AbstractState.ALL;
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

    public AbstractState getStateFilter() {
        return stateFilter;
    }

    public Map<String, Boolean> getTagFilters() {
        return tagFilters;
    }

    public boolean matches(TaskElement element) {
        TaskDescription description = element.getDescription();

        boolean matchesTitle = description.getTaskTitle().toLowerCase().contains(titleFilter);

        boolean matchesDifficulty = difficultyFilter.equals("All") ||
                description.getDifficulty().equals(difficultyFilter);

        boolean matchesLength = lengthFilter.equals("All") ||
                description.getLength().equals(lengthFilter);

        boolean matchesStatus = (stateFilter == AbstractState.ALL) || stateFilter == element.getState();

        boolean matchesTags = false;
        List<String> tags = description.getTags();
        for (String tag : tags) {
            Boolean filterValue = tagFilters.get(tag);
            matchesTags = matchesTags || (filterValue == null || filterValue);
        }

        return matchesTitle && matchesDifficulty && matchesLength && matchesStatus && matchesTags;
    }
}
