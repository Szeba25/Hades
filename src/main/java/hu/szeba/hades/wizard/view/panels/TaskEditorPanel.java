package hu.szeba.hades.wizard.view.panels;

import hu.szeba.hades.main.meta.Languages;
import hu.szeba.hades.main.util.FileUtilities;
import hu.szeba.hades.main.util.GridBagSetter;
import hu.szeba.hades.main.view.components.DialogFactory;
import hu.szeba.hades.main.view.elements.MappedElement;
import hu.szeba.hades.wizard.form.CodeEditorForm;
import hu.szeba.hades.wizard.form.HTMLDescriptionsEditorForm;
import hu.szeba.hades.wizard.form.InputResultEditorForm;
import hu.szeba.hades.wizard.model.WizardTask;
import hu.szeba.hades.wizard.view.components.ModifiableListPanel;
import hu.szeba.hades.wizard.view.elements.DescriptiveElement;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class TaskEditorPanel extends JPanel {

    private DescriptiveElement currentElementRef;
    private WizardTask currentTask;

    private JPanel leftPanel;

    private JTextField titleField;
    private JComboBox<String> difficultyBox;
    private JComboBox<String> lengthBox;
    private JTextArea tags;

    private JPanel rightPanel;

    private ModifiableListPanel inputResultPanel;
    private JButton editSources;
    private JButton editSolutions;
    private JButton editDescriptions;

    private JPanel regexPanel;
    private JTextArea regexInclude;
    private JTextArea regexExclude;

    private InputResultEditorForm inputResultEditorForm;
    private CodeEditorForm sourceEditorForm;
    private CodeEditorForm solutionEditorForm;
    private HTMLDescriptionsEditorForm htmlDescriptionsEditorForm;

    public TaskEditorPanel() {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEtchedBorder());

        currentElementRef = null;
        currentTask = null;

        initializeComponents();
        setupEvents();

        this.add(leftPanel, BorderLayout.WEST);
        this.add(rightPanel, BorderLayout.CENTER);

        setVisible(false);
    }

    private void initializeComponents() {
        initializeLeftPanel();
        initializeRightPanel();

        inputResultEditorForm = new InputResultEditorForm();
        sourceEditorForm = new CodeEditorForm();
        solutionEditorForm = new CodeEditorForm();
        htmlDescriptionsEditorForm = new HTMLDescriptionsEditorForm();
    }

    private void initializeLeftPanel() {
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridBagLayout());

        leftPanel.setPreferredSize(new Dimension(250, 0));

        titleField = new JTextField();
        JLabel titleLabel = new JLabel(Languages.translate("Title:"));
        titleLabel.setLabelFor(titleField);

        // TODO: Translate these too!
        difficultyBox = new JComboBox<>();
        difficultyBox.addItem("Novice");
        difficultyBox.addItem("Easy");
        difficultyBox.addItem("Normal");
        difficultyBox.addItem("Hard");
        difficultyBox.addItem("Master");
        // TODO: Translate these too!

        JLabel difficultyLabel = new JLabel(Languages.translate("Difficulty:"));
        difficultyLabel.setLabelFor(difficultyBox);

        // TODO: Translate these too!
        lengthBox = new JComboBox<>();
        lengthBox.addItem("Short");
        lengthBox.addItem("Medium");
        lengthBox.addItem("Long");
        // TODO: Translate these too!

        JLabel lengthLabel = new JLabel(Languages.translate("Length:"));
        lengthLabel.setLabelFor(lengthBox);

        tags = new JTextArea();
        JScrollPane tagsScroll = new JScrollPane(tags);
        tagsScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        GridBagSetter gs = new GridBagSetter();
        gs.setComponent(leftPanel);

        gs.add(titleLabel,
                0,
                0,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                0,
                new Insets(5, 5, 5, 5));

        gs.add(titleField,
                1,
                0,
                GridBagConstraints.HORIZONTAL,
                1,
                1,
                1,
                0,
                new Insets(5, 5, 5, 0));

        gs.add(difficultyLabel,
                0,
                1,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                0,
                new Insets(0, 5, 5, 5));

        gs.add(difficultyBox,
                1,
                1,
                GridBagConstraints.HORIZONTAL,
                1,
                1,
                1,
                0,
                new Insets(0, 5, 5, 0));

        gs.add(lengthLabel,
                0,
                2,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                0,
                new Insets(0, 5, 5, 5));

        gs.add(lengthBox,
                1,
                2,
                GridBagConstraints.HORIZONTAL,
                1,
                1,
                1,
                0,
                new Insets(0, 5, 5, 0));

        gs.add(new JLabel(Languages.translate("Tags:")),
                0,
                3,
                GridBagConstraints.BOTH,
                2,
                1,
                0,
                0,
                new Insets(5, 5, 5, 5));

        gs.add(tagsScroll,
                0,
                4,
                GridBagConstraints.BOTH,
                2,
                1,
                1,
                1,
                new Insets(5, 5, 5, 5));
    }

    private void initializeRightPanel() {
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridBagLayout());

        inputResultPanel = new ModifiableListPanel(Languages.translate("Input result pairs:"), 200);
        inputResultPanel.setBorder(BorderFactory.createEtchedBorder());

        regexPanel = new JPanel();
        regexPanel.setLayout(new GridBagLayout());
        regexPanel.setBorder(BorderFactory.createEtchedBorder());

        regexInclude = new JTextArea();
        JScrollPane regexIncludeScroll = new JScrollPane(regexInclude);
        regexIncludeScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        regexInclude.setEnabled(false);
        regexInclude.setBackground(new Color(220, 220, 220));

        regexExclude = new JTextArea();
        JScrollPane regexExcludeScroll = new JScrollPane(regexExclude);
        regexExcludeScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        regexExclude.setEnabled(false);
        regexExclude.setBackground(new Color(220, 220, 220));

        GridBagSetter gs = new GridBagSetter();
        gs.setComponent(regexPanel);

        gs.add(new JLabel(Languages.translate("RegEx include:")),
                0,
                0,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                0,
                new Insets(5, 5, 5, 5));

        gs.add(regexIncludeScroll,
                0,
                1,
                GridBagConstraints.BOTH,
                1,
                1,
                1,
                1,
                new Insets(0, 5, 5, 5));

        gs.add(new JLabel(Languages.translate("RegEx exclude:")),
                0,
                2,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                0,
                new Insets(5, 5, 5, 5));

        gs.add(regexExcludeScroll,
                0,
                3,
                GridBagConstraints.BOTH,
                1,
                1,
                1,
                1,
                new Insets(0, 5, 5, 5));

        editSources = new JButton(Languages.translate("Edit sources"));
        editSources.setFocusPainted(false);
        editSolutions = new JButton(Languages.translate("Edit solutions"));
        editSolutions.setFocusPainted(false);
        editDescriptions = new JButton(Languages.translate("Edit descriptions"));
        editDescriptions.setFocusPainted(false);

        gs.setComponent(rightPanel);

        gs.add(inputResultPanel,
                0,
                0,
                GridBagConstraints.BOTH,
                3,
                1,
                1,
                0.3,
                new Insets(5, 5, 5, 5));

        gs.add(regexPanel,
                0,
                1,
                GridBagConstraints.BOTH,
                3,
                1,
                1,
                0.7,
                new Insets(0, 5, 5, 5));

        gs.add(editSources,
                0,
                2,
                GridBagConstraints.HORIZONTAL,
                1,
                1,
                0.5,
                0,
                new Insets(5, 5,5, 5));

        gs.add(editSolutions,
                1,
                2,
                GridBagConstraints.HORIZONTAL,
                1,
                1,
                0.5,
                0,
                new Insets(5,5, 5, 5));

        gs.add(editDescriptions,
                2,
                2,
                GridBagConstraints.HORIZONTAL,
                1,
                1,
                0.5,
                0,
                new Insets(5,5, 5, 5));
    }

    private void setupEvents() {
        inputResultPanel.getModifier().getAdd().addActionListener((event) -> {

            String name = DialogFactory.showCustomInputDialog(
                    "",
                    Languages.translate("New input/result pair name:"),
                    Languages.translate("Add new input/result pair"),
                    Languages.translate("Ok"),
                    Languages.translate("Cancel"));

            if (name != null) {
                if (name.length() > 0 && FileUtilities.validFileName(name) && !currentTask.isInputResultFileExists(name)) {
                    try {
                        // Add a new i/r pair
                        currentTask.addInputResultFile(name);
                        DefaultListModel<MappedElement> model = (DefaultListModel<MappedElement>) inputResultPanel.getList().getModel();
                        MappedElement newElement = new MappedElement(name, name);
                        model.addElement(newElement);
                        inputResultPanel.getList().setSelectedValue(newElement, true);

                        // Open the editor
                        inputResultEditorForm.setContents(name, "", "");
                        inputResultEditorForm.setLocationRelativeTo(null);
                        inputResultEditorForm.setVisible(true);

                        // Save back content
                        currentTask.setInputFileData(name, inputResultEditorForm.getInputFileData());
                        currentTask.setResultFileData(name, inputResultEditorForm.getResultFileData());

                        // Rename if possible!
                        String newName = inputResultEditorForm.getNewName();
                        if (!name.equals(newName)) {
                            // Only rename if changed!
                            if (newName.length() > 0 && FileUtilities.validFileName(newName) && !currentTask.isInputResultFileExists(newName)) {
                                currentTask.renameInputResultFile(name, newName);
                                newElement.setId(newName);
                                newElement.setTitle(newName);
                                inputResultPanel.getList().repaint();
                            } else {

                                DialogFactory.showCustomError(
                                        Languages.translate("This input/result pair name is invalid"),
                                        Languages.translate("Invalid pair name"));

                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    DialogFactory.showCustomError(
                            Languages.translate("This input/result pair name is invalid"),
                            Languages.translate("Invalid pair name"));
                }
            } else {
                DialogFactory.showCustomError(
                        Languages.translate("This input/result pair name is invalid"),
                        Languages.translate("Invalid pair name"));
            }
        });

        inputResultPanel.getModifier().getEdit().addActionListener((event) -> {
            MappedElement selected = inputResultPanel.getList().getSelectedValue();
            if (selected != null) {
                try {
                    // Set contents, and open the editor
                    inputResultEditorForm.setContents(selected.getId(), currentTask.getInputFileData(selected.getId()), currentTask.getResultFileData(selected.getId()));
                    inputResultEditorForm.setLocationRelativeTo(null);
                    inputResultEditorForm.setVisible(true);

                    // Save back contents
                    currentTask.setInputFileData(selected.getId(), inputResultEditorForm.getInputFileData());
                    currentTask.setResultFileData(selected.getId(), inputResultEditorForm.getResultFileData());

                    // Rename if possible!
                    String newName = inputResultEditorForm.getNewName();
                    if (!selected.getId().equals(newName)) {
                        // Only rename if changed!
                        if (newName.length() > 0 && FileUtilities.validFileName(newName) && !currentTask.isInputResultFileExists(newName)) {
                            currentTask.renameInputResultFile(selected.getId(), newName);
                            selected.setId(newName);
                            selected.setTitle(newName);
                            inputResultPanel.getList().repaint();
                        } else {

                            DialogFactory.showCustomError(
                                    Languages.translate("This input/result pair name is invalid"),
                                    Languages.translate("Invalid pair name"));

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        inputResultPanel.getModifier().getDelete().addActionListener((event) -> {
            MappedElement selected = inputResultPanel.getList().getSelectedValue();
            if (selected != null) {

                int result = DialogFactory.showCustomChoiceDialog(
                        Languages.translate("Delete this input/result pair?"),
                        Languages.translate("Delete input/result pair"),
                        Languages.translate("Delete"),
                        Languages.translate("Cancel"));

                if (result == JOptionPane.YES_OPTION) {
                    currentTask.removeInputResultFile(selected.getId());
                    DefaultListModel<MappedElement> model = (DefaultListModel<MappedElement>) inputResultPanel.getList().getModel();
                    model.removeElement(selected);
                }
            }
        });

        editSources.addActionListener((event) -> {
            sourceEditorForm.setFiles(currentTask.getSourceFiles(),
                    new File(currentTask.getTaskPath(), "sources"), currentTask.getReadonlySourcesData());

            sourceEditorForm.setLocationRelativeTo(null);
            sourceEditorForm.setVisible(true);

            currentTask.setReadonlySourcesData(sourceEditorForm.getReadonlySourcesData());
        });

        editSolutions.addActionListener((event) -> {
            solutionEditorForm.setFiles(currentTask.getSolutionFiles(),
                    new File(currentTask.getTaskPath(), "solutions"), null);

            solutionEditorForm.setLocationRelativeTo(null);
            solutionEditorForm.setVisible(true);
        });

        editDescriptions.addActionListener((event) -> {
            htmlDescriptionsEditorForm.setup(
                    currentTask.getShortStory(),
                    currentTask.getStory(),
                    currentTask.getShortInstructions(),
                    currentTask.getInstructions());

            htmlDescriptionsEditorForm.setLocationRelativeTo(null);
            htmlDescriptionsEditorForm.setVisible(true);

            currentTask.setShortStory(htmlDescriptionsEditorForm.getShortStory());
            currentTask.setStory(htmlDescriptionsEditorForm.getStory());
            currentTask.setShortInstructions(htmlDescriptionsEditorForm.getShortInstructions());
            currentTask.setInstructions(htmlDescriptionsEditorForm.getInstructions());
        });
    }

    public void hideAndDisable() {
        currentTask = null;
        currentElementRef = null;
        setVisible(false);
    }

    public void setCurrentTask(WizardTask newTask, DescriptiveElement currentElementRef,
                               Map<String, String> taskIdToTitle,
                               TaskCollectionEditorPanel taskCollectionEditor) {

        // Save old task
        if (this.currentTask != null) {

            // Set new names in the current list
            this.currentElementRef.setTitle(titleField.getText());
            this.currentTask.setTitle(titleField.getText());
            this.currentTask.setDifficulty((String)difficultyBox.getSelectedItem());
            this.currentTask.setLength((String) lengthBox.getSelectedItem());
            this.currentTask.setTags(tags.getText());
            this.currentTask.setRegExIncludeData(regexInclude.getText());
            this.currentTask.setRegExExcludeData(regexExclude.getText());

            // We work directly on input/result data, no need to set it back!
            // We work directly on sources and solutions data, no need to set it back!

            // Update references in task collection editor
            taskIdToTitle.put(currentElementRef.getId(), currentElementRef.getTitle());
            taskCollectionEditor.updateGraphTitles(taskIdToTitle);

        } else {
            setVisible(true);
        }

        // Load new task
        titleField.setText(newTask.getTitle());
        difficultyBox.setSelectedItem(newTask.getDifficulty());
        lengthBox.setSelectedItem(newTask.getLength());
        tags.setText(newTask.getTags());
        regexInclude.setText(newTask.getRegExIncludeData());
        regexExclude.setText(newTask.getRegExExcludeData());

        // Only update the outer list for input result pairs
        DefaultListModel<MappedElement> model = (DefaultListModel<MappedElement>) inputResultPanel.getList().getModel();
        model.removeAllElements();
        for (String name : newTask.getInputResultFileNames()) {
            model.addElement(new MappedElement(name, name));
        }

        // We get the sources and solutions directly!

        // Update current task
        this.currentTask = newTask;
        this.currentElementRef = currentElementRef;
    }

}
