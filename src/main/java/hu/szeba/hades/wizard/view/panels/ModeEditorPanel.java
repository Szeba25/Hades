package hu.szeba.hades.wizard.view.panels;

import hu.szeba.hades.main.util.GridBagSetter;
import hu.szeba.hades.main.view.elements.MappedElement;
import hu.szeba.hades.wizard.form.MultiSelectorForm;
import hu.szeba.hades.wizard.model.WizardMode;
import hu.szeba.hades.wizard.view.components.GraphEditorPanel;
import hu.szeba.hades.wizard.view.elements.DescriptiveElement;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ModeEditorPanel extends JPanel {

    private DescriptiveElement currentElementRef;
    private WizardMode currentMode;

    private JPanel topPanel;

    private JTextField titleField;
    private JCheckBox ignoreDependency;
    private JCheckBox ignoreStory;
    private JCheckBox ironMan;

    private GraphEditorPanel dependenciesPanel;

    private MultiSelectorForm taskCollectionSelectorForm;

    public ModeEditorPanel() {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEtchedBorder());

        currentElementRef = null;
        currentMode = null;

        taskCollectionSelectorForm = new MultiSelectorForm("Wizard: Select task collections");

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

        gs.add(new JSeparator(JSeparator.HORIZONTAL),
                0,
                4,
                GridBagConstraints.HORIZONTAL,
                3,
                1,
                0,
                0,
                new Insets(5, 0, 5, 0));

        dependenciesPanel = new GraphEditorPanel("Task collections (in this mode):", 1536, 1536);
    }

    private void setupEvents() {
        dependenciesPanel.getAddNodeButton().addActionListener((e) -> {
            taskCollectionSelectorForm.setLocationRelativeTo(null);
            taskCollectionSelectorForm.setVisible(true);
        });
    }

    public void setCurrentMode(WizardMode newMode, DescriptiveElement currentElementRef, Map<String, String> idToTitleMap) {
        // Save old mode
        if (this.currentMode != null) {
            this.currentElementRef.setTitle(titleField.getText());
            this.currentMode.setTitle(titleField.getText());
            this.currentMode.setIgnoreDependency(ignoreDependency.isSelected());
            this.currentMode.setIgnoreStory(ignoreStory.isSelected());
            this.currentMode.setIronMan(ironMan.isSelected());
            // We work directly on graph data, no need to set it back!
        } else {
            setVisible(true);
        }

        // Load new mode
        titleField.setText(newMode.getTitle());
        ignoreDependency.setSelected(newMode.isIgnoreDependency());
        ignoreStory.setSelected(newMode.isIgnoreStory());
        ironMan.setSelected(newMode.isIronMan());
        dependenciesPanel.setGraphData(newMode.getGraph(), idToTitleMap);

        // Update current mode
        this.currentMode = newMode;
        this.currentElementRef = currentElementRef;
    }

    public JList<MappedElement> getTaskCollectionSelectorList() {
        return taskCollectionSelectorForm.getList();
    }

}
