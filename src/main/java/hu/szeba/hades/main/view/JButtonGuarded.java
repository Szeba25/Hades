package hu.szeba.hades.main.view;

import javax.swing.*;

public class JButtonGuarded extends JButton {

    private ActionGuard actionGuard;

    public JButtonGuarded(String text) {
        super(text);
        actionGuard = new ActionGuard();
    }

    public ActionGuard getActionGuard() {
        return actionGuard;
    }

}
