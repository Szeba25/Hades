package hu.szeba.hades.form.task;

import hu.szeba.hades.model.task.TaskCollection;
import hu.szeba.hades.model.task.data.TaskDescription;
import hu.szeba.hades.util.SpringUtilities;
import hu.szeba.hades.view.MappedElement;
import hu.szeba.hades.view.task.TaskFilterData;

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
    private JComboBox<TaskFilterData.TaskStatus> statusList;
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
        this.setLocationRelativeTo(null);

        this.data = data;
        createData();
    }

    private void initializeComponents() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new SpringLayout());

        JLabel titleLabel = new JLabel("Task title:");
        JLabel difficultyLabel = new JLabel("Minimum difficulty:");
        JLabel statusLabel = new JLabel("Status:");

        titleField = new JTextField();
        difficultyList = new JComboBox<>();
        difficultyList.addItem("All");
        for (int i = 1; i <= 10; i++) {
            difficultyList.addItem(Integer.toString(i));
        }

        statusList = new JComboBox<>();
        statusList.addItem(TaskFilterData.TaskStatus.ALL);
        statusList.addItem(TaskFilterData.TaskStatus.AVAILABLE);
        statusList.addItem(TaskFilterData.TaskStatus.COMPLETED);
        statusList.addItem(TaskFilterData.TaskStatus.IN_PROGRESS);
        statusList.addItem(TaskFilterData.TaskStatus.UNAVAILABLE);

        topPanel.add(titleLabel);
        topPanel.add(titleField);
        topPanel.add(difficultyLabel);
        topPanel.add(difficultyList);
        topPanel.add(statusLabel);
        topPanel.add(statusList);
        SpringUtilities.makeCompactGrid(topPanel, 3, 2, 5, 5, 5, 5);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new FlowLayout());

        JLabel tagLabel = new JLabel("Tags:");

        tagPanel = new JPanel();
        tagPanel.setLayout(new GridLayout(0,  1));

        tags = new HashMap<>();

        JScrollPane tagScroll = new JScrollPane();
        tagScroll.setPreferredSize(new Dimension(300, 160));
        tagScroll.getVerticalScrollBar().setUnitIncrement(10);
        tagScroll.setViewportView(tagPanel);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        selectAll = new JButton("All");
        selectAll.setFocusPainted(false);
        selectAll.setAlignmentX(Component.CENTER_ALIGNMENT);
        clearAll = new JButton("Clear");
        clearAll.setFocusPainted(false);
        clearAll.setAlignmentX(Component.CENTER_ALIGNMENT);

        rightPanel.add(selectAll);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        rightPanel.add(clearAll);

        centerPanel.add(tagLabel);
        centerPanel.add(tagScroll);
        centerPanel.add(rightPanel);

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
        difficultyList.setSelectedIndex(data.getDifficultyFilter());
        for (int i = 0; i < statusList.getItemCount(); i++) {
            TaskFilterData.TaskStatus statusListItem = statusList.getItemAt(i);
            if (statusListItem == data.getStatusFilter()) {
                statusList.setSelectedIndex(i);
            }
        }
        for (String tag : data.getTagFilters().keySet()) {
            tags.get(tag).setSelected(data.getTagFilters().get(tag));
        }
    }

    private void createData() {
        String titleFilter = titleField.getText();
        String difficultyFilterStr = (String) difficultyList.getSelectedItem();
        if (difficultyFilterStr.equals("All")) {
            difficultyFilterStr = "0";
        }
        int difficultyFilter = Integer.parseInt(difficultyFilterStr);
        TaskFilterData.TaskStatus statusFilter = (TaskFilterData.TaskStatus) statusList.getSelectedItem();

        Map<String, Boolean> tagFilter = new HashMap<>();
        for (String tag : tags.keySet()) {
            tagFilter.put(tag, tags.get(tag).isSelected());
        }

        data.set(titleFilter, difficultyFilter, statusFilter, tagFilter);
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
        removeAllTags();
        for (MappedElement element : taskCollection.getPossibleTasks()) {
            TaskDescription description = taskCollection.getTaskDescription(element.getId());
            for (String tag : description.getTags()) {
                this.addTag(tag);
            }
        }
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
