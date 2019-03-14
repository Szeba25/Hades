package hu.szeba.hades.view.task;

import hu.szeba.hades.controller.task.TaskSelectorController;
import hu.szeba.hades.model.course.CourseDatabase;
import hu.szeba.hades.model.task.data.MissingResultFileException;
import hu.szeba.hades.model.task.languages.InvalidLanguageException;
import hu.szeba.hades.util.SpringUtilities;
import hu.szeba.hades.view.BaseView;
import hu.szeba.hades.view.JButtonGuarded;
import hu.szeba.hades.view.MappedElement;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;

public class TaskSelectorView extends BaseView {

    private TaskSelectorController controller;

    private Color selectedTaskBackground;
    private Color completedTaskForeground;
    private Color unavailableTaskForeground;
    private Color availableTaskForeground;

    /* TOP PART */

    private JPanel topPanel;
    private JComboBox<MappedElement> courseList;
    private JComboBox<MappedElement> taskCollectionList;

    /* LEFT PART */

    private JPanel leftPanel;
    private JList<MappedElement> taskList;
    private JScrollPane taskListScroller;
    private JButtonGuarded filtersButton;

    /* RIGHT PART */

    private JPanel rightPanel;

    private JPanel rightPanelBottom;
    private JButtonGuarded startButton;
    private JButtonGuarded continueButton;

    private JEditorPane descriptionArea;

    public TaskSelectorView(CourseDatabase courseDatabase) {
        super();

        // Create the controller
        controller = new TaskSelectorController(courseDatabase);

        controller.setCourseListContents(courseList);
        controller.setTaskCollectionListContents(taskCollectionList);
        controller.setTaskListContents(taskList);

        // Put everything together and pack
        this.getContentPane().add(topPanel, BorderLayout.NORTH);
        this.getContentPane().add(leftPanel, BorderLayout.WEST);
        this.getContentPane().add(rightPanel, BorderLayout.CENTER);
        this.pack();
    }

    @Override
    public void initializeComponents() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(800, 600));
        this.setTitle("Hades: Please select a task");

        selectedTaskBackground = new Color(160, 160, 255, 120);
        completedTaskForeground = new Color(20, 140, 20);
        unavailableTaskForeground = Color.GRAY;
        availableTaskForeground = Color.BLACK;

        /* TOP PART */

        topPanel = new JPanel();
        topPanel.setLayout(new SpringLayout());
        topPanel.setBorder(new EmptyBorder(5, 5, 0, 5));

        JLabel courseListLabel = new JLabel("Course:");
        courseList = new JComboBox<>();
        courseList.setPreferredSize(new Dimension(160, 20));
        courseListLabel.setLabelFor(courseList);

        JLabel taskCollectionListLabel = new JLabel("Tasks:");
        taskCollectionList = new JComboBox<>();
        taskCollectionList.setPreferredSize(new Dimension(160, 20));
        taskCollectionListLabel.setLabelFor(taskCollectionList);

        topPanel.add(courseListLabel);
        topPanel.add(courseList);
        topPanel.add(taskCollectionListLabel);
        topPanel.add(taskCollectionList);
        SpringUtilities.makeCompactGrid(topPanel, 2, 2, 5, 5, 5, 5);

        /* LEFT PART */

        leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        taskList = new JList<>();
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.setFixedCellWidth(200);

        taskListScroller = new JScrollPane(taskList);
        taskListScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        taskListScroller.setBorder(BorderFactory.createEtchedBorder());

        filtersButton = new JButtonGuarded("Filters");
        filtersButton.setFocusPainted(false);

        leftPanel.add(taskListScroller, BorderLayout.CENTER);
        leftPanel.add(filtersButton, BorderLayout.SOUTH);

        /* RIGHT PART */

        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(new EmptyBorder(5, 0, 5, 5));

        rightPanelBottom = new JPanel();
        rightPanelBottom.setLayout(new BoxLayout(rightPanelBottom, BoxLayout.X_AXIS));
        rightPanelBottom.setBorder(new EmptyBorder(5, 0, 5, 5));

        startButton = new JButtonGuarded("Start");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setFocusPainted(false);
        startButton.setMaximumSize(new Dimension(120, 30));
        startButton.setEnabled(false);

        continueButton = new JButtonGuarded("Continue");
        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueButton.setFocusPainted(false);
        continueButton.setMaximumSize(new Dimension(120, 30));
        continueButton.setEnabled(false);

        rightPanelBottom.add(startButton);
        rightPanelBottom.add(Box.createRigidArea(new Dimension(10, 0)));
        rightPanelBottom.add(continueButton);

        descriptionArea = new JEditorPane();
        descriptionArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionArea.setContentType("text/html");
        descriptionArea.setEditable(false);
        descriptionArea.setBorder(BorderFactory.createEtchedBorder());

        rightPanel.add(descriptionArea);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        rightPanel.add(rightPanelBottom);
    }

    @Override
    public void setupEvents() {
        courseList.addActionListener((event) -> {
            Object selectedItem = courseList.getSelectedItem();
            if (selectedItem != null) {
                try {
                    controller.updateCourse(taskList, taskCollectionList, (MappedElement) selectedItem);
                    clearTaskSelection();
                } catch (IOException | SAXException | ParserConfigurationException e) {
                    e.printStackTrace();
                }
            }
        });

        taskCollectionList.addActionListener((event) -> {
            Object selectedItem = taskCollectionList.getSelectedItem();
            if (selectedItem != null) {
                try {
                    controller.updateTaskCollection(taskList, (MappedElement) selectedItem);
                    clearTaskSelection();
                } catch (ParserConfigurationException | SAXException | IOException e) {
                    e.printStackTrace();
                }
            }
        });

        startButton.addActionListener((event) -> {
            // Protect from multiple actions spawned
            if (startButton.getActionGuard().isGuarded()) {
                return;
            }
            startButton.getActionGuard().guard();

            try {
                if (getSelectedTask() != null) {
                    boolean newTrigger = false;
                    // If progress exists, prompt if overwrite it!
                    if (controller.isProgressExists(getSelectedTask())) {
                        int option = JOptionPane.showConfirmDialog(new JFrame(),
                                "This will delete all previous progress for this task. Continue?",
                                "Start task from scratch...",
                                JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.YES_OPTION) {
                            newTrigger = true;
                        } else {
                            startButton.getActionGuard().reset();
                        }
                    } else {
                        // There was no progress, we can overwrite it...
                        newTrigger = true;
                    }
                    // Finally, if we should create a new task, do it.
                    if (newTrigger) {
                        controller.loadNewTask(getSelectedTask(), this);
                    }
                }
            } catch (InvalidLanguageException | IOException | MissingResultFileException e) {
                e.printStackTrace();
            }
        });

        continueButton.addActionListener((event) -> {
            // Protect from multiple actions spawned
            if (continueButton.getActionGuard().isGuarded()) {
                return;
            }
            continueButton.getActionGuard().guard();

            try {
                if (getSelectedTask() != null) {
                    controller.continueTask(getSelectedTask(), this);
                }
            } catch (InvalidLanguageException | IOException | MissingResultFileException e) {
                e.printStackTrace();
            }
        });

        taskList.getSelectionModel().addListSelectionListener((event) ->  {
            ListSelectionModel listSelectionModel = (ListSelectionModel) event.getSource();
            ListModel listModel = taskList.getModel();
            if (!listSelectionModel.isSelectionEmpty()) {
                int idx = listSelectionModel.getMinSelectionIndex();
                if (listSelectionModel.isSelectedIndex(idx)) {
                    MappedElement value = (MappedElement) listModel.getElementAt(idx);
                    updateSelection(value);
                }
            }
        });

        taskList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof MappedElement) {
                    MappedElement element = (MappedElement)value;
                    setText(element.toString());
                    setBackground(Color.WHITE);
                    if (isSelected) {
                        setBackground(selectedTaskBackground);
                    }
                    if (controller.isTaskCompleted(element)) {
                        setForeground(completedTaskForeground);
                    } else if (controller.isTaskUnavailable(element)) {
                        setForeground(unavailableTaskForeground);
                    } else {
                        setForeground(availableTaskForeground);
                    }

                }
                return component;
            }
        });
    }

    @Override
    public void showView() {
        super.showView();

        // Refresh unavailable tasks
        controller.generateUnavailableTaskIds();

        // Refresh buttons
        updateSelection(getSelectedTask());

        // Remove button guards
        startButton.getActionGuard().reset();
        continueButton.getActionGuard().reset();
    }

    private void updateSelection(MappedElement selectedTask) {
        if (selectedTask != null) {
            boolean available = controller.isTaskUnavailable(selectedTask);
            if (available) {
                startButton.setEnabled(false);
                continueButton.setEnabled(false);
            } else {
                startButton.setEnabled(true);
                if (controller.isProgressExists(selectedTask)) {
                    continueButton.setEnabled(true);
                } else {
                    continueButton.setEnabled(false);
                }
            }
            controller.setTaskShortDescription(selectedTask, descriptionArea);
        } else {
            clearTaskSelection();
        }
    }

    private void clearTaskSelection() {
        descriptionArea.setText("<h3>No task selected...</h3>");
        startButton.setEnabled(false);
        continueButton.setEnabled(false);
    }

    private MappedElement getSelectedTask() {
        return taskList.getSelectedValue();
    }

}
