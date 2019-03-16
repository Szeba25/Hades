package hu.szeba.hades.view.task;

import hu.szeba.hades.controller.task.TaskSelectorController;
import hu.szeba.hades.model.course.CourseDatabase;
import hu.szeba.hades.model.task.data.MissingResultFileException;
import hu.szeba.hades.model.task.languages.InvalidLanguageException;
import hu.szeba.hades.util.GridBagSetter;
import hu.szeba.hades.util.HTMLUtilities;
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

    private Color selectedColor;
    private Color inProgressColor;
    private Color completedColor;
    private Color unavailableColor;
    private Color availableColor;

    /* TOP PART */

    private JPanel topPanel;
    private JComboBox<MappedElement> courseList;
    private JComboBox<MappedElement> modeList;

    /* LEFT PART */

    private JPanel leftPanel;
    private JList<MappedElement> taskCollectionList;
    private JList<MappedElement> taskList;
    private JButton filtersButton;

    /* RIGHT PART */

    private JPanel rightPanel;

    private JButtonGuarded startButton;
    private JButtonGuarded continueButton;

    private JEditorPane descriptionArea;

    private JTextField infoFieldTaskCollectionStatus;
    private JTextField infoFieldTaskCollectionPercentNeeded;
    private JTextField infoFieldTaskCollectionProgress;
    private JTextField infoFieldTaskCollectionTaskCount;

    private JTextField infoFieldTaskStatus;
    private JTextField infoFieldTaskDifficulty;
    private JTextField infoFieldTaskLength;
    private JList<String> infoFieldTaskPrerequisites;

    public TaskSelectorView(CourseDatabase courseDatabase) throws IOException, SAXException, ParserConfigurationException {
        super();

        // Create the controller
        controller = new TaskSelectorController(courseDatabase);

        controller.setCourseListContents(courseList);
        controller.updateCourse(modeList, (MappedElement) courseList.getSelectedItem());
        controller.updateMode(taskCollectionList, (MappedElement) modeList.getSelectedItem());
        controller.updateTaskCollection(taskList, taskCollectionList.getSelectedValue());
        controller.setTaskCollectionInfo(taskCollectionList.getSelectedValue(), infoFieldTaskCollectionStatus,
                infoFieldTaskCollectionPercentNeeded, infoFieldTaskCollectionProgress,
                infoFieldTaskCollectionTaskCount);

        setupListEvents();

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
        this.setMinimumSize(new Dimension(900, 680));
        this.setTitle("Please select a task");

        selectedColor = new Color(160, 160, 255, 120);
        inProgressColor = new Color(50, 50, 210);
        completedColor = new Color(20, 140, 20);
        unavailableColor = Color.GRAY;
        availableColor = Color.BLACK;

        GridBagSetter gs = new GridBagSetter();

        /* TOP PART */

        topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        topPanel.setBorder(new EmptyBorder(5, 5, 0, 5));

        JLabel courseListLabel = new JLabel("Course:");
        courseList = new JComboBox<>();
        courseList.setPreferredSize(new Dimension(160, 20));
        courseListLabel.setLabelFor(courseList);

        JLabel modeListLabel = new JLabel("Mode:");
        modeList = new JComboBox<>();
        modeList.setPreferredSize(new Dimension(160, 20));
        modeListLabel.setLabelFor(modeList);

        JSeparator headerSeparator = new JSeparator(JSeparator.HORIZONTAL);

        gs.setComponent(topPanel);

        gs.add(courseListLabel, 0, 0, GridBagConstraints.BOTH,
                1, 1, 0, 0,
                new Insets(0, 0, 5, 5));

        gs.add(courseList, 1, 0, GridBagConstraints.BOTH,
                1, 1, 1.0, 0,
                new Insets(0, 0, 5, 0));

        gs.add(modeListLabel, 0, 1, GridBagConstraints.BOTH,
                1, 1, 0, 0,
                new Insets(0, 0, 5, 5));

        gs.add(modeList, 1, 1, GridBagConstraints.BOTH,
                1, 1, 1.0, 0,
                new Insets(0, 0, 5, 0));

        gs.add(headerSeparator, 0, 2, GridBagConstraints.BOTH,
                2, 1, 0, 0,
                new Insets(10, 0, 10, 0));

        /* LEFT PART */

        leftPanel = new JPanel();
        leftPanel.setLayout(new GridBagLayout());
        leftPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        taskCollectionList = new JList<>();
        taskCollectionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskCollectionList.setModel(new DefaultListModel<>());
        taskCollectionList.setFixedCellWidth(200);

        JLabel taskCollectionLabel = new JLabel("Task collections:");

        JScrollPane taskCollectionListScroller = new JScrollPane(taskCollectionList);
        taskCollectionListScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        taskCollectionListScroller.setBorder(BorderFactory.createEtchedBorder());

        taskList = new JList<>();
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.setModel(new DefaultListModel<>());
        taskList.setFixedCellWidth(200);

        JLabel taskLabel = new JLabel("Tasks:");

        JScrollPane taskListScroller = new JScrollPane(taskList);
        taskListScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        taskListScroller.setBorder(BorderFactory.createEtchedBorder());

        filtersButton = new JButton("Filters");
        filtersButton.setFocusPainted(false);

        gs.setComponent(leftPanel);

        gs.add(taskCollectionLabel, 0, 0, GridBagConstraints.BOTH,
                1, 1, 1.0, 0,
                new Insets(0, 0, 5, 0));

        gs.add(taskCollectionListScroller, 0, 1, GridBagConstraints.BOTH,
                1, 1, 1.0, 0,
                new Insets(0, 0, 0, 0));

        gs.add(taskLabel, 0, 2, GridBagConstraints.BOTH,
                1, 1, 1.0, 0,
                new Insets(5, 0, 5, 0));

        gs.add(taskListScroller, 0, 3, GridBagConstraints.BOTH,
                1, 1, 1.0, 1.0,
                new Insets(0, 0, 0, 0));

        gs.add(filtersButton, 0, 4, GridBagConstraints.BOTH,
                1, 1, 1.0, 0,
                new Insets(0, 0, 0, 0));

        /* RIGHT PART */

        rightPanel = new JPanel();
        rightPanel.setLayout(new GridBagLayout());
        rightPanel.setBorder(new EmptyBorder(5, 0, 5, 5));

        startButton = new JButtonGuarded("Start");
        startButton.setFocusPainted(false);
        startButton.setEnabled(false);

        continueButton = new JButtonGuarded("Continue");
        continueButton.setFocusPainted(false);
        continueButton.setEnabled(false);

        JLabel descriptionLabel = new JLabel("Task description:");

        descriptionArea = new JEditorPane();
        descriptionArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionArea.setContentType("text/html");
        descriptionArea.setEditable(false);
        descriptionArea.setBorder(BorderFactory.createEtchedBorder());

        JLabel taskCollectionDetailsLabel = new JLabel("Task collection details:");

        JPanel taskCollectionDetails = new JPanel();
        taskCollectionDetails.setLayout(new GridBagLayout());
        taskCollectionDetails.setBorder(BorderFactory.createEtchedBorder());

        infoFieldTaskCollectionStatus = new JTextField();
        infoFieldTaskCollectionStatus.setEditable(false);

        infoFieldTaskCollectionPercentNeeded = new JTextField();
        infoFieldTaskCollectionPercentNeeded.setEditable(false);

        infoFieldTaskCollectionProgress = new JTextField();
        infoFieldTaskCollectionProgress.setEditable(false);

        infoFieldTaskCollectionTaskCount = new JTextField();
        infoFieldTaskCollectionTaskCount.setEditable(false);

        gs.setComponent(taskCollectionDetails);

        gs.add(new JLabel("Status:"), 0, 0, GridBagConstraints.BOTH,
                1, 1, 0, 0,
                new Insets(5, 0, 5, 5));

        gs.add(new JLabel("Percent needed:"), 0, 1, GridBagConstraints.BOTH,
                1, 1, 0, 0,
                new Insets(0, 0, 5, 5));

        gs.add(new JLabel("Progress:"), 0, 2, GridBagConstraints.BOTH,
                1, 1, 0, 0,
                new Insets(0, 0, 5, 5));

        gs.add(new JLabel("Task count:"), 0, 3, GridBagConstraints.BOTH,
                1, 1, 0, 0,
                new Insets(0, 0, 5, 5));

        gs.add(infoFieldTaskCollectionStatus, 1, 0, GridBagConstraints.HORIZONTAL,
                1, 1, 1.0, 0,
                new Insets(5, 0, 5, 5));

        gs.add(infoFieldTaskCollectionPercentNeeded, 1, 1, GridBagConstraints.HORIZONTAL,
                1, 1, 1.0, 0,
                new Insets(0, 0, 5, 5));

        gs.add(infoFieldTaskCollectionProgress, 1, 2, GridBagConstraints.HORIZONTAL,
                1, 1, 1.0, 0,
                new Insets(0, 0, 5, 5));

        gs.add(infoFieldTaskCollectionTaskCount, 1, 3, GridBagConstraints.HORIZONTAL,
                1, 1, 1.0, 0,
                new Insets(0, 0, 5, 5));

        JLabel taskDetailsLabel = new JLabel("Task details:");

        JPanel taskDetails = new JPanel();
        taskDetails.setLayout(new GridBagLayout());
        taskDetails.setPreferredSize(new Dimension(200, 50));
        taskDetails.setBorder(BorderFactory.createEtchedBorder());

        infoFieldTaskStatus = new JTextField();
        infoFieldTaskStatus.setEditable(false);

        infoFieldTaskDifficulty = new JTextField();
        infoFieldTaskDifficulty.setEditable(false);

        infoFieldTaskLength = new JTextField();
        infoFieldTaskLength.setEditable(false);

        infoFieldTaskPrerequisites = new JList<>();
        infoFieldTaskPrerequisites.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        infoFieldTaskPrerequisites.setModel(new DefaultListModel<>());
        JScrollPane taskPrerequisitesScroll = new JScrollPane(infoFieldTaskPrerequisites);
        taskPrerequisitesScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        gs.setComponent(taskDetails);

        gs.add(new JLabel("Status:"), 0, 0, GridBagConstraints.BOTH,
                1, 1, 0, 0,
                new Insets(5, 0, 5, 5));

        gs.add(new JLabel("Difficulty:"), 0, 1, GridBagConstraints.BOTH,
                1, 1, 0, 0,
                new Insets(0, 0, 5, 5));

        gs.add(new JLabel("Length:"), 0, 2, GridBagConstraints.BOTH,
                1, 1, 0, 0,
                new Insets(0, 0, 5, 5));

        gs.add(infoFieldTaskStatus, 1, 0, GridBagConstraints.HORIZONTAL,
                1, 1, 1.0, 0,
                new Insets(5, 0, 5, 5));

        gs.add(infoFieldTaskDifficulty, 1, 1, GridBagConstraints.HORIZONTAL,
                1, 1, 1.0, 0,
                new Insets(0, 0, 5, 5));

        gs.add(infoFieldTaskLength, 1, 2, GridBagConstraints.HORIZONTAL,
                1, 1, 1.0, 0,
                new Insets(0, 0, 5, 5));

        gs.add(new JLabel("Task prerequisites:"), 0, 3, GridBagConstraints.BOTH,
                2, 1, 0, 0,
                new Insets(0, 0, 5, 0));

        gs.add(taskPrerequisitesScroll, 0, 4, GridBagConstraints.BOTH,
                2, 1, 1.0, 1.0,
                new Insets(0, 0, 0, 0));

        JSeparator buttonsSeparator = new JSeparator(JSeparator.HORIZONTAL);

        gs.setComponent(rightPanel);

        gs.add(descriptionLabel, 0, 0, GridBagConstraints.BOTH,
                1, 1, 0, 0,
                new Insets(0, 0, 5, 0));

        gs.add(descriptionArea, 0, 1, GridBagConstraints.BOTH,
                1, 6, 1.0, 1.0,
                new Insets(0, 0, 0, 5));

        gs.add(taskCollectionDetailsLabel, 1, 0, GridBagConstraints.BOTH,
                1, 1, 0, 0,
                new Insets(0, 0, 5, 0));

        gs.add(taskCollectionDetails, 1, 1, GridBagConstraints.BOTH,
                1, 1, 0, 0,
                new Insets(0, 0, 0, 0));

        gs.add(taskDetailsLabel, 1, 2, GridBagConstraints.BOTH,
                1, 1, 0, 0,
                new Insets(5, 0, 5, 0));

        gs.add(taskDetails, 1, 3, GridBagConstraints.BOTH,
                1, 1, 0, 0.7,
                new Insets(0, 0, 0, 0));

        gs.add(buttonsSeparator, 1, 4, GridBagConstraints.BOTH,
                1, 1, 0, 0,
                new Insets(10, 0, 10, 0));

        gs.add(startButton, 1, 5, GridBagConstraints.BOTH,
                1, 1, 0, 0,
                new Insets(0, 0, 5, 0));

        gs.add(continueButton, 1, 6, GridBagConstraints.BOTH,
                1, 1, 0, 0,
                new Insets(0, 0, 0, 0));
    }

    private void setupListEvents() {
        courseList.addActionListener((event) -> {
            Object selectedItem = courseList.getSelectedItem();
            if (selectedItem != null) {
                try {
                    controller.updateCourse(modeList, (MappedElement) selectedItem);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        modeList.addActionListener((event) -> {
            Object selectedItem = modeList.getSelectedItem();
            if (selectedItem != null) {
                try {
                    controller.updateMode(taskCollectionList, (MappedElement) selectedItem);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        taskCollectionList.getSelectionModel().addListSelectionListener((event) -> {
            ListSelectionModel listSelectionModel = (ListSelectionModel) event.getSource();
            ListModel listModel = taskCollectionList.getModel();
            if (!listSelectionModel.isSelectionEmpty()) {
                int idx = listSelectionModel.getMinSelectionIndex();
                if (listSelectionModel.isSelectedIndex(idx)) {
                    MappedElement value = (MappedElement) listModel.getElementAt(idx);
                    try {
                        controller.updateTaskCollection(taskList, value);
                        controller.setTaskCollectionInfo(value, infoFieldTaskCollectionStatus,
                                infoFieldTaskCollectionPercentNeeded, infoFieldTaskCollectionProgress,
                                infoFieldTaskCollectionTaskCount);
                        clearTaskSelection();
                    } catch (ParserConfigurationException | SAXException | IOException e) {
                        e.printStackTrace();
                    }
                }
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
    }

    @Override
    public void setupEvents() {
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
                    if (controller.isTaskStarted(getSelectedTask())) {
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

        taskCollectionList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof MappedElement) {
                    MappedElement element = (MappedElement)value;
                    setText(element.toString());
                    setBackground(Color.WHITE);
                    if (isSelected) {
                        setBackground(selectedColor);
                    }
                    if (controller.isTaskCollectionCompleted(element)) {
                        setForeground(completedColor);
                    } else if (controller.isTaskCollectionUnavailable(element)) {
                        setForeground(unavailableColor);
                    } else {
                        setForeground(availableColor);
                    }
                }
                return component;
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
                        setBackground(selectedColor);
                    }
                    switch(controller.getTaskEffectiveStatus(element)) {
                        case COMPLETED:
                            setForeground(completedColor);
                            break;
                        case UNAVAILABLE:
                            setForeground(unavailableColor);
                            break;
                        case IN_PROGRESS:
                            setForeground(inProgressColor);
                            break;
                        default:
                            setForeground(availableColor);
                            break;
                    }
                }
                return component;
            }
        });

        filtersButton.addActionListener((e) -> {
            controller.showTaskFilterView(taskList, this);
            clearTaskSelection();
        });
    }

    @Override
    public void showView() {
        super.showView();

        // Refresh unavailable task collections, tasks, and dependency lists
        controller.generateCachedData();

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
                if (controller.isTaskStarted(selectedTask)) {
                    continueButton.setEnabled(true);
                } else {
                    continueButton.setEnabled(false);
                }
            }
            controller.setTaskShortDescription(selectedTask, descriptionArea);
            controller.setTaskInfo(selectedTask, infoFieldTaskStatus,
                    infoFieldTaskDifficulty, infoFieldTaskLength,
                    infoFieldTaskPrerequisites);
        } else {
            clearTaskSelection();
        }
    }

    private void clearTaskSelection() {
        descriptionArea.setText(HTMLUtilities.getEmptyTaskDescription());
        infoFieldTaskStatus.setText("");
        infoFieldTaskDifficulty.setText("");
        infoFieldTaskLength.setText("");
        ((DefaultListModel<String>)infoFieldTaskPrerequisites.getModel()).removeAllElements();
        startButton.setEnabled(false);
        continueButton.setEnabled(false);
    }

    private MappedElement getSelectedTask() {
        return taskList.getSelectedValue();
    }

}
