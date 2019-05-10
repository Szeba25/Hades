package hu.szeba.hades.wizard.view;

import hu.szeba.hades.main.meta.Languages;
import hu.szeba.hades.main.view.components.ActionGuard;
import hu.szeba.hades.main.view.components.JButtonGuarded;
import hu.szeba.hades.main.view.components.ViewableFrame;
import hu.szeba.hades.main.view.elements.MappedElement;
import hu.szeba.hades.wizard.controller.CourseSelectorController;
import hu.szeba.hades.wizard.model.WizardCourseDatabase;
import hu.szeba.hades.wizard.view.components.ModifiableListPanel;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;

public class CourseSelectorView extends JFrame implements ViewableFrame {

    private CourseSelectorController controller;

    private JPanel mainPanel;

    private ModifiableListPanel courseListPanel;

    public CourseSelectorView(WizardCourseDatabase database) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle(Languages.translate("Wizard: Courses"));
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(480, 500));
        this.setResizable(false);

        initializeComponents();
        setupEvents();

        controller = new CourseSelectorController(database);

        controller.setCourseListContent(courseListPanel.getList());

        this.getContentPane().add(mainPanel, BorderLayout.CENTER);
        this.pack();
    }

    private void initializeComponents() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        courseListPanel = new ModifiableListPanel(Languages.translate("All courses:"), 300);

        mainPanel.add(courseListPanel, BorderLayout.CENTER);
    }

    private void setupEvents() {
        courseListPanel.getModifier().getAdd().addActionListener((event) -> {
            ActionGuard guard = courseListPanel.getModifier().getAdd().getActionGuard();
            if (guard.isGuarded()) {
                return;
            }

        });

        courseListPanel.getModifier().getEdit().addActionListener((event) -> {
            ActionGuard guard = courseListPanel.getModifier().getEdit().getActionGuard();
            if (guard.isGuarded()) {
                return;
            }

            try {
                MappedElement selectedCourse = courseListPanel.getList().getSelectedValue();
                if (selectedCourse != null) {
                    guard.guard();
                    new CourseEditorView(this, selectedCourse.getId()).showViewMaximized();
                    this.hideView();
                }
            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace();
            }
        });

        courseListPanel.getModifier().getDelete().addActionListener((event) -> {
            ActionGuard guard = courseListPanel.getModifier().getDelete().getActionGuard();
            if (guard.isGuarded()) {
                return;
            }

        });
    }

    @Override
    public void showView() {
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.requestFocus();

        // Reset guarded buttons
        courseListPanel.getModifier().getAdd().getActionGuard().reset();
        courseListPanel.getModifier().getEdit().getActionGuard().reset();
        courseListPanel.getModifier().getDelete().getActionGuard().reset();
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
