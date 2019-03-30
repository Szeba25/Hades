package hu.szeba.hades.main.view.elements;

import hu.szeba.hades.main.io.DescriptionFile;

public class TaskElement extends StatefulElement {

    private DescriptionFile description;

    public TaskElement(String id, String title, DescriptionFile description) {
        super(id, title);
        this.description = description;
    }

    public DescriptionFile getDescription() {
        return description;
    }

}
