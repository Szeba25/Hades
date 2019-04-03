package hu.szeba.hades.main.view.elements;

import hu.szeba.hades.main.io.DescriptionFile;
import hu.szeba.hades.main.meta.Languages;

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
        this.difficultyFilter = Languages.translate("All");
        this.lengthFilter = Languages.translate("All");
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
        DescriptionFile description = element.getDescription();

        boolean matchesTitle = description.getTitle().toLowerCase().contains(titleFilter.toLowerCase());

        boolean matchesDifficulty = difficultyFilter.equals(Languages.translate("All")) ||
                Languages.translate(description.getDifficulty()).equals(difficultyFilter);

        boolean matchesLength = lengthFilter.equals(Languages.translate("All")) ||
                Languages.translate(description.getLength()).equals(lengthFilter);

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
