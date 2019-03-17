package hu.szeba.hades.view.elements;

public class MappedElement {

    private final String id;
    private final String title;

    public MappedElement(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }

}
