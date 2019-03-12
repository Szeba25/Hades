package hu.szeba.hades.view;

public class ActionGuard {

    private boolean available;

    public ActionGuard() {
        reset();
    }

    public void reset() {
        available = true;
    }

    public void guard() {
        available = false;
    }

    public boolean isAvailable() {
        return available;
    }

}
