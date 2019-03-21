package hu.szeba.hades.wizard.view.panels;

import hu.szeba.hades.main.util.GridBagSetter;
import hu.szeba.hades.wizard.view.components.ModifiableListPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TaskEditorPanel extends JPanel {

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

    public TaskEditorPanel() {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEtchedBorder());

        initializeComponents();
        setupEvents();

        this.add(leftPanel, BorderLayout.WEST);
        this.add(rightPanel, BorderLayout.CENTER);
    }

    private void initializeComponents() {
        initializeLeftPanel();
        initializeRightPanel();
    }

    private void setupEvents() {

    }

    private void initializeLeftPanel() {
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridBagLayout());

        leftPanel.setPreferredSize(new Dimension(300, 600));

        titleField = new JTextField();
        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setLabelFor(titleField);

        difficultyBox = new JComboBox<>();
        JLabel difficultyLabel = new JLabel("Difficulty:");
        difficultyLabel.setLabelFor(difficultyBox);

        lengthBox = new JComboBox<>();
        JLabel lengthLabel = new JLabel("Length:");
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

        gs.add(new JLabel("Tags:"),
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

        inputResultPanel = new ModifiableListPanel("Input result pairs:");

        regexPanel = new JPanel();
        regexPanel.setLayout(new GridBagLayout());
        regexPanel.setBorder(BorderFactory.createEtchedBorder());

        regexInclude = new JTextArea();
        JScrollPane regexIncludeScroll = new JScrollPane(regexInclude);
        regexIncludeScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        regexExclude = new JTextArea();
        JScrollPane regexExcludeScroll = new JScrollPane(regexExclude);
        regexExcludeScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        GridBagSetter gs = new GridBagSetter();
        gs.setComponent(regexPanel);

        gs.add(new JLabel("RegEx include:"),
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

        gs.add(new JLabel("RegEx exclude:"),
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

        editSources = new JButton("Edit sources");
        editSources.setFocusPainted(false);
        editSolutions = new JButton("Edit solutions");
        editSolutions.setFocusPainted(false);
        editDescriptions = new JButton("Edit descriptions");
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

}
