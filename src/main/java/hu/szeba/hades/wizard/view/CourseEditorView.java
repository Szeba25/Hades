package hu.szeba.hades.wizard.view;

import hu.szeba.hades.main.util.GridBagSetter;
import hu.szeba.hades.main.view.components.ViewableFrame;
import hu.szeba.hades.wizard.controller.CourseEditorController;
import hu.szeba.hades.wizard.model.WizardCourse;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class CourseEditorView extends JFrame implements ViewableFrame {

    private ViewableFrame parentView;

    private CourseEditorController controller;

    private JPanel topPanel;
    private JTextField titleField;
    private JComboBox<String> languageBox;

    private JPanel centerPanel;

    public CourseEditorView(ViewableFrame parentView, String courseId)
            throws ParserConfigurationException, SAXException, IOException {

        this.parentView = parentView;

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setTitle("Wizard: Course editor");
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(1000, 680));
        this.setResizable(true);

        initializeComponents();
        setupEvents();

        WizardCourse course = new WizardCourse(courseId);
        controller = new CourseEditorController(course);

        titleField.setText(course.getCourseTitle());
        languageBox.getEditor().setItem(course.getLanguage());

        this.getContentPane().add(topPanel, BorderLayout.NORTH);
        this.getContentPane().add(centerPanel, BorderLayout.CENTER);
        this.pack();
    }

    private void initializeComponents() {
        topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());

        JLabel titleLabel = new JLabel("Title:");
        titleField = new JTextField();
        titleLabel.setLabelFor(titleField);

        JLabel languageLabel = new JLabel("Programming language:");
        languageBox = new JComboBox<>();
        languageBox.setEditable(true);
        languageLabel.setLabelFor(languageBox);

        GridBagSetter gs = new GridBagSetter();
        gs.setComponent(topPanel);

        gs.add(titleLabel,
                0,
                0,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                0,
                new Insets(5, 5, 0, 5));

        gs.add(titleField,
                1,
                0,
                GridBagConstraints.HORIZONTAL,
                2,
                1,
                1,
                0,
                new Insets(5, 5, 0, 5));

        gs.add(languageLabel,
                0,
                1,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                0,
                new Insets(5, 5, 0, 5));

        gs.add(languageBox,
                1,
                1,
                GridBagConstraints.HORIZONTAL,
                2,
                1,
                1,
                0,
                new Insets(5, 5, 0, 5));

        centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());

        gs.setComponent(centerPanel);
    }

    private void setupEvents() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                CourseEditorView.this.dispose();
                parentView.showView();
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
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
        this.requestFocus();
    }

    @Override
    public void hideView() {
        this.setVisible(false);
    }

}
