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

public class TaskSolvingView extends BaseView implements NewSourceFileListener {

    private BaseView parentView;
    private NewSourceFileForm newSourceFileForm;

    private TaskSolvingController controller;

    private Font monoFont;

    private JPanel topPanel;

    private DefaultListModel<String> fileListModel;
    private JList<String> fileList;
    private JScrollPane fileListScroller;

    private JTabbedPane codeTab;
    private Map<String, RSyntaxTextArea> codeTabByName;

    private JEditorPane taskInstructionsPane;

    private TerminalArea terminalArea;
    private JScrollPane terminalScroll;

    private JSplitPane splitPane;
    private JSplitPane taskSplitPane;

    private JMenuBar menuBar;

    private JMenu fileMenu;
    private JMenuItem newFileMenuItem;

    private JMenu buildMenu;
    private JMenuItem buildMenuItem;
    private JMenuItem buildAndRunMenuItem;
    private JMenuItem runMenuItem;
    private JMenuItem stopMenuItem;

    private JMenu helpMenu;
    private JMenuItem aboutMenuItem;
    private JMenuItem ultimateHelpMenuItem;

    private LockedMenusWrapper lockedMenusWrapper;

    public TaskSolvingView(BaseView parentView, Task task) {
        super();

        this.parentView = parentView;
        this.newSourceFileForm = new NewSourceFileForm(this);

        this.controller = new TaskSolvingController(task);
        this.controller.setSourceList(this);

        this.setTitle("Solving task: " + task.getData().getTaskName());
        this.runMenuItem.setEnabled(task.getCompilerOutputRegister().getCompilerOutput().isReady());
        this.stopMenuItem.setEnabled(false);
    }

    @Override
    public void initializeComponents() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

        fileMenu.add(newFileMenuItem);

        buildMenu = new JMenu("Build");
        buildMenuItem = new JMenuItem("Build all");
        buildAndRunMenuItem = new JMenuItem("Build all and run...");
        runMenuItem = new JMenuItem("Run...");
        stopMenuItem = new JMenuItem("Stop! (for this input)");

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

        lockedMenusWrapper = new LockedMenusWrapper(buildMenuItem, buildAndRunMenuItem, runMenuItem, stopMenuItem);

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
                super.windowClosing(event);
                parentView.showView();
            }
        });
        // Add new source file by a dialogue
        newFileMenuItem.addActionListener((event) -> {
            this.newSourceFileForm.setVisible(true);
        });
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
        // Switching (or opening: NYI) tabs with list
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
    }

    @Override
    public void addNewSourceFileTrigger(String name) throws IOException {
        controller.addNewSourceFile(name, this);
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
        codeTab.setTabComponentAt(codeTab.getTabCount()-1, new ClosableTabComponent(codeTab));

        codeTabByName.put(name, codeTabArea);
    }

}
