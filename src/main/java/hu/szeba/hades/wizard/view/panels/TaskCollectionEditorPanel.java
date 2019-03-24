package hu.szeba.hades.wizard.view.panels;

import hu.szeba.hades.main.util.GridBagSetter;
import hu.szeba.hades.wizard.form.MultiSelectorForm;
import hu.szeba.hades.wizard.model.WizardTaskCollection;
import hu.szeba.hades.wizard.view.components.GraphEditorPanel;
import hu.szeba.hades.wizard.view.elements.DescriptiveElement;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class TaskCollectionEditorPanel extends JPanel {

    private DescriptiveElement currentElementRef;
    private WizardTaskCollection currentTaskCollection;

    private JPanel topPanel;

    private JTextField titleField;
    private JSpinner thresholdSpinner;

    private GraphEditorPanel dependenciesPanel;

    private MultiSelectorForm taskSelectorForm;

    public TaskCollectionEditorPanel() {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEtchedBorder());

        currentElementRef = null;
        currentTaskCollection = null;

        taskSelectorForm = new MultiSelectorForm("Wizard: Select tasks");

        initializeComponents();
        setupEvents();

        this.add(topPanel, BorderLayout.NORTH);
        this.add(dependenciesPanel, BorderLayout.CENTER);

        setVisible(false);
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
                1,
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
                1,
                1,
                1,
                0,
                new Insets(5, 5, 0, 5));

        gs.add(new JSeparator(JSeparator.HORIZONTAL),
                0,
                2,
                GridBagConstraints.HORIZONTAL,
                2,
                1,
                0,
                0,
                new Insets(5, 0, 5, 0));

        dependenciesPanel = new GraphEditorPanel("Tasks (in this collection):", 1536, 1536);
    }

    private void setupEvents() {
        dependenciesPanel.getAddNodeButton().addActionListener((e) -> {
            taskSelectorForm.setLocationRelativeTo(null);
            taskSelectorForm.setVisible(true);
        });
    }

    public void setCurrentTaskCollection(WizardTaskCollection newTaskCollection, DescriptiveElement currentElementRef,
                                         Map<String, String> idToTitleMap) {
        // Save old task collection
        if (this.currentTaskCollection != null) {
            this.currentElementRef.setTitle(titleField.getText());
            this.currentTaskCollection.setTitle(titleField.getText());
            this.currentTaskCollection.setCompletionThreshold((int) thresholdSpinner.getValue());
            // We work directly on graph data, no need to set it back!
        } else {
            setVisible(true);
        }

        // Load new task collection
        titleField.setText(newTaskCollection.getTitle());
        thresholdSpinner.setValue(newTaskCollection.getCompletionThreshold());
        dependenciesPanel.setGraphData(newTaskCollection.getGraph(), idToTitleMap);

        // Update current task collection
        this.currentTaskCollection = newTaskCollection;
        this.currentElementRef = currentElementRef;
    }

}
