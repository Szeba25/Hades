package hu.szeba.hades.view.task;

import hu.szeba.hades.controller.task.TaskSolvingController;
import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.view.BaseView;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class TaskSolvingView extends BaseView {

    private BaseView parentView;

    private TaskSolvingController taskSolvingController;

    private RSyntaxTextArea codeArea;
    private RTextScrollPane codeScroll;

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
    }

    @Override
    public void initializeComponents() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setMinimumSize(new Dimension(900, 700));
        this.setLayout(new BorderLayout());

        codeArea = new RSyntaxTextArea();
        codeArea.setTabSize(4);
        codeArea.setAutoIndentEnabled(true);
        codeArea.setCodeFoldingEnabled(true);
        codeArea.setSyntaxEditingStyle(RSyntaxTextArea.SYNTAX_STYLE_C);
        codeArea.setCurrentLineHighlightColor(new Color(10, 30, 140, 50));
        codeArea.setFont(new Font("Consolas", Font.PLAIN, 14));

        codeScroll = new RTextScrollPane(codeArea);
        codeScroll.setMinimumSize(new Dimension(900, 400));
        codeScroll.setLineNumbersEnabled(true);

        terminalArea = new JTextArea();
        terminalArea.setEditable(false);

        terminalScroll = new JScrollPane(terminalArea);
        terminalScroll.setMinimumSize(new Dimension(900, 250));

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, codeScroll, terminalScroll);
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
                taskSolvingController.compile(fileList.getSelectedIndex());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        /*
        fileList.addListSelectionListener(e ->
            if (!e.getValueIsAdjusting()) {
                int selected = fileList.getSelectedIndex();
                int previous = selected == e.getFirstIndex() ? e.getLastIndex() : e.getFirstIndex();
                if (selected != previous) {
                    taskSolvingController.changeFile(selected, previous);
                }
            }
        });
        */
    }

    public void setCodeAreaContent(String text) {
        codeArea.setText(text);
    }

    public String getCodeAreaContent() {
        return codeArea.getText();
    }

    public JMenuItem getBuildMenu() {
        return buildMenu;
    }

    public JTextArea getTerminalArea() {
        return terminalArea;
    }

    public void setSourceList(String[] sourceList) {
        fileList.setListData(sourceList);
        /*
        for (int i = 0; i < sourceList.length; i++) {
            addCodeArea(i);
        }
        */
        fileList.setSelectedIndex(0);
    }
}
