package hu.szeba.hades.wizard.view;

import hu.szeba.hades.main.util.GridBagSetter;
import hu.szeba.hades.main.view.components.ViewableFrame;
import hu.szeba.hades.wizard.components.ModifiableListPanel;
import hu.szeba.hades.wizard.form.ModeEditorForm;
import hu.szeba.hades.wizard.form.TaskCollectionEditorForm;
import hu.szeba.hades.wizard.form.TaskEditorForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CourseEditorView extends JFrame implements ViewableFrame {

    private ViewableFrame parentView;

    private JPanel topPanel;
    private JTextField titleField;
    private JComboBox<String> languageBox;

    private JPanel centerPanel;

    private ModifiableListPanel modeListPanel;
    private ModifiableListPanel taskCollectionListPanel;
    private ModifiableListPanel taskListPanel;

    public CourseEditorView(ViewableFrame parentView) {
        this.parentView = parentView;

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setTitle("Wizard: Course editor");
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(1000, 650));
        this.setResizable(false);

        initializeComponents();
        setupEvents();

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

        modeListPanel = new ModifiableListPanel("All modes:");
        taskCollectionListPanel = new ModifiableListPanel("All task collections:");
        taskListPanel = new ModifiableListPanel("All tasks:");

        gs.setComponent(centerPanel);

        gs.add(modeListPanel,
                0,
                0,
                GridBagConstraints.BOTH,
                1,
                1,
                0.5,
                1,
                new Insets(5, 5, 5, 5));

        gs.add(taskCollectionListPanel,
                1,
                0,
                GridBagConstraints.BOTH,
                1,
                1,
                0.5,
                1,
                new Insets(5, 0, 5, 5));

        gs.add(taskListPanel,
                2,
                0,
                GridBagConstraints.BOTH,
                1,
                1,
                0.5,
                1,
                new Insets(5, 0, 5, 5));

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

        this.modeListPanel.getModifier().getAdd().addActionListener((e) -> {
            ModeEditorForm form = new ModeEditorForm();
            form.setLocationRelativeTo(this);
            form.setVisible(true);
        });

        this.taskCollectionListPanel.getModifier().getAdd().addActionListener((e) -> {
            TaskCollectionEditorForm form = new TaskCollectionEditorForm();
            form.setLocationRelativeTo(this);
            form.setVisible(true);
        });

        this.taskListPanel.getModifier().getAdd().addActionListener((e) -> {
            TaskEditorForm form = new TaskEditorForm();
            form.setLocationRelativeTo(this);
            form.setVisible(true);
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
