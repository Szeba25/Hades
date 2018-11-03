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

    private JTabbedPane codeTab;
    private Map<String, RSyntaxTextArea> codeTabByName;

    private JTextArea terminalArea;
    private JScrollPane terminalScroll;

    private JSplitPane splitPane;

    private JMenuBar menuBar;

    private JMenu buildMenu;
    private JMenuItem buildMenuItem;
    private JMenuItem runMenuItem;

    private JList fileList;
    private JScrollPane fileListScroller;

    public TaskSolvingView(BaseView parentView, Task task) {
        super();

        this.parentView = parentView;
        this.taskSolvingController = new TaskSolvingController(this, task);

        this.setTitle("Solving task: " + task.getTaskName());
    }

    @Override
    public void initializeComponents() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setMinimumSize(new Dimension(900, 700));
        this.setLayout(new BorderLayout());

        codeTab = new JTabbedPane();
        codeTab.setMinimumSize(new Dimension(900, 400));

        codeTabByName = new HashMap<>();

        terminalArea = new JTextArea();
        terminalArea.setEditable(false);

        terminalScroll = new JScrollPane(terminalArea);
        terminalScroll.setMinimumSize(new Dimension(900, 250));

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, codeTab, terminalScroll);
        splitPane.setPreferredSize(new Dimension(900, 700));
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(700);

        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        buildMenu = new JMenu("Build");
        buildMenuItem = new JMenuItem("Build all");
        runMenuItem = new JMenuItem("Run...");
        buildMenu.add(buildMenuItem);
        buildMenu.addSeparator();
        buildMenu.add(runMenuItem);

        JMenu helpMenu = new JMenu("Help");

        menuBar.add(fileMenu);
        menuBar.add(buildMenu);
        menuBar.add(helpMenu);

        fileList = new JList();
        fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fileList.setFixedCellWidth(250);

        fileListScroller = new JScrollPane(fileList);
        fileListScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        this.getContentPane().add(fileListScroller, BorderLayout.WEST);
        this.getContentPane().add(splitPane, BorderLayout.CENTER);
        this.getContentPane().add(menuBar, BorderLayout.NORTH);
        this.pack();
    }

    @Override
    public void setupEvents() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
            super.windowClosing(event);
            parentView.showView();
            }
        });
        buildMenuItem.addActionListener((event) -> {
            try {
                taskSolvingController.compile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        runMenuItem.addActionListener((event) -> {
            taskSolvingController.run();
        });
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

    private void addCodeArea(String name, String syntaxStyle) {
        RSyntaxTextArea codeTabArea = new RSyntaxTextArea();
        codeTabArea.setTabSize(4);
        codeTabArea.setAutoIndentEnabled(true);
        codeTabArea.setCodeFoldingEnabled(true);
        codeTabArea.setSyntaxEditingStyle(syntaxStyle);
        codeTabArea.setCurrentLineHighlightColor(new Color(10, 30, 140, 50));
        codeTabArea.setFont(new Font("Consolas", Font.PLAIN, 14));

        RTextScrollPane codeTabScroll = new RTextScrollPane(codeTabArea);
        codeTabScroll.setLineNumbersEnabled(true);

        codeTab.addTab(name, codeTabScroll);
        codeTabByName.put(name, codeTabArea);
    }

    public void setCodeAreaContents(List<SourceFile> sources) {
        sources.forEach((file) -> codeTabByName.get(file.getName()).setText(file.getData()));
    }

    public Map<String, RSyntaxTextArea> getCodeAreas() {
        return codeTabByName;
    }

    public JMenu getBuildMenu() {
        return buildMenu;
    }

    public JTextArea getTerminalArea() {
        return terminalArea;
    }

    public void setSourceList(String[] sourceList, String syntaxStyle) {
        fileList.setListData(sourceList);
        for (String src : sourceList) {
            addCodeArea(src, syntaxStyle);
        }
        fileList.setSelectedIndex(0);
    }
}
