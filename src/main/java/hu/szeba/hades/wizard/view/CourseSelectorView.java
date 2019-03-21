package hu.szeba.hades.wizard.view;

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
        this.setTitle("Wizard: Courses");
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(380, 500));
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

        courseListPanel = new ModifiableListPanel("All courses:");

        mainPanel.add(courseListPanel, BorderLayout.CENTER);
    }

    private void setupEvents() {
        courseListPanel.getModifier().getAdd().addActionListener((event) -> {
            try {
                MappedElement selectedCourse = courseListPanel.getList().getSelectedValue();
                if (selectedCourse != null) {
                    new CourseEditorView(this, selectedCourse.getId()).showView();
                    this.hideView();
                }
            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace();
            }
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
