package hu.szeba.hades.wizard.form;

import hu.szeba.hades.util.GridBagSetter;
import hu.szeba.hades.wizard.components.GraphEditorPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ModeEditorForm extends JDialog {

    private JPanel topPanel;

    private JTextField titleField;
    private JCheckBox ignoreDependency;
    private JCheckBox ignoreStory;
    private JCheckBox ironMan;

    private GraphEditorPanel dependenciesPanel;

    private MultiSelectorForm taskCollectionSelectorForm;

    public ModeEditorForm() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setTitle("Wizard: Mode editor");
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(1000, 680));
        this.setResizable(true);
        this.setModal(true);

        taskCollectionSelectorForm = new MultiSelectorForm("Wizard: Select task collections");

        initializeComponents();
        setupEvents();

        this.getContentPane().add(topPanel, BorderLayout.NORTH);
        this.getContentPane().add(dependenciesPanel, BorderLayout.CENTER);
        this.pack();
    }

    private void initializeComponents() {
        topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());

        JLabel titleLabel = new JLabel("Title:");
        titleField = new JTextField();
        titleLabel.setLabelFor(titleField);

        JLabel ignoreDependencyLabel = new JLabel("Ignore dependency:");
        ignoreDependency = new JCheckBox();
        ignoreDependency.setSelected(false);
        ignoreDependencyLabel.setLabelFor(ignoreDependency);

        JLabel ignoreStoryLabel = new JLabel("Ignore story:");
        ignoreStory = new JCheckBox();
        ignoreStory.setSelected(false);
        ignoreStoryLabel.setLabelFor(ignoreStory);

        JLabel ironManLabel = new JLabel("Iron man:");
        ironMan = new JCheckBox();
        ironMan.setSelected(false);
        ironManLabel.setLabelFor(ironMan);

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

        gs.add(ignoreDependencyLabel,
                0,
                1,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                0,
                new Insets(5, 5, 0, 5));

        gs.add(ignoreDependency,
                1,
                1,
                GridBagConstraints.NONE,
                1,
                1,
                0,
                0,
                new Insets(5, 5, 0, 5));

        gs.add(ignoreStoryLabel,
                0,
                2,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                0,
                new Insets(5, 5, 0, 5));

        gs.add(ignoreStory,
                1,
                2,
                GridBagConstraints.NONE,
                1,
                1,
                0,
                0,
                new Insets(5, 5, 0, 5));

        gs.add(ironManLabel,
                0,
                3,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                0,
                new Insets(5, 5, 0, 5));

        gs.add(ironMan,
                1,
                3,
                GridBagConstraints.NONE,
                1,
                1,
                0,
                0,
                new Insets(5, 5, 0, 5));

        dependenciesPanel = new GraphEditorPanel("Task collections (in this mode):", 1024, 1536);

    }

    private void setupEvents() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                // TODO: Save here!
                // ...
                ModeEditorForm.this.dispose();
            }
        });

        dependenciesPanel.getAddNodeButton().addActionListener((e) -> {
            taskCollectionSelectorForm.setLocationRelativeTo(this);
            taskCollectionSelectorForm.setVisible(true);
        });
    }

}
