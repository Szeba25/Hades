package hu.szeba.hades.view;

import javax.swing.*;

public abstract class BaseView extends JFrame {

    public BaseView() {
        super();
        initializeComponents();
        setupEvents();
    }

    public abstract void initializeComponents();
    public abstract void setupEvents();

    public void showView() {
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void showView(BaseView parentView) {
        this.setLocationRelativeTo(parentView);
        this.setVisible(true);
    }

    public void showViewMaximized() {
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
    }

    public void hideView() {
        this.setVisible(false);
    }

}
