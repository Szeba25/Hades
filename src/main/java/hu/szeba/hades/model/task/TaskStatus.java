package hu.szeba.hades.model.task;

public enum TaskStatus {

    ALL, AVAILABLE, COMPLETED, IN_PROGRESS, UNAVAILABLE;
    public String toString() {
        return (name().charAt(0) + name().toLowerCase().substring(1)).replace("_", " ");
    }

}
