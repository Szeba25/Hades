package hu.szeba.hades.main.view.task;

import hu.szeba.hades.main.controller.task.TaskSelectorController;
import hu.szeba.hades.main.model.course.CourseDatabase;
import hu.szeba.hades.main.model.task.data.MissingResultFileException;
import hu.szeba.hades.main.model.task.languages.InvalidLanguageException;
import hu.szeba.hades.main.util.GridBagSetter;
import hu.szeba.hades.main.view.JButtonGuarded;
import hu.szeba.hades.main.view.ViewableFrame;
import hu.szeba.hades.main.view.elements.AbstractState;
import hu.szeba.hades.main.view.elements.MappedElement;
import hu.szeba.hades.main.view.elements.StatefulElement;
import hu.szeba.hades.main.view.elements.TaskElement;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class TaskSelectorView extends JFrame implements ViewableFrame {

    private TaskSelectorController controller;
    private ViewableFrame parentView;

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
    private JList<StatefulElement> taskCollectionList;
    private JList<TaskElement> taskList;
    private JButton filtersButton;

    /* RIGHT PART */

    private JPanel rightPanel;

    private JButtonGuarded startButton;
    private JButtonGuarded continueButton;

    private JEditorPane descriptionArea;
    private HTMLDocument defaultDocument;

    private JTextField taskCollectionStatus;
    private JTextField taskCollectionPercentNeeded;
    private JTextField taskCollectionProgress;
    private JTextField taskCollectionTaskCount;
    private JList<String> taskCollectionPrerequisites;

    private JTextField taskStatus;
    private JTextField taskDifficulty;
    private JTextField taskLength;
    private JList<String> taskPrerequisites;

    public TaskSelectorView(ViewableFrame parentView, CourseDatabase courseDatabase)
            throws IOException, SAXException, ParserConfigurationException {

        // Register parent view
        this.parentView = parentView;

        // JFrame init
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setResizable(true);
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(900, 680));
        this.setTitle("Please select a task");

        // Initialize components, and setup events
        initializeComponents();
        setupEvents();

        // Create the controller
        controller = new TaskSelectorController(courseDatabase);

        controller.setCourseListContents(courseList);
        controller.updateCourse(modeList, (MappedElement) courseList.getSelectedItem());
        controller.updateMode(taskCollectionList, (MappedElement) modeList.getSelectedItem());
        controller.updateTaskCollection(taskList, taskCollectionList.getSelectedValue());
        updateTaskCollectionInfo(taskCollectionList.getSelectedValue());

        setupListEvents();

        // Put everything together and pack
        this.getContentPane().add(topPanel, BorderLayout.NORTH);
        this.getContentPane().add(leftPanel, BorderLayout.WEST);
        this.getContentPane().add(rightPanel, BorderLayout.CENTER);
        this.pack();
    }

    private void initializeComponents() {
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
                new Insets(5, 0, 0, 0));

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

        HTMLEditorKit htmlEditorKit = (HTMLEditorKit) descriptionArea.getEditorKit();
        defaultDocument = (HTMLDocument) htmlEditorKit.createDefaultDocument();
        try {
            htmlEditorKit.insertHTML(defaultDocument, 0, "<h3>Please select a task!</h3>", 0, 0, null);
        } catch (BadLocationException | IOException e) {
            e.printStackTrace();
        }

        descriptionArea.setDocument(defaultDocument);

        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        descriptionScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JLabel taskCollectionDetailsLabel = new JLabel("Task collection details:");

        JPanel taskCollectionDetails = new JPanel();
        taskCollectionDetails.setLayout(new GridBagLayout());
        taskCollectionDetails.setBorder(BorderFactory.createEtchedBorder());

        taskCollectionStatus = new JTextField();
        taskCollectionStatus.setEditable(false);

        taskCollectionPercentNeeded = new JTextField();
        taskCollectionPercentNeeded.setEditable(false);

        taskCollectionProgress = new JTextField();
        taskCollectionProgress.setEditable(false);

        taskCollectionTaskCount = new JTextField();
        taskCollectionTaskCount.setEditable(false);

        taskCollectionPrerequisites = new JList<>();
        taskCollectionPrerequisites.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskCollectionPrerequisites.setModel(new DefaultListModel<>());
        JScrollPane taskCollectionPrerequisitesScroll = new JScrollPane(taskCollectionPrerequisites);
        taskCollectionPrerequisitesScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        taskCollectionPrerequisitesScroll.setPreferredSize(new Dimension(250, 80));

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

        gs.add(taskCollectionStatus, 1, 0, GridBagConstraints.HORIZONTAL,
                1, 1, 1.0, 0,
                new Insets(5, 0, 5, 5));

        gs.add(taskCollectionPercentNeeded, 1, 1, GridBagConstraints.HORIZONTAL,
                1, 1, 1.0, 0,
                new Insets(0, 0, 5, 5));

        gs.add(taskCollectionProgress, 1, 2, GridBagConstraints.HORIZONTAL,
                1, 1, 1.0, 0,
                new Insets(0, 0, 5, 5));

        gs.add(taskCollectionTaskCount, 1, 3, GridBagConstraints.HORIZONTAL,
                1, 1, 1.0, 0,
                new Insets(0, 0, 5, 5));

        gs.add(new JLabel("Task collection prerequisites:"), 0, 4, GridBagConstraints.HORIZONTAL,
                2, 1, 0, 0,
                new Insets(0, 0, 5, 0));

        gs.add(taskCollectionPrerequisitesScroll, 0, 5, GridBagConstraints.BOTH,
                2, 1, 1.0, 0,
                new Insets(0, 0, 0, 0));

        JLabel taskDetailsLabel = new JLabel("Task details:");

        JPanel taskDetails = new JPanel();
        taskDetails.setLayout(new GridBagLayout());
        taskDetails.setPreferredSize(new Dimension(250, 50));
        taskDetails.setBorder(BorderFactory.createEtchedBorder());

        taskStatus = new JTextField();
        taskStatus.setEditable(false);

        taskDifficulty = new JTextField();
        taskDifficulty.setEditable(false);

        taskLength = new JTextField();
        taskLength.setEditable(false);

        taskPrerequisites = new JList<>();
        taskPrerequisites.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskPrerequisites.setModel(new DefaultListModel<>());
        JScrollPane taskPrerequisitesScroll = new JScrollPane(taskPrerequisites);
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

        gs.add(taskStatus, 1, 0, GridBagConstraints.HORIZONTAL,
                1, 1, 1.0, 0,
                new Insets(5, 0, 5, 5));

        gs.add(taskDifficulty, 1, 1, GridBagConstraints.HORIZONTAL,
                1, 1, 1.0, 0,
                new Insets(0, 0, 5, 5));

        gs.add(taskLength, 1, 2, GridBagConstraints.HORIZONTAL,
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

        gs.add(descriptionScroll, 0, 1, GridBagConstraints.BOTH,
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
                    StatefulElement element = (StatefulElement) listModel.getElementAt(idx);
                    try {
                        controller.updateTaskCollection(taskList, element);
                        updateTaskCollectionInfo(element);
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
                    TaskElement value = (TaskElement) listModel.getElementAt(idx);
                    updateTaskSelection(value);
                }
            }
        });
    }

    private void setupEvents() {
        startButton.addActionListener((event) -> {
            // Protect from multiple actions spawned
            if (startButton.getActionGuard().isGuarded()) {
                return;
            }
            startButton.getActionGuard().guard();

            try {
                TaskElement selectedTask = taskList.getSelectedValue();
                if (selectedTask != null) {
                    boolean newTrigger = false;
                    // If progress exists, prompt if overwrite it!
                    if (selectedTask.getState() == AbstractState.COMPLETED || selectedTask.getState() == AbstractState.IN_PROGRESS) {
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
                        controller.loadNewTask(selectedTask, this);
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
                if (taskList.getSelectedValue() != null) {
                    controller.continueTask(taskList.getSelectedValue(), this);
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
                if (value instanceof StatefulElement) {
                    StatefulElement element = (StatefulElement)value;
                    setText(element.getTitle());
                    setBackground(Color.WHITE);
                    if (isSelected) {
                        setBackground(selectedColor);
                    }
                    switch(element.getState()) {
                        case COMPLETED:
                            setForeground(completedColor);
                            break;
                        case UNAVAILABLE:
                            setForeground(unavailableColor);
                            break;
                        default:
                            setForeground(availableColor);
                            break;
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
                if (value instanceof TaskElement) {
                    TaskElement element = (TaskElement)value;
                    setText(element.toString());
                    setBackground(Color.WHITE);
                    if (isSelected) {
                        setBackground(selectedColor);
                    }
                    switch(element.getState()) {
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

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                TaskSelectorView.this.dispose();
                parentView.showView();
            }
        });
    }

    @Override
    public void showView() {
        this.setLocationRelativeTo(null);
        basicShow();
    }

    @Override
    public void showViewMaximized() {
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        basicShow();
    }

    private void basicShow() {
        // JFrame stuff
        setVisible(true);
        requestFocus();

        // Refresh unavailable task collections, tasks, and dependency lists
        controller.generateCachedData();

        // Refresh buttons and info panels
        updateTaskSelection(taskList.getSelectedValue());
        updateTaskCollectionInfo(taskCollectionList.getSelectedValue());

        // Remove button guards
        startButton.getActionGuard().reset();
        continueButton.getActionGuard().reset();
    }

    @Override
    public void hideView() {
        this.setVisible(false);
    }

    private void updateTaskCollectionInfo(StatefulElement selectedTaskCollection) {
        controller.setTaskCollectionInfo(selectedTaskCollection, taskCollectionStatus,
                taskCollectionPercentNeeded, taskCollectionProgress,
                taskCollectionTaskCount, taskCollectionPrerequisites);
    }

    private void updateTaskSelection(TaskElement selectedTask) {
        if (selectedTask != null) {
            if (selectedTask.getState() == AbstractState.UNAVAILABLE) {
                startButton.setEnabled(false);
                continueButton.setEnabled(false);
            } else {
                startButton.setEnabled(true);
                if (selectedTask.getState() == AbstractState.COMPLETED || selectedTask.getState() == AbstractState.IN_PROGRESS) {
                    continueButton.setEnabled(true);
                } else {
                    continueButton.setEnabled(false);
                }
            }
            Document doc = selectedTask.getDescription().getShortDocument((HTMLEditorKit) descriptionArea.getEditorKit());
            descriptionArea.setDocument(doc);
            controller.setTaskInfo(selectedTask, taskStatus,
                    taskDifficulty, taskLength,
                    taskPrerequisites);
        } else {
            clearTaskSelection();
        }
    }

    private void clearTaskSelection() {
        descriptionArea.setDocument(defaultDocument);
        taskStatus.setText("");
        taskDifficulty.setText("");
        taskLength.setText("");
        ((DefaultListModel<String>) taskPrerequisites.getModel()).removeAllElements();
        startButton.setEnabled(false);
        continueButton.setEnabled(false);
    }

}
