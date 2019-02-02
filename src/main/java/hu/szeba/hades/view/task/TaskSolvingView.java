package hu.szeba.hades.view.task;

import hu.szeba.hades.controller.task.TaskSolvingController;
import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.model.task.data.SourceFile;
import hu.szeba.hades.view.BaseView;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskSolvingView extends BaseView {

    private BaseView parentView;

    private TaskSolvingController taskSolvingController;

    private JPanel topPanel;

    private JTabbedPane codeTab;
    private Map<String, RSyntaxTextArea> codeTabByName;

    private JList fileList;
    private JScrollPane fileListScroller;

    private JTextArea terminalArea;
    private JScrollPane terminalScroll;

    private JSplitPane splitPane;

    private JMenuBar menuBar;

    private JMenu buildMenu;
    private JMenuItem buildMenuItem;
    private JMenuItem buildAndRunMenuItem;
    private JMenuItem runMenuItem;

    public TaskSolvingView(BaseView parentView, Task task) {
        super();

        this.parentView = parentView;
        this.taskSolvingController = new TaskSolvingController(task);

        this.taskSolvingController.setSourceList(this);

        this.setTitle("Solving task: " + task.getData().getTaskName());
        this.runMenuItem.setEnabled(task.getCompilerOutput().isReady());
    }

    @Override
    public void initializeComponents() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setMinimumSize(new Dimension(900, 700));
        this.setLayout(new BorderLayout());

        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        codeTab = new JTabbedPane();

        codeTabByName = new HashMap<>();

        fileList = new JList();
        fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fileList.setFixedCellWidth(125);

        fileListScroller = new JScrollPane(fileList);
        fileListScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        topPanel.add(fileListScroller, BorderLayout.WEST);
        topPanel.add(codeTab, BorderLayout.CENTER);

        terminalArea = new JTextArea();
        terminalArea.setEditable(false);

        terminalScroll = new JScrollPane(terminalArea);

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, terminalScroll);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.7);

        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        buildMenu = new JMenu("Build");

        buildMenuItem = new JMenuItem("Build all");
        buildAndRunMenuItem = new JMenuItem("Build all and run...");
        runMenuItem = new JMenuItem("Run...");

        buildMenu.add(buildMenuItem);
        buildMenu.addSeparator();
        buildMenu.add(buildAndRunMenuItem);
        buildMenu.addSeparator();
        buildMenu.add(runMenuItem);

        JMenu helpMenu = new JMenu("Help");

        menuBar.add(fileMenu);
        menuBar.add(buildMenu);
        menuBar.add(helpMenu);

        this.getContentPane().add(splitPane, BorderLayout.CENTER);
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
                taskSolvingController.compile(codeTabByName, terminalArea, buildMenu);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // Build and run action
        buildAndRunMenuItem.addActionListener((event) -> {
            try {
                taskSolvingController.compileAndRun(codeTabByName, terminalArea, buildMenu);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // Run action
        runMenuItem.addActionListener((event) -> taskSolvingController.run(terminalArea, buildMenu));
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
        codeTabArea.setFont(new Font("Consolas", Font.PLAIN, 14));

        /*
        try {
            Theme theme = Theme.load(new FileInputStream(new File("vs.xml")));
            theme.apply(codeTabArea);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        RTextScrollPane codeTabScroll = new RTextScrollPane(codeTabArea);
        codeTabScroll.setLineNumbersEnabled(true);

        codeTab.addTab(name, codeTabScroll);
        codeTabByName.put(name, codeTabArea);
    }

}
