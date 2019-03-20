package hu.szeba.hades.main.view.elements;

public enum AbstractState {

    ALL, UNAVAILABLE, COMPLETED, IN_PROGRESS, AVAILABLE;

    @Override
    public java.lang.String toString() {
        return (name().charAt(0) + name().substring(1).toLowerCase()).replace("_", " ");
    }

}
