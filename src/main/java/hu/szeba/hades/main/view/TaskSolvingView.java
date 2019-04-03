package hu.szeba.hades.main.view;

import hu.szeba.hades.main.controller.TaskSolvingController;
import hu.szeba.hades.main.meta.Languages;
import hu.szeba.hades.main.meta.UltimateHelper;
import hu.szeba.hades.main.model.task.Task;
import hu.szeba.hades.main.io.EditableTextFile;
import hu.szeba.hades.main.util.GridBagSetter;
import hu.szeba.hades.main.view.components.*;
import jdk.nashorn.internal.scripts.JO;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class TaskSolvingView extends JFrame implements ViewableFrame {

    private ViewableFrame parentView;

    private TaskSolvingController controller;

    private Font monoFont;

    private DefaultListModel<String> fileListModel;
    private JList<String> fileList;
    private JPopupMenu fileListPopup;
    private JMenuItem deleteFilePopupItem;
    private JMenuItem renameFilePopupItem;

    private JTabbedPane codeTab;
    private Map<String, JTextArea> codeTabByName;

    private JEditorPane taskInstructionsPane;

    private JButton taskStoryShowButton;
    private JEditorPane taskStoryPane;
    private JDialog taskStoryDialog;
    private JButton taskStoryOkButton;
    private boolean storyPresent;

    private TerminalArea terminalArea;

    private JSplitPane taskSplitPane;
    private JMenuBar menuBar;

    private JMenu fileMenu;
    private JMenuItem newFileMenuItem;
    private JMenuItem saveAllFileMenuItem;
    private JMenuItem clearTerminalMenuItem;

    private JMenu buildMenu;
    private JMenuItem buildMenuItem;
    private JMenuItem buildAndRunMenuItem;
    private JMenuItem runMenuItem;
    private JMenuItem stopMenuItem;

    private JMenu helpMenu;
    private JMenuItem aboutMenuItem;
    private JMenuItem ultimateHelpMenuItem;

    private UltimateHelper ultimateHelper;
    private LockedMenusWrapper lockedMenusWrapper;

    public TaskSolvingView(ViewableFrame parentView, Task task) {

        // Register parent view
        this.parentView = parentView;

        // JFrame init
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setResizable(true);
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(900, 700));

        // Initialize components, and setup events
        initializeComponents();
        setupEvents();

        this.controller = new TaskSolvingController(task);

        // Set contents
        this.controller.setTaskViewContent(this);

        // Scroll back to top! (Setting caret position will take care of this)
        this.taskInstructionsPane.setCaretPosition(0);
        this.taskStoryPane.setCaretPosition(0);

        // Set title, and disable some menus based on the task
        this.setTitle(Languages.translate("Solving task:") + " " + task.getTaskDescription().getTitle());
        this.runMenuItem.setEnabled(task.getCompilerOutputRegister().getCompilerOutput().isReady());

        // Set task story title
        this.taskStoryDialog.setTitle(Languages.translate("Story:") + " " + task.getTaskDescription().getTitle());
        SwingUtilities.getRootPane(taskStoryDialog).setDefaultButton(taskStoryOkButton);
        this.taskStoryShowButton.setEnabled(storyPresent);

        // Put everything together, and pack!
        this.getContentPane().add(taskSplitPane, BorderLayout.CENTER);
        this.getContentPane().add(menuBar, BorderLayout.NORTH);
        this.pack();
    }

    private void initializeComponents() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        monoFont = new Font("Monospaced", Font.PLAIN, 14);

        fileListModel = new DefaultListModel<>();
        fileList = new JList<>(fileListModel);
        fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fileList.setFixedCellWidth(125);
        fileListPopup = new JPopupMenu();
        deleteFilePopupItem = new JMenuItem(Languages.translate("Delete"));
        renameFilePopupItem = new JMenuItem(Languages.translate("Rename"));

        fileListPopup.add(deleteFilePopupItem);
        fileListPopup.add(renameFilePopupItem);

        JScrollPane fileListScroller = new JScrollPane(fileList);
        fileListScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        codeTab = new JTabbedPane();
        codeTabByName = new HashMap<>();

        taskInstructionsPane = new JEditorPane();
        taskInstructionsPane.setContentType("text/html");
        taskInstructionsPane.setEditable(false);
        JScrollPane taskInstructionsScroll = new JScrollPane(taskInstructionsPane);
        taskInstructionsScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        taskStoryShowButton = new JButton(Languages.translate("Show story"));
        taskStoryShowButton.setPreferredSize(new Dimension(180, 30));
        taskStoryShowButton.setFocusPainted(false);

        taskStoryPane = new JEditorPane();
        taskStoryPane.setContentType("text/html");
        taskStoryPane.setEditable(false);
        taskStoryPane.setFocusable(false);
        JScrollPane taskStoryScroll = new JScrollPane(taskStoryPane);
        taskStoryScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        taskStoryScroll.setMinimumSize(new Dimension(250, 300));

        taskStoryDialog = new JDialog();
        taskStoryDialog.setModal(true);
        taskStoryDialog.setVisible(false);
        taskStoryDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        taskStoryDialog.setMinimumSize(new Dimension(640, 480));

        JPanel taskStoryContents = new JPanel();
        taskStoryContents.setLayout(new GridBagLayout());

        taskStoryOkButton = new JButton(Languages.translate("Challenge accepted!"));
        taskStoryOkButton.setFocusPainted(false);

        GridBagSetter gs0 = new GridBagSetter();
        gs0.setComponent(taskStoryContents);

        gs0.add(taskStoryScroll,
                0,
                0,
                GridBagConstraints.BOTH,
                1,
                1,
                1,
                1,
                new Insets(5, 5, 5, 5));

        gs0.add(taskStoryOkButton,
                0,
                1,
                GridBagConstraints.NONE,
                1,
                1,
                0,
                0,
                new Insets(0, 0, 5, 0));

        taskStoryDialog.getContentPane().add(taskStoryContents);
        taskStoryDialog.pack();

        JPanel taskInstructionAndStoryPanel = new JPanel();
        taskInstructionAndStoryPanel.setLayout(new GridBagLayout());
        taskInstructionAndStoryPanel.setMinimumSize(new Dimension(250, 700));
        taskInstructionAndStoryPanel.setPreferredSize(new Dimension(400, 700));

        GridBagSetter gs1 = new GridBagSetter();
        gs1.setComponent(taskInstructionAndStoryPanel);

        gs1.add(taskInstructionsScroll,
                0,
                0,
                GridBagConstraints.BOTH,
                1,
                1,
                1,
                1,
                new Insets(0, 0, 10, 0));

        gs1.add(taskStoryShowButton,
                0,
                1,
                GridBagConstraints.NONE,
                1,
                1,
                0,
                0,
                new Insets(0, 0, 10, 0));

        topPanel.add(fileListScroller, BorderLayout.WEST);
        topPanel.add(codeTab, BorderLayout.CENTER);

        JTextPane terminalPane = new JTextPane();
        JPanel noWrapPanel = new JPanel(new BorderLayout());
        noWrapPanel.add(terminalPane);
        terminalArea = new TerminalArea(terminalPane);
        JScrollPane terminalScroll = new JScrollPane(noWrapPanel);
        terminalScroll.getVerticalScrollBar().setUnitIncrement(14);
        terminalScroll.setMinimumSize(new Dimension(300, 200));

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, terminalScroll);
        mainSplitPane.setOneTouchExpandable(true);
        mainSplitPane.setResizeWeight(0.6);

        taskSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mainSplitPane, taskInstructionAndStoryPanel);
        taskSplitPane.setOneTouchExpandable(true);
        taskSplitPane.setResizeWeight(1.0);

        menuBar = new JMenuBar();

        fileMenu = new JMenu(Languages.translate("File"));
        newFileMenuItem = new JMenuItem(Languages.translate("New source file"));
        saveAllFileMenuItem = new JMenuItem(Languages.translate("Save all now..."));
        clearTerminalMenuItem = new JMenuItem(Languages.translate("Clear terminal"));

        fileMenu.add(newFileMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(saveAllFileMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(clearTerminalMenuItem);

        buildMenu = new JMenu(Languages.translate("Build"));
        buildMenuItem = new JMenuItem(Languages.translate("Build all"));
        buildAndRunMenuItem = new JMenuItem(Languages.translate("Build all and run..."));
        runMenuItem = new JMenuItem(Languages.translate("Run..."));
        stopMenuItem = new JMenuItem(Languages.translate("Stop!"));
        stopMenuItem.setEnabled(false);

        buildMenu.add(buildMenuItem);
        buildMenu.addSeparator();
        buildMenu.add(buildAndRunMenuItem);
        buildMenu.addSeparator();
        buildMenu.add(runMenuItem);
        buildMenu.addSeparator();
        buildMenu.add(stopMenuItem);

        helpMenu = new JMenu(Languages.translate("Help"));
        ultimateHelpMenuItem = new JMenuItem(Languages.translate("When nothing helps anymore..."));
        aboutMenuItem = new JMenuItem(Languages.translate("About"));

        helpMenu.add(ultimateHelpMenuItem);
        helpMenu.add(aboutMenuItem);

        ultimateHelper = new UltimateHelper();

        lockedMenusWrapper = new LockedMenusWrapper(
                newFileMenuItem,
                deleteFilePopupItem,
                renameFilePopupItem,
                saveAllFileMenuItem,
                buildMenuItem,
                buildAndRunMenuItem,
                runMenuItem,
                stopMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(buildMenu);
        menuBar.add(helpMenu);
    }

    private void setupEvents() {
        // Close the window
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                if (lockedMenusWrapper.getLockExit()) {
                    Object[] options = {Languages.translate("Ok")};
                    JOptionPane.showOptionDialog(
                            new JFrame(),
                            Languages.translate("Compiling (and/or) running in process!"),
                            Languages.translate("Cant't exit"),
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.WARNING_MESSAGE, null, options,  options[0]);
                } else {
                    Object[] options = {Languages.translate("Save and quit"), Languages.translate("Cancel")};
                    int result = JOptionPane.showOptionDialog(
                            new JFrame(),
                            Languages.translate("Save and quit?"),
                            Languages.translate("Goodbye..."),
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                    switch (result) {
                        case JOptionPane.YES_OPTION:
                            try {
                                controller.saveSourceContents(codeTabByName);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            super.windowClosing(event);
                            TaskSolvingView.this.dispose();
                            parentView.showView();
                            break;
                        case JOptionPane.NO_OPTION:
                            break;
                    }
                }
            }
        });
        // Add new source file by a dialogue
        newFileMenuItem.addActionListener((event) -> {
            String name = InputDialogFactory.showCustomInputDialog(Languages.translate("Add new source file"),
                    Languages.translate("New source file name:"),
                    Languages.translate("Ok"),
                    Languages.translate("Cancel"));
            if (name != null) {
                try {
                    controller.addNewSourceFile(name, this);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(new JFrame(),
                            Languages.translate("Cannot create source file specified:")+ " " + e.getMessage(),
                            Languages.translate("File creation error..."),
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        // Context menu
        fileList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    fileList.setSelectedIndex(fileList.locationToIndex(e.getPoint()));
                    fileListPopup.show(fileList, e.getPoint().x, e.getPoint().y);
                }
            }
        });
        // Delete source file
        deleteFilePopupItem.addActionListener((event) -> {
            String selectedSourceName = fileList.getSelectedValue();
            if (selectedSourceName == null) {
                JOptionPane.showMessageDialog(
                        new JFrame(),
                        Languages.translate("Please select a source file from the list!"),
                        Languages.translate("No source selected"), JOptionPane.WARNING_MESSAGE);

            } else if(controller.isSourceReadonly(selectedSourceName)) {
                JOptionPane.showMessageDialog(
                        new JFrame(),
                        Languages.translate("This source is readonly!"),
                        Languages.translate("Readonly file"), JOptionPane.WARNING_MESSAGE);
            } else {
                int result = JOptionPane.showConfirmDialog(
                        new JFrame(),
                        Languages.translate("Delete source file:") + "/" + selectedSourceName + "?",
                        Languages.translate("Delete source file"), JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    try {
                        controller.deleteSourceFile(selectedSourceName, this);
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
                    }
                }
            }
        });
        // Rename source file
        renameFilePopupItem.addActionListener((event) -> {
            String selectedSourceName = fileList.getSelectedValue();
            if (selectedSourceName == null) {
                JOptionPane.showMessageDialog(
                        new JFrame(),
                        Languages.translate("Please select a source file from the list!"),
                        Languages.translate("No source selected"),
                        JOptionPane.WARNING_MESSAGE);
            } else if(controller.isSourceReadonly(selectedSourceName)) {
                JOptionPane.showMessageDialog(
                        new JFrame(),
                        Languages.translate("This source is readonly!"),
                        Languages.translate("Readonly file"), JOptionPane.WARNING_MESSAGE);
            } else {
                String newName = InputDialogFactory.showCustomInputDialog(Languages.translate("Rename source file"),
                        Languages.translate("Rename source file:"),
                        Languages.translate("Ok"),
                        Languages.translate("Cancel"));
                if (newName != null) {
                    try {
                        controller.renameSourceFile(selectedSourceName, newName, this);
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
                    }
                }
            }
        });
        // Save all source files now!
        saveAllFileMenuItem.addActionListener((event) -> {
            try {
                controller.saveSourceContentsWithTerminalOutput(codeTabByName, terminalArea);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // Clear terminal!
        clearTerminalMenuItem.addActionListener((event) -> terminalArea.clear());
        // Build action
        buildMenuItem.addActionListener((event) -> {
            try {
                controller.compile(codeTabByName, terminalArea, lockedMenusWrapper);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // Build and run action
        buildAndRunMenuItem.addActionListener((event) -> {
            try {
                controller.compileAndRun(codeTabByName, terminalArea, lockedMenusWrapper);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // Run action
        runMenuItem.addActionListener((event) -> controller.run(terminalArea, lockedMenusWrapper));
        // Stop action
        stopMenuItem.addActionListener((event) -> {
            controller.stopCurrentProcess(terminalArea);
        });
        // Switching tabs with list
        fileList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JList list = (JList) e.getSource();
                    boolean found = false;
                    String value = (String) list.getSelectedValue();
                    for (int i = 0; i < codeTab.getTabCount(); i++) {
                        String title = codeTab.getTitleAt(i);
                        if (title.equals(value)) {
                            codeTab.setSelectedIndex(i);
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        controller.openExistingSourceFile(value, TaskSolvingView.this);
                    }
                }
            }
        });
        // Ultimate help
        ultimateHelpMenuItem.addActionListener((event) -> {
            try {
                ultimateHelper.help();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // About
        aboutMenuItem.addActionListener((event) -> {
            JOptionPane.showMessageDialog(new JFrame(),
                    Languages.translate("Hades (development build)") + "\n" +
                            Languages.translate("Contact: underworld.support@gmail.com") + "\n" +
                            Languages.translate("Source: https://github.com/Szeba25/hades") + "\n" +
                            Languages.translate("License: MIT (see repository)"),
                            "Hades",  JOptionPane.PLAIN_MESSAGE);
        });
        // Show story
        taskStoryShowButton.addActionListener((event) -> {
            showStoryDialog();
        });
        // Close story
        taskStoryOkButton.addActionListener((event) -> {
            taskStoryDialog.setVisible(false);
        });
    }

    public void setTaskInstructions(String longDescription) {
        taskInstructionsPane.setText(longDescription);
    }

    public void setTaskStory(String text) {
        taskStoryPane.setText(text);
        storyPresent = text.length() > 0;
    }

    public void setCodeAreaContents(List<EditableTextFile> sources) {
        for (EditableTextFile sf : sources) {
            codeTabByName.get(sf.getName()).setText(sf.getData());
        }
    }

    public void setCodeAreaContent(String name, String data) {
        codeTabByName.get(name).setText(data);
    }

    public void addSourceFile(String name, boolean readonly, String syntaxStyle) {
        addCodeArea(name, readonly, syntaxStyle);
        if (!fileListModel.contains(name)) {
            // Add the new element
            fileListModel.addElement(name);
            // Sort the file list
            sortFileList();
        }
        // Select this element in the list!
        fileList.setSelectedIndex(fileListModel.indexOf(name));
        // Select the last tab component, as a new area was added!
        codeTab.setSelectedIndex(codeTab.getTabCount() - 1);
    }

    public void renameSourceFile(String oldName, String newName) {
        // Rename tab (if present)
        for (int i = 0; i < codeTab.getTabCount(); i++) {
            if (codeTab.getTitleAt(i).equals(oldName)) {
                codeTab.setTitleAt(i, newName);
                // Update the tab width
                ((ClosableTabComponent)codeTab.getTabComponentAt(i)).updateUI();
                break;
            }
        }
        // Re-put to map
        JTextArea area = codeTabByName.remove(oldName);
        codeTabByName.put(newName, area);
        // Rename in list!
        fileListModel.removeElement(oldName);
        fileListModel.addElement(newName);
        sortFileList();
    }

    public void deleteSourceFile(String name) {
        // Delete tab (if present)
        for (int i = 0; i < codeTab.getTabCount(); i++) {
            if (codeTab.getTitleAt(i).equals(name)) {
                codeTab.remove(i);
                break;
            }
        }
        // Delete from map!
        codeTabByName.remove(name);
        // Delete from list!
        fileListModel.removeElement(name);
    }

    public void setSourceList(String[] sourceList, Set<String> readonlySources, String syntaxStyle) {
        for (String src : sourceList) {
            fileListModel.addElement(src);
            addCodeArea(src, readonlySources.contains(src), syntaxStyle);
        }
        sortFileList();
        fileList.setSelectedIndex(0);
    }

    public void showStoryDialog() {
        if (storyPresent) {
            taskStoryDialog.setLocationRelativeTo(this);
            taskStoryDialog.requestFocus();
            taskStoryDialog.setVisible(true);
        }
    }

    private void sortFileList() {
        List<String> list = Collections.list(fileListModel.elements());
        Collections.sort(list);
        fileListModel.clear();
        for (String added : list) {
            fileListModel.addElement(added);
        }
    }

    @Deprecated
    private void addSimpleCodeArea(String name, boolean readonly) {
        JTextArea codeTabArea = new JTextArea();
        codeTabArea.setTabSize(4);
        codeTabArea.setFont(monoFont);
        codeTabArea.setEditable(readonly);

        JScrollPane codeTabScroll = new JScrollPane(codeTabArea);

        codeTab.add(name, codeTabScroll);
        codeTab.setTabComponentAt(codeTab.getTabCount()-1, new ClosableTabComponent(codeTab, controller));

        codeTabByName.put(name, codeTabArea);
    }

    private void addCodeArea(String name, boolean readonly, String syntaxStyle) {
        RSyntaxTextArea codeTabArea = new RSyntaxTextArea();
        codeTabArea.setEditable(!readonly);
        codeTabArea.setTabSize(4);
        codeTabArea.setSyntaxEditingStyle(syntaxStyle);
        codeTabArea.setFont(monoFont);
        if (!readonly) {
            codeTabArea.setAutoIndentEnabled(true);
            codeTabArea.setCodeFoldingEnabled(true);
            codeTabArea.setCurrentLineHighlightColor(new Color(10, 30, 140, 35));
        } else {
            codeTabArea.setCurrentLineHighlightColor(new Color(0, 0, 0, 0));
            codeTabArea.setBracketMatchingEnabled(false);
            codeTabArea.setPaintMatchedBracketPair(false);
            codeTabArea.setBackground(new Color(245, 245, 245, 255));
        }

        RTextScrollPane codeTabScroll = new RTextScrollPane(codeTabArea);
        codeTabScroll.setLineNumbersEnabled(true);

        codeTab.add(name, codeTabScroll);
        codeTab.setTabComponentAt(codeTab.getTabCount()-1, new ClosableTabComponent(codeTab, controller));

        codeTabByName.put(name, codeTabArea);
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
