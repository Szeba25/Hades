package hu.szeba.hades.wizard;

import hu.szeba.hades.util.GridBagSetter;
import hu.szeba.hades.view.ViewableFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CourseEditorView extends JFrame implements ViewableFrame {

    private ViewableFrame parentView;

    private JPanel topPanel;
    private JTextField titleField;
    private JTextField languageField;

    private JPanel centerPanel;

    public CourseEditorView(ViewableFrame parentView) {
        this.parentView = parentView;

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setTitle("Wizard: Course editor");
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(1000, 600));
        this.setResizable(false);

        initializeComponents();
        setupEvents();

        this.getContentPane().add(topPanel, BorderLayout.NORTH);
        this.pack();
    }

    private void initializeComponents() {
        topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());

        JLabel titleLabel = new JLabel("Title:");
        titleField = new JTextField();
        titleLabel.setLabelFor(titleField);

        JLabel languageLabel = new JLabel("Programming language:");
        languageField = new JTextField();
        languageLabel.setLabelFor(languageField);

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
                1,
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

        gs.add(languageField,
                1,
                1,
                GridBagConstraints.HORIZONTAL,
                1,
                1,
                1,
                0,
                new Insets(5, 5, 0, 5));

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
        this.showView();
    }

    @Override
    public void hideView() {
        this.setVisible(false);
    }

}
