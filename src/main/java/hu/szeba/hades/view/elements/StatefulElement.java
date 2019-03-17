package hu.szeba.hades.view.elements;

import java.util.ArrayList;
import java.util.List;

public class StatefulElement {

    private String id;
    private String title;
    private AbstractState state;
    private List<String> parentTitles;

    public StatefulElement(String id, String title) {
        this.id = id;
        this.title = title;
        this.state = AbstractState.AVAILABLE;
        this.parentTitles = new ArrayList<>();
    }

    public void setState(AbstractState state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public AbstractState getState() {
        return state;
    }

    public List<String> getParentTitles() {
        return parentTitles;
    }

}
