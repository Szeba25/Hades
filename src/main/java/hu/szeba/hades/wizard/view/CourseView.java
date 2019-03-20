package hu.szeba.hades.wizard.view;

import hu.szeba.hades.main.view.components.ViewableFrame;
import hu.szeba.hades.wizard.controller.CourseController;
import hu.szeba.hades.wizard.model.WizardCourseDatabase;
import hu.szeba.hades.wizard.view.components.ModifiableListPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class CourseView extends JFrame implements ViewableFrame {

    private CourseController controller;

    private JPanel mainPanel;

    private ModifiableListPanel courseListPanel;

    public CourseView(WizardCourseDatabase database) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Wizard: Courses");
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(380, 500));
        this.setResizable(false);

        initializeComponents();
        setupEvents();

        controller = new CourseController(database);

        this.getContentPane().add(mainPanel, BorderLayout.CENTER);
        this.pack();
    }

    private void initializeComponents() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        courseListPanel = new ModifiableListPanel("All courses:");

        mainPanel.add(courseListPanel, BorderLayout.CENTER);
    }

    private void setupEvents() {
        courseListPanel.getModifier().getAdd().addActionListener((e) -> {
            new CourseEditorView(this).showView();
            this.hideView();
        });
    }

    @Override
    public void showView() {
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.requestFocus();
    }

    @Override
    public void showViewMaximized() {
        this.showView();
    }

    @Override
    public void hideView() {
        this.setVisible(false);
    }

}
