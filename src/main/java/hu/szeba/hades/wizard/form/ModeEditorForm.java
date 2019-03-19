package hu.szeba.hades.wizard.form;

import hu.szeba.hades.util.GridBagSetter;
import hu.szeba.hades.wizard.elements.DescriptiveElement;
import hu.szeba.hades.wizard.elements.PlusMinusPanel;

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

    private JPanel centerPanel;

    private JList<DescriptiveElement> taskCollections;
    private PlusMinusPanel taskCollectionsAdder;
    private JTextArea graphEditor;

    private TaskCollectionSelectorForm taskCollectionSelectorForm;

    public ModeEditorForm() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setTitle("Wizard: Mode editor");
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(600, 600));
        this.setResizable(false);
        this.setModal(true);

        taskCollectionSelectorForm = new TaskCollectionSelectorForm();

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

        centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());

        taskCollections = new JList<>();
        taskCollections.setFixedCellWidth(200);
        taskCollections.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane taskCollectionsScroll = new JScrollPane(taskCollections);
        taskCollectionsScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        taskCollectionsAdder = new PlusMinusPanel();

        graphEditor = new JTextArea();
        JScrollPane graphEditorScroll = new JScrollPane(graphEditor);
        graphEditorScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        gs.setComponent(centerPanel);

        gs.add(new JLabel("Task collections (in this mode):"),
                0,
                0,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                0,
                new Insets(5, 5, 5, 5));

        gs.add(taskCollectionsScroll,
                0,
                1,
                GridBagConstraints.VERTICAL,
                1,
                1,
                0,
                1,
                new Insets(5, 5, 5, 5));

        gs.add(taskCollectionsAdder,
                1,
                1,
                GridBagConstraints.NONE,
                1,
                1,
                0,
                0,
                new Insets(5, 0, 5, 25));

        gs.add(new JLabel("Dependencies:"),
                2,
                0,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                0,
                new Insets(5, 5, 5, 5));

        gs.add(graphEditorScroll,
                2,
                1,
                GridBagConstraints.BOTH,
                1,
                1,
                1.0,
                1.0,
                new Insets(5, 5, 5, 5));

    }

    private void setupEvents() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                ModeEditorForm.this.dispose();
            }
        });

        taskCollectionsAdder.getPlus().addActionListener((e) -> {
            taskCollectionSelectorForm.setLocationRelativeTo(this);
            taskCollectionSelectorForm.setVisible(true);
        });
    }

}
