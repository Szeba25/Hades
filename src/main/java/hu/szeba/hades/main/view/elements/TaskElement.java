package hu.szeba.hades.main.view.elements;

import hu.szeba.hades.main.model.task.data.TaskDescription;

public class TaskElement extends StatefulElement {

    private TaskDescription description;

    public TaskElement(String id, String title, TaskDescription description) {
        super(id, title);
        this.description = description;
    }

    public TaskDescription getDescription() {
        return description;
    }

}
