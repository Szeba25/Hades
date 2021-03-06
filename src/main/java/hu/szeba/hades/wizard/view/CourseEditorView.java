package hu.szeba.hades.wizard.view;

import hu.szeba.hades.main.meta.Languages;
import hu.szeba.hades.main.util.GridBagSetter;
import hu.szeba.hades.main.view.components.DialogFactory;
import hu.szeba.hades.main.view.components.ViewableFrame;
import hu.szeba.hades.wizard.controller.CourseEditorController;
import hu.szeba.hades.wizard.model.WizardCourse;
import hu.szeba.hades.wizard.view.components.DynamicButtonListPanel;
import hu.szeba.hades.wizard.view.elements.DescriptiveElement;
import hu.szeba.hades.wizard.view.panels.ModeEditorPanel;
import hu.szeba.hades.wizard.view.panels.TaskCollectionEditorPanel;
import hu.szeba.hades.wizard.view.panels.TaskEditorPanel;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class CourseEditorView extends JFrame implements ViewableFrame {

    private ViewableFrame parentView;

    private CourseEditorController controller;

    private JPanel topPanel;
    private JTextField titleField;
    private JTextField languageField;

    private JTabbedPane tabbedPane;

    private DynamicButtonListPanel modeList;
    private DynamicButtonListPanel taskCollectionList;
    private DynamicButtonListPanel taskList;

    private ModeEditorPanel modeEditor;
    private TaskCollectionEditorPanel taskCollectionEditor;
    private TaskEditorPanel taskEditor;

    private JPanel modesTab;
    private JPanel taskCollectionsTab;
    private JPanel tasksTab;

    public CourseEditorView(ViewableFrame parentView, String courseId)
            throws ParserConfigurationException, SAXException, IOException {

        this.parentView = parentView;

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setTitle(Languages.translate("Wizard: Course editor"));
        this.setLayout(new BorderLayout(0, 10));
        this.setMinimumSize(new Dimension(1100, 700));
        this.setResizable(true);

        initializeComponents();
        setupEvents();

        WizardCourse course = new WizardCourse(courseId);
        controller = new CourseEditorController(course);

        // Set main list contents
        controller.setModeListContents(modeList.getList());
        controller.setTaskCollectionListContents(taskCollectionList.getList());
        controller.setTaskListContents(taskList.getList());

        titleField.setText(course.getCourseTitle());
        languageField.setText(course.getLanguage());

        this.getContentPane().add(topPanel, BorderLayout.NORTH);
        this.getContentPane().add(tabbedPane, BorderLayout.CENTER);
        this.pack();
    }

    private void initializeComponents() {
        topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());

        JLabel titleLabel = new JLabel(Languages.translate("Title:"));
        titleField = new JTextField();
        titleLabel.setLabelFor(titleField);

        JLabel languageLabel = new JLabel(Languages.translate("Programming language:"));
        languageField = new JTextField();
        languageLabel.setLabelFor(languageField);

        GridBagSetter gs = new GridBagSetter();
        gs.setComponent(topPanel);

        gs.add(titleLabel,
                0,
                0,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                0,
                new Insets(5, 5, 0, 5));

        gs.add(titleField,
                1,
                0,
                GridBagConstraints.HORIZONTAL,
                2,
                1,
                1,
                0,
                new Insets(5, 5, 0, 5));

        gs.add(languageLabel,
                0,
                1,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                0,
                new Insets(5, 5, 0, 5));

        gs.add(languageField,
                1,
                1,
                GridBagConstraints.HORIZONTAL,
                2,
                1,
                1,
                0,
                new Insets(5, 5, 0, 5));

        tabbedPane = new JTabbedPane();

        modeList = new DynamicButtonListPanel(Languages.translate("All modes:"), 300, "+", "-");
        taskCollectionList = new DynamicButtonListPanel(Languages.translate("All task collections:"), 300, "+", "-");
        taskList = new DynamicButtonListPanel(Languages.translate("All tasks:"), 300, "+", "-");

        modesTab = new JPanel();
        modesTab.setLayout(new BorderLayout(5, 0));
        modesTab.add(modeList, BorderLayout.WEST);

        modeEditor = new ModeEditorPanel();
        modesTab.add(modeEditor, BorderLayout.CENTER);

        taskCollectionsTab = new JPanel();
        taskCollectionsTab.setLayout(new BorderLayout(5, 0));
        taskCollectionsTab.add(taskCollectionList, BorderLayout.WEST);

        taskCollectionEditor = new TaskCollectionEditorPanel();
        taskCollectionsTab.add(taskCollectionEditor, BorderLayout.CENTER);

        tasksTab = new JPanel();
        tasksTab.setLayout(new BorderLayout(5, 0));
        tasksTab.add(taskList, BorderLayout.WEST);

        taskEditor = new TaskEditorPanel();
        tasksTab.add(taskEditor, BorderLayout.CENTER);

        tabbedPane.addTab(Languages.translate("Modes"), modesTab);
        tabbedPane.addTab(Languages.translate("Task collections"), taskCollectionsTab);
        tabbedPane.addTab(Languages.translate("Tasks"), tasksTab);
    }

    private void setupEvents() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);

                // Apply changes
                modifyAllChanges();

                // Prompt user for saving
                int result = DialogFactory.showCustomCancellableChoiceDialog(
                        Languages.translate("Save progress?"),
                        Languages.translate("Leave course..."),
                        Languages.translate("Save and quit"),
                        Languages.translate("Just quit"),
                        Languages.translate("Cancel"));

                switch (result) {
                    case JOptionPane.YES_OPTION:
                        try {
                            // Save the course!
                            controller.save(titleField.getText(), languageField.getText());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        CourseEditorView.this.dispose();
                        parentView.showView();
                        break;
                    case JOptionPane.NO_OPTION:
                        System.out.println("Course not saved...");
                        CourseEditorView.this.dispose();
                        parentView.showView();
                        break;
                    default:
                        break;
                }
            }
        });

        tabbedPane.addChangeListener((e) -> modifyAllChanges());

        modeList.getModifier().getButton(0).addActionListener((event) -> {
            try {
                controller.newMode();
            } catch (IOException e) {
                e.printStackTrace();
            }
            controller.setModeListContents(modeList.getList());
        });

        modeList.getModifier().getButton(1).addActionListener((event) -> {
            controller.deleteMode(modeList.getList(), modeEditor);
        });

        taskCollectionList.getModifier().getButton(0).addActionListener((event) -> {
            try {
                controller.newTaskCollection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            controller.setTaskCollectionListContents(taskCollectionList.getList());
        });

        taskCollectionList.getModifier().getButton(1).addActionListener((event) -> {
            controller.deleteTaskCollection(taskCollectionList.getList(), taskCollectionEditor);
        });

        taskList.getModifier().getButton(0).addActionListener((event) -> {
            try {
                controller.newTask();
            } catch (IOException | ParserConfigurationException | SAXException e) {
                e.printStackTrace();
            }
            controller.setTaskListContents(taskList.getList());
        });

        taskList.getModifier().getButton(1).addActionListener((event) -> {
            controller.deleteTask(taskList.getList(), taskEditor);
        });

        modeList.getList().getSelectionModel().addListSelectionListener((event) -> {
            ListSelectionModel listSelectionModel = (ListSelectionModel) event.getSource();
            ListModel listModel = modeList.getList().getModel();
            if (!listSelectionModel.isSelectionEmpty()) {
                int idx = listSelectionModel.getMinSelectionIndex();
                if (listSelectionModel.isSelectedIndex(idx)) {
                    DescriptiveElement element = (DescriptiveElement) listModel.getElementAt(idx);
                    controller.setCurrentMode(modeEditor, element);
                }
            }
        });

        taskCollectionList.getList().getSelectionModel().addListSelectionListener((event) -> {
            ListSelectionModel listSelectionModel = (ListSelectionModel) event.getSource();
            ListModel listModel = taskCollectionList.getList().getModel();
            if (!listSelectionModel.isSelectionEmpty()) {
                int idx = listSelectionModel.getMinSelectionIndex();
                if (listSelectionModel.isSelectedIndex(idx)) {
                    DescriptiveElement element = (DescriptiveElement) listModel.getElementAt(idx);
                    controller.setCurrentTaskCollection(taskCollectionEditor, element, modeEditor);
                }
            }
        });

        taskList.getList().getSelectionModel().addListSelectionListener((event) -> {
            ListSelectionModel listSelectionModel = (ListSelectionModel) event.getSource();
            ListModel listModel = taskList.getList().getModel();
            if (!listSelectionModel.isSelectionEmpty()) {
                int idx = listSelectionModel.getMinSelectionIndex();
                if (listSelectionModel.isSelectedIndex(idx)) {
                    DescriptiveElement element = (DescriptiveElement) listModel.getElementAt(idx);
                    controller.setCurrentTask(taskEditor, element, taskCollectionEditor);
                }
            }
        });

        // Saving with CTRL+S
        tabbedPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ctrl S"), "save course");
        tabbedPane.getActionMap().put("save course", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.save(titleField.getText(), languageField.getText());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void modifyAllChanges() {
        // Trigger update on current list element
        if (modeList.getList().getSelectedValue() != null) {
            controller.setCurrentMode(modeEditor, (DescriptiveElement) modeList.getList().getSelectedValue());
        }
        if (taskCollectionList.getList().getSelectedValue() != null) {
            controller.setCurrentTaskCollection(taskCollectionEditor, (DescriptiveElement) taskCollectionList.getList().getSelectedValue(), modeEditor);
        }
        if (taskList.getList().getSelectedValue() != null) {
            controller.setCurrentTask(taskEditor, (DescriptiveElement) taskList.getList().getSelectedValue(), taskCollectionEditor);
        }
    }

    @Override
    public void showView() {
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.requestFocus();
    }

    @Override
    public void showViewMaximized() {
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
        this.requestFocus();
    }

    @Override
    public void hideView() {
        this.setVisible(false);
    }

}
