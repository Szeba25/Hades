package hu.szeba.hades.view.task;

import hu.szeba.hades.util.SpringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TaskFiltersView extends JDialog {

    private JButton okButton;

    public TaskFiltersView() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(450, 200));
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
        JLabel difficultyLabel = new JLabel("Difficulty:");
        JLabel statusLabel = new JLabel("Status:");
        JLabel tagLabel = new JLabel("Tags:");

        JTextField titleField = new JTextField();
        JComboBox<String> difficultyList = new JComboBox<>();
        JComboBox<String> statusList = new JComboBox<>();

        topPanel.add(titleLabel);
        topPanel.add(titleField);
        topPanel.add(difficultyLabel);
        topPanel.add(difficultyList);
        topPanel.add(statusLabel);
        topPanel.add(statusList);
        topPanel.add(tagLabel);
        topPanel.add(new JLabel(""));
        SpringUtilities.makeCompactGrid(topPanel, 4, 2, 5, 5, 5, 5);

        JPanel tagPanel = new JPanel();
        tagPanel.setLayout(new GridLayout(0,  3));

        for (int i = 0; i < 15; i++) {
            JCheckBox box = new JCheckBox("Tag " + i);
            box.setSelected(true);
            tagPanel.add(box);
        }

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());

        okButton = new JButton("Ok");
        okButton.setFocusPainted(false);
        okButton.setPreferredSize(new Dimension(120, 25));

        bottomPanel.add(okButton);

        this.getContentPane().add(topPanel, BorderLayout.NORTH);
        this.getContentPane().add(tagPanel, BorderLayout.CENTER);
        this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
    }

    private void setupEvents() {
        okButton.addActionListener((e) -> {
            this.setVisible(false);
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                TaskFiltersView.this.setVisible(false);
            }
        });
    }

}
