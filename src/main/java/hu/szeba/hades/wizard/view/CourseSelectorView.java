package hu.szeba.hades.wizard.view;

import hu.szeba.hades.main.meta.Languages;
import hu.szeba.hades.main.view.components.ActionGuard;
import hu.szeba.hades.main.view.components.DialogFactory;
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
            try {
                controller.addCourse();
                controller.setCourseListContent(courseListPanel.getList());
            } catch (IOException e) {
                e.printStackTrace();
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
            MappedElement selectedCourse = courseListPanel.getList().getSelectedValue();
            if (selectedCourse != null) {
                int option = DialogFactory.showCustomChoiceDialog(
                        Languages.translate("This will delete the course. Continue?"),
                        Languages.translate("Delete course..."),
                        Languages.translate("Yes"), Languages.translate("No"));

                if (option == JOptionPane.YES_OPTION) {
                    try {
                        controller.deleteCourse(selectedCourse);
                        controller.setCourseListContent(courseListPanel.getList());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void showView() {
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.requestFocus();

        // Reset guarded buttons
        courseListPanel.getModifier().getEdit().getActionGuard().reset();

        // Refresh list
        try {
            controller.refreshCourseDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        controller.setCourseListContent(courseListPanel.getList());
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
