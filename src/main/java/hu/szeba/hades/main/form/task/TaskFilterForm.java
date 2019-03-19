package hu.szeba.hades.main.form.task;

import hu.szeba.hades.main.model.course.TaskCollection;
import hu.szeba.hades.main.util.GridBagSetter;
import hu.szeba.hades.main.util.SpringUtilities;
import hu.szeba.hades.main.view.elements.AbstractState;
import hu.szeba.hades.main.view.elements.TaskElement;
import hu.szeba.hades.main.view.task.TaskFilterData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

public class TaskFilterForm extends JDialog {

    private TaskFilterData data;

    private JButton okButton;

    private JTextField titleField;
    private JComboBox<String> difficultyList;
    private JComboBox<String> lengthSpinner;
    private JComboBox<AbstractState> stateList;
    private JPanel tagPanel;
    private Map<String, JCheckBox> tags;

    private JButton selectAll;
    private JButton clearAll;

    public TaskFilterForm(TaskFilterData data) {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(300, 200));
        this.setModal(true);
        this.setTitle("Set filters for the task list");

        initializeComponents();
        setupEvents();

        SwingUtilities.getRootPane(this).setDefaultButton(okButton);
        this.pack();

        this.data = data;
        createData();
    }

    private void initializeComponents() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new SpringLayout());

        JLabel titleLabel = new JLabel("Task title:");
        JLabel difficultyLabel = new JLabel("Difficulty:");
        JLabel lengthLabel = new JLabel("Length:");
        JLabel statusLabel = new JLabel("Status:");

        titleField = new JTextField();

        difficultyList = new JComboBox<>();
        difficultyList.addItem("All");
        difficultyList.addItem("Novice");
        difficultyList.addItem("Easy");
        difficultyList.addItem("Normal");
        difficultyList.addItem("Hard");
        difficultyList.addItem("Master");

        lengthSpinner = new JComboBox<>();
        lengthSpinner.addItem("All");
        lengthSpinner.addItem("Short");
        lengthSpinner.addItem("Medium");
        lengthSpinner.addItem("Long");

        stateList = new JComboBox<>();
        stateList.addItem(AbstractState.ALL);
        stateList.addItem(AbstractState.AVAILABLE);
        stateList.addItem(AbstractState.COMPLETED);
        stateList.addItem(AbstractState.IN_PROGRESS);
        stateList.addItem(AbstractState.UNAVAILABLE);

        topPanel.add(titleLabel);
        topPanel.add(titleField);
        topPanel.add(difficultyLabel);
        topPanel.add(difficultyList);
        topPanel.add(lengthLabel);
        topPanel.add(lengthSpinner);
        topPanel.add(statusLabel);
        topPanel.add(stateList);
        SpringUtilities.makeCompactGrid(topPanel, 4, 2, 5, 5, 5, 5);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());

        JLabel tagLabel = new JLabel("Tags:");

        tagPanel = new JPanel();
        tagPanel.setLayout(new GridLayout(0,  1));

        tags = new HashMap<>();

        JScrollPane tagScroll = new JScrollPane();
        tagScroll.setPreferredSize(new Dimension(300, 160));
        tagScroll.getVerticalScrollBar().setUnitIncrement(10);
        tagScroll.setViewportView(tagPanel);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

        selectAll = new JButton("All");
        selectAll.setFocusPainted(false);
        selectAll.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectAll.setMaximumSize(new Dimension(70, 20));
        clearAll = new JButton("Clear");
        clearAll.setFocusPainted(false);
        clearAll.setAlignmentX(Component.CENTER_ALIGNMENT);
        clearAll.setMaximumSize(new Dimension(70, 20));

        buttonsPanel.add(selectAll);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        buttonsPanel.add(clearAll);

        GridBagSetter gs = new GridBagSetter();

        gs.setComponent(centerPanel);

        gs.add(tagLabel,
                0,
                0,
                GridBagConstraints.BOTH,
                1,
                2,
                0,
                0,
                new Insets(0, 5, 0, 5));

        gs.add(tagScroll,
                1,
                0,
                GridBagConstraints.BOTH,
                1,

                2,
                1,
                1,
                new Insets(0, 0, 0, 5));

        gs.add(buttonsPanel,
                2,
                0,
                GridBagConstraints.HORIZONTAL,
                1,
                2,
                1,
                0,
                new Insets(0, 0, 5, 5));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());

        okButton = new JButton("Ok");
        okButton.setFocusPainted(false);
        okButton.setPreferredSize(new Dimension(120, 25));

        bottomPanel.add(okButton);

        this.getContentPane().add(topPanel, BorderLayout.NORTH);
        this.getContentPane().add(centerPanel, BorderLayout.CENTER);
        this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        titleField.setText(data.getTitleFilter());
        difficultyList.setSelectedItem(data.getDifficultyFilter());
        lengthSpinner.setSelectedItem(data.getLengthFilter());
        stateList.setSelectedItem(data.getStateFilter());
        for (String tag : data.getTagFilters().keySet()) {
            tags.get(tag).setSelected(data.getTagFilters().get(tag));
        }
    }

    private void createData() {
        String titleFilter = titleField.getText();
        String difficultyFilter = (String) difficultyList.getSelectedItem();
        String lengthFilter = (String) lengthSpinner.getSelectedItem();
        AbstractState stateFilter = (AbstractState) stateList.getSelectedItem();
        Map<String, Boolean> tagFilter = new HashMap<>();
        for (String tag : tags.keySet()) {
            tagFilter.put(tag, tags.get(tag).isSelected());
        }
        data.set(titleFilter, difficultyFilter, lengthFilter, stateFilter, tagFilter);
    }

    private void setupEvents() {
        okButton.addActionListener((e) -> {
            createData();
            this.setVisible(false);
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                loadData();
                TaskFilterForm.this.setVisible(false);
            }
        });

        selectAll.addActionListener((e) -> {
            for (JCheckBox box : tags.values()) {
                box.setSelected(true);
            }
        });

        clearAll.addActionListener((e) -> {
            for (JCheckBox box : tags.values()) {
                box.setSelected(false);
            }
        });
    }

    public void addAllTags(TaskCollection taskCollection) {
        // Reset logical data
        data.reset();

        // Reset form data
        titleField.setText("");
        difficultyList.setSelectedIndex(0);
        lengthSpinner.setSelectedIndex(0);
        stateList.setSelectedIndex(0);
        removeAllTags();

        // Add new tags
        for (TaskElement element : taskCollection.getPossibleTasks()) {
            for (String tag : element.getDescription().getTags()) {
                this.addTag(tag);
            }
        }

        // Set new logical data
        createData();
    }

    private void addTag(String name) {
        JCheckBox box = new JCheckBox(name);
        box.setSelected(true);
        if (!tags.containsKey(name)) {
            tagPanel.add(box);
            tags.put(name, box);
        }
    }

    private void removeAllTags() {
        tagPanel.removeAll();
        tags.clear();
    }

}
