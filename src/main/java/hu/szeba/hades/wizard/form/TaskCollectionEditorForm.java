package hu.szeba.hades.wizard.form;

import hu.szeba.hades.main.util.GridBagSetter;
import hu.szeba.hades.wizard.view.components.GraphEditorPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TaskCollectionEditorForm extends JDialog {

    private JPanel topPanel;

    private JTextField titleField;
    private JSpinner thresholdSpinner;

    private GraphEditorPanel dependenciesPanel;

    private MultiSelectorForm taskSelectorForm;

    public TaskCollectionEditorForm() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setTitle("Wizard: Task collection editor");
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(1000, 680));
        this.setResizable(true);
        this.setModal(true);

        taskSelectorForm = new MultiSelectorForm("Wizard: Select tasks");

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

        JLabel thresholdLabel = new JLabel("Task threshold (%):");
        thresholdSpinner = new JSpinner();
        thresholdSpinner.setModel(new SpinnerNumberModel(75, 0, 100, 1));
        thresholdLabel.setLabelFor(thresholdSpinner);

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

        gs.add(thresholdLabel,
                0,
                1,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                0,
                new Insets(5, 5, 0, 5));

        gs.add(thresholdSpinner,
                1,
                1,
                GridBagConstraints.HORIZONTAL,
                2,
                1,
                1,
                0,
                new Insets(5, 5, 0, 5));

        dependenciesPanel = new GraphEditorPanel("Tasks (in this collection):", 1024, 1536);

    }

    private void setupEvents() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                // TODO: Save here!
                // ...
                TaskCollectionEditorForm.this.dispose();
            }
        });

        dependenciesPanel.getAddNodeButton().addActionListener((e) -> {
            taskSelectorForm.setLocationRelativeTo(this);
            taskSelectorForm.setVisible(true);
        });
    }

}
