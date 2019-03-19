package hu.szeba.hades.wizard.form;

import hu.szeba.hades.util.GridBagSetter;
import hu.szeba.hades.wizard.components.ModifiableListPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TaskEditorForm extends JDialog {

    private JPanel leftPanel;

    private JTextField titleField;
    private JComboBox<String> difficultyBox;
    private JComboBox<String> lengthBox;
    private JTextArea tags;

    private JPanel rightPanel;

    private JPanel inputResultPanel;
    private JPanel solutionPanel;
    private JPanel sourcesPanel;
    private JPanel regexPanel;

    public TaskEditorForm() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setTitle("Wizard: Task editor");
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(800, 600));
        this.setResizable(false);
        this.setModal(true);

        initializeComponents();
        setupEvents();

        this.getContentPane().add(leftPanel, BorderLayout.WEST);
        this.getContentPane().add(rightPanel, BorderLayout.CENTER);
        this.pack();
    }

    private void initializeComponents() {
        initializeLeftPanel();
        initializeRightPanel();
    }

    private void setupEvents() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                // TODO: Save here!
                // ...
                TaskEditorForm.this.dispose();
            }
        });
    }

    private void initializeLeftPanel() {
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridBagLayout());

        leftPanel.setPreferredSize(new Dimension(200, 600));

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
        solutionPanel = new ModifiableListPanel("Solution source files:");
        sourcesPanel = new ModifiableListPanel("Starting source files:");
        regexPanel = new ModifiableListPanel("RegEx patterns:");

        GridBagSetter gs = new GridBagSetter();
        gs.setComponent(rightPanel);

        gs.add(inputResultPanel,
                0,
                0,
                GridBagConstraints.BOTH,
                1,
                1,
                0.5,
                0.5,
                new Insets(5, 5, 5, 5));

        gs.add(solutionPanel,
                1,
                0,
                GridBagConstraints.BOTH,
                1,
                1,
                0.5,
                0.5,
                new Insets(5, 0, 5, 5));

        gs.add(sourcesPanel,
                0,
                1,
                GridBagConstraints.BOTH,
                1,
                1,
                0.5,
                0.5,
                new Insets(0, 5, 5, 5));

        gs.add(regexPanel,
                1,
                1,
                GridBagConstraints.BOTH,
                1,
                1,
                0.5,
                0.5,
                new Insets(0, 0, 5, 5));
    }

}
