package hu.szeba.hades.wizard.form;

import hu.szeba.hades.util.GridBagSetter;
import hu.szeba.hades.wizard.elements.DescriptiveElement;
import hu.szeba.hades.wizard.components.PlusMinusPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TaskCollectionEditorForm extends JDialog {

    private JPanel topPanel;

    private JTextField titleField;
    private JSpinner thresholdSpinner;

    private JPanel centerPanel;

    private JList<DescriptiveElement> tasks;
    private PlusMinusPanel tasksAdder;
    private JTextArea graphEditor;

    private MultiSelectorForm taskSelectorForm;

    public TaskCollectionEditorForm() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setTitle("Wizard: Task collection editor");
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(600, 600));
        this.setResizable(false);
        this.setModal(true);

        taskSelectorForm = new MultiSelectorForm("Wizard: Select tasks");

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

        centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());

        tasks = new JList<>();
        tasks.setFixedCellWidth(200);
        tasks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tasksScroll = new JScrollPane(tasks);
        tasksScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        tasksAdder = new PlusMinusPanel();

        graphEditor = new JTextArea();
        JScrollPane graphEditorScroll = new JScrollPane(graphEditor);
        graphEditorScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        gs.setComponent(centerPanel);

        gs.add(new JLabel("Tasks (in this collection):"),
                0,
                0,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                0,
                new Insets(5, 5, 5, 5));

        gs.add(tasksScroll,
                0,
                1,
                GridBagConstraints.VERTICAL,
                1,
                1,
                0,
                1,
                new Insets(5, 5, 5, 5));

        gs.add(tasksAdder,
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
                // TODO: Save here!
                // ...
                TaskCollectionEditorForm.this.dispose();
            }
        });

        tasksAdder.getPlus().addActionListener((e) -> {
            taskSelectorForm.setLocationRelativeTo(this);
            taskSelectorForm.setVisible(true);
        });
    }

}
