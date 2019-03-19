package hu.szeba.hades.main.view;

public class ActionGuard {

    private boolean guarded;

    public ActionGuard() {
        reset();
    }

    public void reset() {
        guarded = false;
    }

    public void guard() {
        guarded = true;
    }

    public boolean isGuarded() {
        return guarded;
    }

}