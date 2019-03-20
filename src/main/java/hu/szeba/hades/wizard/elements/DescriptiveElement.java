package hu.szeba.hades.wizard.elements;

import hu.szeba.hades.view.elements.MappedElement;

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

}
