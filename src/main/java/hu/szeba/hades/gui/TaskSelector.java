package hu.szeba.hades.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TaskSelector {

    private JFrame mainFrame;

    private JPanel leftPanel;
    private JPanel rightPanel;

    private JList taskList;
    private JScrollPane taskListScroller;

    private JTextArea descriptionArea;
    private JButton startButton;

    public TaskSelector() {
        mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(true);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setMinimumSize(new Dimension(640, 480));
        mainFrame.setTitle("Hades: Please select a task");

        leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(new EmptyBorder(5, 0, 5, 5));

        String[] tasks = {"Task 1", "Task 2", "Task 3"};
        taskList = new JList(tasks);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.setFixedCellWidth(200);

        taskListScroller = new JScrollPane(taskList);
        taskListScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        taskListScroller.setBorder(BorderFactory.createEtchedBorder());

        descriptionArea = new JTextArea();
        descriptionArea.setAlignmentX(Component.RIGHT_ALIGNMENT);
        descriptionArea.setEditable(false);
        descriptionArea.setBorder(BorderFactory.createEtchedBorder());

        startButton = new JButton("Start");
        startButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        startButton.setFocusPainted(false);
        startButton.setMaximumSize(new Dimension(120, 30));

        leftPanel.add(taskListScroller);

        rightPanel.add(descriptionArea);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        rightPanel.add(startButton);

        mainFrame.getContentPane().add(leftPanel, BorderLayout.WEST);
        mainFrame.getContentPane().add(rightPanel, BorderLayout.CENTER);
        mainFrame.pack();
    }

    public void show() {
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

}
