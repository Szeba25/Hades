package hu.szeba.hades.wizard.view.panels;

import hu.szeba.hades.main.util.GridBagSetter;
import hu.szeba.hades.main.view.elements.MappedElement;
import hu.szeba.hades.wizard.form.MultiSelectorForm;
import hu.szeba.hades.wizard.model.WizardTaskCollection;
import hu.szeba.hades.wizard.view.components.GraphEditorPanel;
import hu.szeba.hades.wizard.view.elements.DescriptiveElement;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class TaskCollectionEditorPanel extends JPanel {

    private DescriptiveElement currentElementRef;
    private WizardTaskCollection currentTaskCollection;

    private JPanel topPanel;

    private JTextField titleField;
    private JTextField thresholdField;

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
        thresholdField = new JTextField();

        thresholdLabel.setLabelFor(thresholdField);

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

        gs.add(thresholdField,
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

        dependenciesPanel = new GraphEditorPanel("Tasks (in this collection):", 2048, 2048);
    }

    private void setupEvents() {
        dependenciesPanel.getAddNodeButton().addActionListener((e) -> {
            taskSelectorForm.setLocationRelativeTo(null);
            taskSelectorForm.setVisible(true);
            List<MappedElement> selections = taskSelectorForm.getSelectedElements();
            dependenciesPanel.addNodes(selections);
        });
    }

    public void setCurrentTaskCollection(WizardTaskCollection newTaskCollection, DescriptiveElement currentElementRef,
                                         Map<String, String> taskIdToTitle,
                                         List<MappedElement> possibleTasks,
                                         Map<String, String> taskCollectionIdToTitle,
                                         ModeEditorPanel modeEditor) {
        // Save old task collection
        if (this.currentTaskCollection != null) {

            // Set new names in the current list
            this.currentElementRef.setTitle(titleField.getText());
            this.currentTaskCollection.setTitle(titleField.getText());

            // Update references in ModeEditorPanel!!!
            taskCollectionIdToTitle.put(currentElementRef.getId(), titleField.getText());
            modeEditor.updateGraphTitles(taskCollectionIdToTitle);

            // If no number was entered, work with value 100
            try {
                int thresholdValue = Integer.parseInt(thresholdField.getText());
                this.currentTaskCollection.setCompletionThreshold(thresholdValue);
            } catch (NumberFormatException e) {
                System.out.println("Invalid value entered: " + thresholdField.getText());
                this.currentTaskCollection.setCompletionThreshold(100);
            }

            // We work directly on graph data, no need to set it back!
        } else {
            setVisible(true);
        }

        // Load new task collection
        titleField.setText(newTaskCollection.getTitle());
        thresholdField.setText(String.valueOf(newTaskCollection.getCompletionThreshold()));
        dependenciesPanel.setGraphData(newTaskCollection.getGraph(), taskIdToTitle);

        // Set up possible task selector list
        taskSelectorForm.setListContents(possibleTasks);

        // Update current task collection
        this.currentTaskCollection = newTaskCollection;
        this.currentElementRef = currentElementRef;
    }

    public void updateGraphTitles(Map<String, String> idToTitle) {
        if (currentTaskCollection != null) {
            dependenciesPanel.setGraphData(currentTaskCollection.getGraph(), idToTitle);
        }
    }

}
