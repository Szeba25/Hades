package hu.szeba.hades.wizard.view.elements;

import hu.szeba.hades.main.view.elements.MappedElement;

public class DescriptiveElement extends MappedElement {

    public DescriptiveElement(String id, String title) {
        super(id, title);
    }

    @Override
    public String toString() {
        return getId() + ": " + getTitle();
    }

}
