package hu.szeba.hades.view.task;

import hu.szeba.hades.controller.task.TaskSelectorController;
import hu.szeba.hades.model.course.CourseDatabase;
import hu.szeba.hades.model.task.data.MissingResultFileException;
import hu.szeba.hades.model.task.languages.InvalidLanguageException;
import hu.szeba.hades.util.HTMLUtilities;
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
    private JEditorPane infoArea;

    public TaskSelectorView(CourseDatabase courseDatabase) throws IOException, SAXException, ParserConfigurationException {
        super();

        // Create the controller
        controller = new TaskSelectorController(courseDatabase);

        controller.setCourseListContents(courseList);
        controller.updateCourse(modeList, (MappedElement) courseList.getSelectedItem());
        controller.updateMode(taskCollectionList, (MappedElement) modeList.getSelectedItem());
        controller.updateTaskCollection(taskList, taskCollectionList.getSelectedValue());
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
        this.setMinimumSize(new Dimension(800, 600));
        this.setTitle("Please select a task");

        selectedColor = new Color(160, 160, 255, 120);
        inProgressColor = new Color(50, 50, 210);
        completedColor = new Color(20, 140, 20);
        unavailableColor = Color.GRAY;
        availableColor = Color.BLACK;

        /* TOP PART */

        topPanel = new JPanel();
        topPanel.setLayout(new SpringLayout());
        topPanel.setBorder(new EmptyBorder(5, 5, 0, 5));

        JLabel courseListLabel = new JLabel("Course:");
        courseList = new JComboBox<>();
        courseList.setPreferredSize(new Dimension(160, 20));
        courseListLabel.setLabelFor(courseList);

        JLabel modeListLabel = new JLabel("Mode:");
        modeList = new JComboBox<>();
        modeList.setPreferredSize(new Dimension(160, 20));
        modeListLabel.setLabelFor(modeList);

        topPanel.add(courseListLabel);
        topPanel.add(courseList);
        topPanel.add(modeListLabel);
        topPanel.add(modeList);
        SpringUtilities.makeCompactGrid(topPanel, 2, 2, 0, 5, 5, 5);

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

        GridBagConstraints c1 = new GridBagConstraints();

        c1.gridx = 0;
        c1.gridy = 0;
        c1.fill = GridBagConstraints.BOTH;
        c1.gridwidth = 1;
        c1.gridheight = 1;
        c1.weightx = 1.0;
        c1.weighty = 0.0;
        c1.insets = new Insets(0, 0, 5, 0);
        leftPanel.add(taskCollectionLabel, c1);

        c1.gridx = 0;
        c1.gridy = 1;
        c1.fill = GridBagConstraints.BOTH;
        c1.gridwidth = 1;
        c1.gridheight = 1;
        c1.weightx = 1.0;
        c1.weighty = 0.0;
        c1.insets = new Insets(0, 0, 0, 0);
        leftPanel.add(taskCollectionListScroller, c1);

        c1.gridx = 0;
        c1.gridy = 2;
        c1.fill = GridBagConstraints.BOTH;
        c1.gridwidth = 1;
        c1.gridheight = 1;
        c1.weightx = 1.0;
        c1.weighty = 0.0;
        c1.insets = new Insets(5, 0, 5, 0);
        leftPanel.add(taskLabel, c1);

        c1.gridx = 0;
        c1.gridy = 3;
        c1.fill = GridBagConstraints.BOTH;
        c1.gridwidth = 1;
        c1.gridheight = 1;
        c1.weightx = 1.0;
        c1.weighty = 1.0;
        c1.insets = new Insets(0, 0, 0, 0);
        leftPanel.add(taskListScroller, c1);

        c1.gridx = 0;
        c1.gridy = 4;
        c1.fill = GridBagConstraints.BOTH;
        c1.gridwidth = 1;
        c1.gridheight = 1;
        c1.weightx = 1.0;
        c1.weighty = 0.0;
        c1.insets = new Insets(0, 0, 0, 0);
        leftPanel.add(filtersButton, c1);

        /* RIGHT PART */

        rightPanel = new JPanel();
        rightPanel.setLayout(new GridBagLayout());
        rightPanel.setBorder(new EmptyBorder(5, 0, 5, 5));

        infoArea = new JEditorPane();
        infoArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoArea.setContentType("text/html");
        infoArea.setEditable(false);
        infoArea.setBorder(BorderFactory.createEtchedBorder());

        startButton = new JButtonGuarded("Start");
        startButton.setFocusPainted(false);
        startButton.setMaximumSize(new Dimension(150, 30));
        startButton.setEnabled(false);

        continueButton = new JButtonGuarded("Continue");
        continueButton.setFocusPainted(false);
        continueButton.setMaximumSize(new Dimension(150, 30));
        continueButton.setEnabled(false);

        JLabel descriptionLabel = new JLabel("Task description:");

        descriptionArea = new JEditorPane();
        descriptionArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionArea.setContentType("text/html");
        descriptionArea.setEditable(false);
        descriptionArea.setBorder(BorderFactory.createEtchedBorder());

        GridBagConstraints c2 = new GridBagConstraints();
        c2.gridx = 0;
        c2.gridy = 0;
        c2.fill = GridBagConstraints.BOTH;
        c2.gridwidth = 2;
        c2.gridheight = 1;
        c2.weightx = 1.0;
        c2.weighty = 0.0;
        c2.insets = new Insets(0, 0, 5, 0);
        rightPanel.add(descriptionLabel, c2);

        c2.gridx = 0;
        c2.gridy = 1;
        c2.fill = GridBagConstraints.BOTH;
        c2.gridwidth = 2;
        c2.gridheight = 1;
        c2.weightx = 1.0;
        c2.weighty = 1.0;
        c2.insets = new Insets(0, 0, 5, 0);
        rightPanel.add(descriptionArea, c2);

        c2.gridx = 0;
        c2.gridy = 2;
        c2.fill = GridBagConstraints.BOTH;
        c2.gridwidth = 1;
        c2.gridheight = 2;
        c2.weightx = 1.0;
        c2.weighty = 0;
        c2.insets = new Insets(0, 0, 0, 5);
        rightPanel.add(infoArea, c2);

        c2.gridx = 1;
        c2.gridy = 2;
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.gridwidth = 1;
        c2.gridheight = 1;
        c2.weightx = 0;
        c2.weighty = 0;
        c2.insets = new Insets(5, 0, 0, 0);
        rightPanel.add(startButton, c2);

        c2.gridx = 1;
        c2.gridy = 3;
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.gridwidth = 1;
        c2.gridheight = 1;
        c2.weightx = 0;
        c2.weighty = 0;
        c2.insets = new Insets(0, 0, 0, 0);
        rightPanel.add(continueButton, c2);
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
                    if (controller.isTaskCompleted(element)) {
                        setForeground(completedColor);
                    } else if (controller.isTaskUnavailable(element)) {
                        setForeground(unavailableColor);
                    } else if (controller.isTaskStarted(element)) {
                        setForeground(inProgressColor);
                    } else {
                        setForeground(availableColor);
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

        // Refresh unavailable task collections and tasks
        controller.generateUnavailableIds();

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
            controller.setTaskInfo(selectedTask, infoArea);
        } else {
            clearTaskSelection();
        }
    }

    private void clearTaskSelection() {
        descriptionArea.setText(HTMLUtilities.getEmptyTaskDescription());
        infoArea.setText(HTMLUtilities.getEmptyTaskInfoHTML());
        startButton.setEnabled(false);
        continueButton.setEnabled(false);
    }

    private MappedElement getSelectedTask() {
        return taskList.getSelectedValue();
    }

}
