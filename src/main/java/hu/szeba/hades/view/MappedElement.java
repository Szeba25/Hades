package hu.szeba.hades.view;

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

    @Override
    public String toString() {
        return title;
    }

}
