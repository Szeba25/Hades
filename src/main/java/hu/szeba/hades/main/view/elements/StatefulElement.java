package hu.szeba.hades.main.view.elements;

import java.util.ArrayList;
import java.util.List;

public class StatefulElement extends MappedElement {

    private AbstractState state;
    private List<String> prerequisites;

    public StatefulElement(String id, String title) {
        super(id, title);
        this.state = AbstractState.AVAILABLE;
        this.prerequisites = new ArrayList<>();
    }

    public void setState(AbstractState state) {
        this.state = state;
    }

    public void setPrerequisites(List<String> prerequisites) {
        this.prerequisites = prerequisites;
    }

    public AbstractState getState() {
        return state;
    }

    public List<String> getPrerequisites() {
        return prerequisites;
    }

}
