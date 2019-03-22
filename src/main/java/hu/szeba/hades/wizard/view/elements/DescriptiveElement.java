package hu.szeba.hades.wizard.view.elements;

import hu.szeba.hades.main.view.elements.MappedElement;

public class DescriptiveElement extends MappedElement {

    private String display;

    public DescriptiveElement(String id, String title) {
        super(id, title);
        this.display = "(" + id + ") -> " + title;
    }

    @Override
    public String toString() {
        return display;
    }

    public void setTitle(String title) {
        super.setTitle(title);
        this.display = "(" + getId() + ") -> " + title;
    }
}
