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

public class TaskSolvingView extends BaseView {

    private BaseView parentView;

    private TaskSolvingController taskSolvingController;

    private RSyntaxTextArea codeArea;
    private RTextScrollPane codeScroll;

    private JTextArea terminalArea;
    private JScrollPane terminalScroll;

    private JSplitPane splitPane;

    private JMenuBar menuBar;
    private JMenuItem compileMenuItem;

    public TaskSolvingView(BaseView parentView, Task task) {
        super();
        this.parentView = parentView;
        this.taskSolvingController = new TaskSolvingController(this, task);
    }

    @Override
    public void initializeComponents() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());

        codeArea = new RSyntaxTextArea();
        codeArea.setTabSize(4);
        codeArea.setAutoIndentEnabled(true);
        codeArea.setCodeFoldingEnabled(true);
        codeArea.setSyntaxEditingStyle(RSyntaxTextArea.SYNTAX_STYLE_C);
        codeArea.setCurrentLineHighlightColor(new Color(10, 30, 140, 50));
        codeArea.setFont(new Font("Consolas", Font.PLAIN, 14));

        terminalArea = new JTextArea();

        codeScroll = new RTextScrollPane(codeArea);
        codeScroll.setLineNumbersEnabled(true);
        codeScroll.setMinimumSize(new Dimension(800, 600));

        terminalScroll = new JScrollPane(terminalArea);
        terminalScroll.setMinimumSize(new Dimension(800, 200));

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, codeScroll, terminalScroll);
        splitPane.setPreferredSize(new Dimension(800, 800));
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(150);

        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu buildMenu = new JMenu("Build");
        compileMenuItem = new JMenuItem("Compile");
        buildMenu.add(compileMenuItem);
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
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
            super.windowClosing(event);
            parentView.showView();
            }
        });
        compileMenuItem.addActionListener((event) -> {
            try {
                taskSolvingController.compile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void setCodeAreaContent(String text) {
        codeArea.setText(text);
    }

    public String getCodeAreaContent() {
        return codeArea.getText();
    }

    public JMenuItem getCompileMenuItem() {
        return compileMenuItem;
    }

    public JTextArea getTerminalArea() {
        return terminalArea;
    }

}
