package hu.szeba.hades.control.task;

public class TaskSolvingControl {

    private TaskSolvingControlMethods task;

    public TaskSolvingControl(TaskSolvingControlMethods task) {
        this.task = task;
    }

    public void compile() {
        task.compile();
    }

    public void run() {
        task.run();
    }

}
