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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskSolvingView extends BaseView {

    private BaseView parentView;

    private TaskSolvingController taskSolvingController;

    private Font monoFont;

    private JPanel topPanel;

    private JList fileList;
    private JScrollPane fileListScroller;

    private JTabbedPane codeTab;
    private Map<String, RSyntaxTextArea> codeTabByName;

    private JEditorPane taskInstructionsPane;

    private TerminalArea terminalArea;
    private JScrollPane terminalScroll;

    private JSplitPane splitPane;
    private JSplitPane taskSplitPane;

    private JMenuBar menuBar;

    private JMenu buildMenu;
    private JMenuItem buildMenuItem;
    private JMenuItem buildAndRunMenuItem;
    private JMenuItem runMenuItem;
    private JMenuItem stopMenuItem;

    private BuildMenuWrapper buildMenuWrapper;

    public TaskSolvingView(BaseView parentView, Task task) {
        super();

        this.parentView = parentView;
        this.taskSolvingController = new TaskSolvingController(task);

        this.taskSolvingController.setSourceList(this);

        this.setTitle("Solving task: " + task.getData().getTaskName());
        this.runMenuItem.setEnabled(task.getCompilerOutput().isReady());
        this.stopMenuItem.setEnabled(false);
    }

    @Override
    public void initializeComponents() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setMinimumSize(new Dimension(900, 700));
        this.setLayout(new BorderLayout());

        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        monoFont = new Font("Consolas", Font.PLAIN, 14);

        fileList = new JList();
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

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, terminalScroll);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.7);

        taskSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitPane, taskInstructionsPane);
        taskSplitPane.setOneTouchExpandable(true);
        taskSplitPane.setResizeWeight(0.8);

        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

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

        buildMenuWrapper = new BuildMenuWrapper(buildMenuItem, buildAndRunMenuItem, runMenuItem, stopMenuItem);

        JMenu helpMenu = new JMenu("Help");

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
        // Build action
        buildMenuItem.addActionListener((event) -> {
            try {
                taskSolvingController.compile(codeTabByName, terminalArea, buildMenuWrapper);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // Build and run action
        buildAndRunMenuItem.addActionListener((event) -> {
            try {
                taskSolvingController.compileAndRun(codeTabByName, terminalArea, buildMenuWrapper);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // Run action
        runMenuItem.addActionListener((event) -> taskSolvingController.run(terminalArea, buildMenuWrapper));
        // Stop action
        stopMenuItem.addActionListener((event) -> {
            taskSolvingController.stopCurrentProcess(terminalArea);
        });
        // Switching (or opening: NYI) tabs with list
        fileList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JList list = (JList)e.getSource();
                if (e.getClickCount() == 2) {
                    String value = (String)list.getSelectedValue();
                    for (int i = 0; i < codeTab.getTabCount(); i++) {
                        String title = codeTab.getTitleAt(i);
                        if (title.equals(value)) {
                            codeTab.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
        });
    }

    public void setTaskInstructions(String longDescription) {
        taskInstructionsPane.setText(longDescription);
    }

    public void setCodeAreaContents(List<SourceFile> sources) {
        sources.forEach((file) -> codeTabByName.get(file.getName()).setText(file.getData()));
    }

    public void setSourceList(String[] sourceList, String syntaxStyle) {
        fileList.setListData(sourceList);
        for (String src : sourceList) {
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

        codeTab.addTab(name, codeTabScroll);
        codeTabByName.put(name, codeTabArea);
    }

}
