package hu.szeba.hades.view.task;

import hu.szeba.hades.controller.task.TaskSolvingController;
import hu.szeba.hades.meta.UltimateHelper;
import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.model.task.data.SourceFile;
import hu.szeba.hades.util.GridBagSetter;
import hu.szeba.hades.view.BaseView;
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

public class TaskSolvingView extends BaseView {

    private BaseView parentView;

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

    public TaskSolvingView(BaseView parentView, Task task) {
        super();

        this.parentView = parentView;
        this.controller = new TaskSolvingController(task);

        // Set contents
        this.controller.setTaskViewContent(this);

        // Scroll back to top! (Setting caret position will take care of this)
        this.taskInstructionsPane.setCaretPosition(0);
        this.taskStoryPane.setCaretPosition(0);

        // Set title, and disable some menus based on the task
        this.setTitle("Solving task: " + task.getTaskDescription().getTaskTitle());
        this.runMenuItem.setEnabled(task.getCompilerOutputRegister().getCompilerOutput().isReady());

        // Set task story title
        this.taskStoryDialog.setTitle("Story: " + task.getTaskDescription().getTaskTitle());
        this.taskStoryShowButton.setEnabled(storyPresent);

        // Put everything together, and pack!
        this.getContentPane().add(taskSplitPane, BorderLayout.CENTER);
        this.getContentPane().add(menuBar, BorderLayout.NORTH);
        this.pack();
    }

    @Override
    public void initializeComponents() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setMinimumSize(new Dimension(900, 700));
        this.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        monoFont = new Font("Monospaced", Font.PLAIN, 14);

        fileListModel = new DefaultListModel<>();
        fileList = new JList<>(fileListModel);
        fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fileList.setFixedCellWidth(125);
        fileListPopup = new JPopupMenu();
        deleteFilePopupItem = new JMenuItem("Delete");
        renameFilePopupItem = new JMenuItem("Rename");

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

        taskStoryShowButton = new JButton("Show story");
        taskStoryShowButton.setPreferredSize(new Dimension(180, 30));
        taskStoryShowButton.setFocusPainted(false);

        taskStoryPane = new JEditorPane();
        taskStoryPane.setContentType("text/html");
        taskStoryPane.setEditable(false);
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

        taskStoryOkButton = new JButton("Challenge accepted!");
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

        fileMenu = new JMenu("File");
        newFileMenuItem = new JMenuItem("New source file");
        saveAllFileMenuItem = new JMenuItem("Save all now...");
        clearTerminalMenuItem = new JMenuItem("Clear terminal");

        fileMenu.add(newFileMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(saveAllFileMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(clearTerminalMenuItem);

        buildMenu = new JMenu("Build");
        buildMenuItem = new JMenuItem("Build all");
        buildAndRunMenuItem = new JMenuItem("Build all and run...");
        runMenuItem = new JMenuItem("Run...");
        stopMenuItem = new JMenuItem("Stop!");
        stopMenuItem.setEnabled(false);

        buildMenu.add(buildMenuItem);
        buildMenu.addSeparator();
        buildMenu.add(buildAndRunMenuItem);
        buildMenu.addSeparator();
        buildMenu.add(runMenuItem);
        buildMenu.addSeparator();
        buildMenu.add(stopMenuItem);

        helpMenu = new JMenu("Help");
        ultimateHelpMenuItem = new JMenuItem("When nothing helps anymore...");
        aboutMenuItem = new JMenuItem("About");

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

    @Override
    public void setupEvents() {
        // Close the window
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                if (lockedMenusWrapper.getLockExit()) {
                    JOptionPane.showMessageDialog(new JFrame(), "Compiling (and/or) running in process!", "Cant't exit", JOptionPane.WARNING_MESSAGE);
                } else {
                    Object[] options = {"Save and quit", "Cancel"};
                    int result = JOptionPane.showOptionDialog(new JFrame(), "Save progress and close this task?", "Goodbye...", JOptionPane.YES_NO_OPTION,
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
            String name = JOptionPane.showInputDialog(new JFrame(),
                    "New source file name:",
                    "Add new source file",
                    JOptionPane.PLAIN_MESSAGE);
            if (name != null) {
                try {
                    controller.addNewSourceFile(name, this);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(new JFrame(), "Cannot create source file specified: " +
                            e.getMessage(), "File saving error...", JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(new JFrame(), "Please select a source file from the list!", "No source selected", JOptionPane.WARNING_MESSAGE);
            } else if(controller.isSourceReadonly(selectedSourceName)) {
                JOptionPane.showMessageDialog(new JFrame(), "This source is readonly!", "Readonly file", JOptionPane.WARNING_MESSAGE);
            } else {
                int result = JOptionPane.showConfirmDialog(new JFrame(), "Delete source file: " + selectedSourceName + "?",
                        "Delete source file", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    try {
                        controller.deleteSourceFile(selectedSourceName, this);
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(new JFrame(), "Unable to delete source file: " + e.getMessage());
                    }
                }
            }
        });
        // Rename source file
        renameFilePopupItem.addActionListener((event) -> {
            String selectedSourceName = fileList.getSelectedValue();
            if (selectedSourceName == null) {
                JOptionPane.showMessageDialog(new JFrame(), "Please select a source file from the list!", "No source selected", JOptionPane.WARNING_MESSAGE);
            } else if(controller.isSourceReadonly(selectedSourceName)) {
                JOptionPane.showMessageDialog(new JFrame(), "This source is readonly!", "Readonly file", JOptionPane.WARNING_MESSAGE);
            } else {
                String newName = (String) JOptionPane.showInputDialog(new JFrame(),
                        "Rename source file:",
                        "Rename source file",
                        JOptionPane.PLAIN_MESSAGE, null, null, selectedSourceName);
                if (newName != null) {
                    try {
                        controller.renameSourceFile(selectedSourceName, newName, this);
                    } catch (IOException e) {
                        e.printStackTrace();
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
                    "Hades (development build)\n\n"  +
                    "Contact: underworld.support@gmail.com\n" +
                    "Source: https://github.com/Szeba25/hades\n" +
                    "License: MIT (see repository)",
                    "About...",  JOptionPane.PLAIN_MESSAGE);
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

    public void setCodeAreaContents(List<SourceFile> sources) {
        for (SourceFile sf : sources) {
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

}
