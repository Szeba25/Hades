package hu.szeba.hades.view.task;

import hu.szeba.hades.controller.task.TaskSolvingController;
import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.model.task.data.SourceFile;
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
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskSolvingView extends BaseView {

    private BaseView parentView;

    private TaskSolvingController controller;

    private Font monoFont;

    private JPanel topPanel;

    private DefaultListModel<String> fileListModel;
    private JList<String> fileList;
    private JScrollPane fileListScroller;

    private JTabbedPane codeTab;
    private Map<String, JTextArea> codeTabByName;

    private JEditorPane taskInstructionsPane;

    private TerminalArea terminalArea;
    private JScrollPane terminalScroll;

    private JSplitPane splitPane;
    private JSplitPane taskSplitPane;

    private JMenuBar menuBar;

    private JMenu fileMenu;
    private JMenuItem newFileMenuItem;
    private JMenuItem deleteFileMenuItem;
    private JMenuItem renameFileMenuItem;
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

    private LockedMenusWrapper lockedMenusWrapper;
    private boolean trackSourceChanges;

    public TaskSolvingView(BaseView parentView, Task task) {
        super();

        this.parentView = parentView;

        this.controller = new TaskSolvingController(task);
        this.controller.setSourceList(this);

        this.setTitle("Solving task: " + task.getData().getTaskName());
        this.runMenuItem.setEnabled(task.getCompilerOutputRegister().getCompilerOutput().isReady());
        this.stopMenuItem.setEnabled(false);
    }

    @Override
    public void initializeComponents() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setMinimumSize(new Dimension(900, 700));
        this.setLayout(new BorderLayout());

        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        monoFont = new Font("Monospaced", Font.PLAIN, 14);

        fileListModel = new DefaultListModel<>();
        fileList = new JList<>(fileListModel);
        fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fileList.setFixedCellWidth(125);

        fileListScroller = new JScrollPane(fileList);
        fileListScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        codeTab = new JTabbedPane();
        codeTabByName = new HashMap<>();

        taskInstructionsPane = new JEditorPane();
        taskInstructionsPane.setContentType("text/html");
        taskInstructionsPane.setEditable(false);

        topPanel.add(fileListScroller, BorderLayout.WEST);
        topPanel.add(codeTab, BorderLayout.CENTER);

        JTextPane terminalPane = new JTextPane();
        JPanel noWrapPanel = new JPanel(new BorderLayout());
        noWrapPanel.add(terminalPane);
        terminalArea = new TerminalArea(terminalPane);
        terminalScroll = new JScrollPane(noWrapPanel);
        terminalScroll.getVerticalScrollBar().setUnitIncrement(14);
        terminalScroll.setMinimumSize(new Dimension(300, 200));

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, terminalScroll);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.6);

        taskSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitPane, taskInstructionsPane);
        taskSplitPane.setOneTouchExpandable(true);
        taskSplitPane.setResizeWeight(0.8);

        menuBar = new JMenuBar();

        fileMenu = new JMenu("File");
        newFileMenuItem = new JMenuItem("New source file");
        deleteFileMenuItem = new JMenuItem("Delete source file");
        renameFileMenuItem = new JMenuItem("Edit source file name");
        saveAllFileMenuItem = new JMenuItem("Save all now...");
        clearTerminalMenuItem = new JMenuItem("Clear terminal");

        fileMenu.add(newFileMenuItem);
        fileMenu.add(deleteFileMenuItem);
        fileMenu.add(renameFileMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(saveAllFileMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(clearTerminalMenuItem);

        buildMenu = new JMenu("Build");
        buildMenuItem = new JMenuItem("Build all");
        buildAndRunMenuItem = new JMenuItem("Build all and run...");
        runMenuItem = new JMenuItem("Run...");
        stopMenuItem = new JMenuItem("Stop!");

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

        lockedMenusWrapper = new LockedMenusWrapper(
                newFileMenuItem,
                deleteFileMenuItem,
                renameFileMenuItem,
                saveAllFileMenuItem,
                buildMenuItem,
                buildAndRunMenuItem,
                runMenuItem,
                stopMenuItem);

        trackSourceChanges = false;

        menuBar.add(fileMenu);
        menuBar.add(buildMenu);
        menuBar.add(helpMenu);

        this.getContentPane().add(taskSplitPane, BorderLayout.CENTER);
        this.getContentPane().add(menuBar, BorderLayout.NORTH);
        this.pack();
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
                    int result = JOptionPane.showOptionDialog(new JFrame(), "Already leaving? :(", "Giving up...", JOptionPane.YES_NO_OPTION,
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
                            e.getMessage());
                }
            }
        });
        // Delete source file
        deleteFileMenuItem.addActionListener((event) -> {
            String selectedSourceName = fileList.getSelectedValue();
            int result = JOptionPane.showConfirmDialog(new JFrame(), "Delete source file: " + selectedSourceName + "?",
                    "Delete source file", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                try {
                    // Delete from sources (data)
                    controller.deleteSourceFile(selectedSourceName);
                    // Delete tab (if present)
                    for (int i = 0; i < codeTab.getTabCount(); i++) {
                        if (codeTab.getTitleAt(i).equals(selectedSourceName)) {
                            codeTab.remove(i);
                            break;
                        }
                    }
                    // Delete from map!
                    codeTabByName.remove(selectedSourceName);
                    // Delete from list!
                    fileListModel.removeElement(selectedSourceName);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(new JFrame(), "Unable to delete source file: " + e.getMessage());
                }
            }
        });
        // Rename source file
        renameFileMenuItem.addActionListener((event) -> {
            // TODO: Make this functional...
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
            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(URI.create("https://youtu.be/0EpIWybDPfI"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
    }

    public void setTaskInstructions(String longDescription) {
        taskInstructionsPane.setText(longDescription);
    }

    public void setCodeAreaContents(List<SourceFile> sources) {
        sources.forEach((file) -> codeTabByName.get(file.getName()).setText(file.getData()));
    }

    public void setCodeAreaContent(String name, String data) {
        codeTabByName.get(name).setText(data);
    }

    public void addSourceFile(String name, String syntaxStyle) {
        addCodeArea(name, syntaxStyle);
        if (!fileListModel.contains(name)) {
            fileListModel.addElement(name);
            fileList.setSelectedIndex(0);
        } else {
            // Select the last tab component, as a new area was added!
            codeTab.setSelectedIndex(codeTab.getTabCount() - 1);
        }
    }

    public void setSourceList(String[] sourceList, String syntaxStyle) {
        for (String src : sourceList) {
            fileListModel.addElement(src);
            addCodeArea(src, syntaxStyle);
        }
        fileList.setSelectedIndex(0);
    }

    @Deprecated
    private void addSimpleCodeArea(String name, String syntaxStyle) {
        JTextArea codeTabArea = new JTextArea();
        codeTabArea.setTabSize(4);
        codeTabArea.setFont(monoFont);
        JScrollPane codeTabScroll = new JScrollPane(codeTabArea);

        codeTab.add(name, codeTabScroll);
        codeTab.setTabComponentAt(codeTab.getTabCount()-1, new ClosableTabComponent(codeTab, controller));

        codeTabByName.put(name, codeTabArea);
    }

    private void addCodeArea(String name, String syntaxStyle) {
        RSyntaxTextArea codeTabArea = new RSyntaxTextArea();
        codeTabArea.setTabSize(4);
        codeTabArea.setAutoIndentEnabled(true);
        codeTabArea.setCodeFoldingEnabled(true);
        codeTabArea.setSyntaxEditingStyle(syntaxStyle);
        codeTabArea.setCurrentLineHighlightColor(new Color(10, 30, 140, 50));

        codeTabArea.setFont(monoFont);

        RTextScrollPane codeTabScroll = new RTextScrollPane(codeTabArea);
        codeTabScroll.setLineNumbersEnabled(true);

        codeTab.add(name, codeTabScroll);
        codeTab.setTabComponentAt(codeTab.getTabCount()-1, new ClosableTabComponent(codeTab, controller));

        codeTabByName.put(name, codeTabArea);
    }

}
