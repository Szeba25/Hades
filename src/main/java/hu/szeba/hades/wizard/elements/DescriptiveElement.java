package hu.szeba.hades.wizard.elements;

public class DescriptiveElement {

    private String id;
    private String title;
    private String display;

    public DescriptiveElement(String id, String title) {
        this.id = id;
        this.title = title;
        this.display = "(" + id + ") -> " + title;
    }

    public String toString() {
        return display;
    }

}
