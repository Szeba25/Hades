package hu.szeba.hades.view.campaign;

import hu.szeba.hades.controller.campaign.TaskSelectorController;
import hu.szeba.hades.controller.campaign.TaskSelectorControllerRegister;
import hu.szeba.hades.view.BaseView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TaskSelectorView extends BaseView implements TaskSelectorControllerRegister {

    private TaskSelectorController taskSelectorController;

    private JPanel leftPanel;
    private JPanel rightPanel;

    private JList taskList;
    private JScrollPane taskListScroller;

    private JTextArea descriptionArea;
    private JButton startButton;

    public TaskSelectorView() {
        super();
    }

    @Override
    public void initializeComponents() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(640, 480));
        this.setTitle("Hades: Please select a task");

        leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(new EmptyBorder(5, 0, 5, 5));

        taskList = new JList();
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

        this.getContentPane().add(leftPanel, BorderLayout.WEST);
        this.getContentPane().add(rightPanel, BorderLayout.CENTER);
        this.pack();
    }

    @Override
    public void setupEvents() {
        startButton.addActionListener(event -> {
            taskSelectorController.loadNewTask();
        });
    }

    @Override
    public void registerTaskSelectorController(TaskSelectorController taskSelectorController) {
        this.taskSelectorController = taskSelectorController;
    }

    public void setTaskListContents(String[] tasks) {
        taskList.setListData(tasks);
    }

    public String getSelectedTaskName() {
        if (taskList.getSelectedValue() != null) {
            return taskList.getSelectedValue().toString();
        } else {
            return null;
        }
    }

}
