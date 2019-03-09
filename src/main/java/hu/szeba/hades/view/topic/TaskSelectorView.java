package hu.szeba.hades.view.topic;

import hu.szeba.hades.controller.topic.TaskSelectorController;
import hu.szeba.hades.model.task.data.MissingResultFileException;
import hu.szeba.hades.model.topic.Topic;
import hu.szeba.hades.model.task.languages.InvalidLanguageException;
import hu.szeba.hades.view.BaseView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class TaskSelectorView extends BaseView {

    private TaskSelectorController controller;

    private JPanel leftPanel;
    private JPanel bottomPanel;
    private JPanel rightPanel;

    private JList<String> taskList;
    private JScrollPane taskListScroller;

    private Color selectedTaskBackground;
    private Color unavailableTaskForeground;
    private Color availableTaskForeground;

    private JEditorPane descriptionArea;
    private JButton startButton;
    private JButton continueButton;

    public TaskSelectorView(Topic topic) {
        super();
        controller = new TaskSelectorController(topic);
        controller.setTaskListContents(taskList);
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

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.setBorder(new EmptyBorder(5, 0, 5, 5));

        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(new EmptyBorder(5, 0, 5, 5));

        taskList = new JList<>();
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.setFixedCellWidth(200);

        taskList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                updateSelection(getSelectedTaskId());
            }
        });

        taskList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof String) {
                    String text = (String) value;
                    setText(text);
                    setBackground(Color.WHITE);
                    if (isSelected) {
                        setBackground(selectedTaskBackground);
                    }
                    if (controller.getUnavailableTaskIds().contains(controller.getTaskIdByTaskTitle(text))) {
                        setForeground(unavailableTaskForeground);
                    } else {
                        setForeground(availableTaskForeground);
                    }
                }
                return component;
            }
        });

        taskListScroller = new JScrollPane(taskList);
        taskListScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        taskListScroller.setBorder(BorderFactory.createEtchedBorder());

        selectedTaskBackground = new Color(160, 160, 255, 120);
        unavailableTaskForeground = Color.GRAY;
        availableTaskForeground = Color.BLACK;

        descriptionArea = new JEditorPane();
        descriptionArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionArea.setContentType("text/html");
        descriptionArea.setEditable(false);
        descriptionArea.setBorder(BorderFactory.createEtchedBorder());

        startButton = new JButton("Start");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setFocusPainted(false);
        startButton.setMaximumSize(new Dimension(120, 30));
        startButton.setEnabled(false);

        continueButton = new JButton("Continue");
        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueButton.setFocusPainted(false);
        continueButton.setMaximumSize(new Dimension(120, 30));
        continueButton.setEnabled(false);

        leftPanel.add(taskListScroller);

        bottomPanel.add(startButton);
        bottomPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        bottomPanel.add(continueButton);

        rightPanel.add(descriptionArea);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        rightPanel.add(bottomPanel);

        this.getContentPane().add(leftPanel, BorderLayout.WEST);
        this.getContentPane().add(rightPanel, BorderLayout.CENTER);
        this.pack();
    }

    @Override
    public void setupEvents() {
        startButton.addActionListener((event) -> {
            try {
                if (getSelectedTaskId() != null) {
                    boolean newTrigger = false;
                    // If progress exists, prompt if overwrite it!
                    if (controller.progressExists(getSelectedTaskId())) {
                        int option = JOptionPane.showConfirmDialog(new JFrame(),
                                "This will delete all previous progress for this task. Continue?",
                                "Start task from scratch...",
                                JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.YES_OPTION) {
                            newTrigger = true;
                        }
                    } else {
                        // There was no progress, we can overwrite it...
                        newTrigger = true;
                    }
                    // Finally, if we should create a new task, do it.
                    if (newTrigger) {
                        controller.loadNewTask(getSelectedTaskId(), this);
                    }
                }
            } catch (InvalidLanguageException | IOException | MissingResultFileException e) {
                e.printStackTrace();
            }
        });
        continueButton.addActionListener((event) -> {
            try {
                if (getSelectedTaskId() != null) {
                    controller.continueTask(getSelectedTaskId(), this);
                }
            } catch (InvalidLanguageException | IOException | MissingResultFileException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void showView() {
        super.showView();
        // Refresh unavailable tasks
        controller.generateUnavailableTaskIds();
        // Refresh buttons
        updateSelection(getSelectedTaskId());
    }

    private void updateSelection(String taskId) {
        if (taskId != null) {
            boolean available = controller.getUnavailableTaskIds().contains(taskId);
            if (available) {
                startButton.setEnabled(false);
                continueButton.setEnabled(false);
            } else {
                startButton.setEnabled(true);
                if (controller.progressExists(taskId)) {
                    continueButton.setEnabled(true);
                } else {
                    continueButton.setEnabled(false);
                }
            }
            controller.setTaskShortDescription(taskId, descriptionArea);
        } else {
            descriptionArea.setText("<h3>No task selected...</h3>");
        }
    }

    private String getSelectedTaskId() {
        return controller.getTaskIdByTaskTitle(taskList.getSelectedValue());
    }

}
