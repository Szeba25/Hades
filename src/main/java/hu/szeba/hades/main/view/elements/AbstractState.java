package hu.szeba.hades.main.view.elements;

import hu.szeba.hades.main.meta.Languages;

public enum AbstractState {

    ALL, UNAVAILABLE, COMPLETED, IN_PROGRESS, AVAILABLE;

    @Override
    public java.lang.String toString() {
        return Languages.translate((name().charAt(0) + name().substring(1).toLowerCase()).replace("_", " "));
    }

}
