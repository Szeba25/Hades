package hu.szeba.hades.view.task;

import hu.szeba.hades.util.SpringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

public class TaskFilterView extends JDialog {

    private JButton okButton;

    private JTextField titleField;
    private JComboBox<String> difficultyList;
    private JComboBox<String> statusList;
    private Map<String, JCheckBox> tags;

    private JButton selectAll;
    private JButton clearAll;

    private TaskFilterData data;

    public TaskFilterView() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(300, 200));
        this.setModal(true);
        this.setTitle("Set filters for the task list");

        initializeComponents();
        setupEvents();

        this.pack();
        this.setLocationRelativeTo(null);
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
        statusList.addItem("All");
        statusList.addItem("Available");
        statusList.addItem("In progress");
        statusList.addItem("Unavailable");

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

        JPanel tagPanel = new JPanel();
        tagPanel.setLayout(new GridLayout(0,  1));

        tags = new HashMap<>();

        for (int i = 0; i < 40; i++) {
            JCheckBox box = new JCheckBox("Tag " + i);
            box.setSelected(true);
            tags.put("Tag " + i, box);
            tagPanel.add(box);
        }

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

    public void setVisible(boolean value) {
        super.setVisible(value);

        this.requestFocus();
        this.loadData();
    }

    private void loadData() {
        titleField.setText(data.getTitleFilter());
        difficultyList.setSelectedIndex(data.getDifficultyFilter());
        for (int i = 0; i < statusList.getItemCount(); i++) {
            String statusListItem = statusList.getItemAt(i);
            if (statusListItem.equals(data.getStatusFilter())) {
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
        String statusFilter = (String) statusList.getSelectedItem();

        Map<String, Boolean> tagFilter = new HashMap<>();
        for (String tag : tags.keySet()) {
            tagFilter.put(tag, tags.get(tag).isSelected());
        }

        data = new TaskFilterData(titleFilter, difficultyFilter, statusFilter, tagFilter);
    }

    private void setupEvents() {
        okButton.addActionListener((e) -> {
            createData();
            this.setVisible(false);
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                TaskFilterView.this.setVisible(false);
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

}
