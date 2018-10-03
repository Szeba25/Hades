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

public class TaskSolvingView extends BaseView {

    private BaseView parentView;

    private TaskSolvingController taskSolvingController;

    private RSyntaxTextArea codeArea;
    private RTextScrollPane codeScroll;

    private JMenuBar menuBar;

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
        codeArea.setAutoIndentEnabled(true);
        codeArea.setCodeFoldingEnabled(true);
        codeArea.setSyntaxEditingStyle(RSyntaxTextArea.SYNTAX_STYLE_C);
        codeArea.setCurrentLineHighlightColor(new Color(10, 30, 140, 50));
        codeArea.setFont(new Font("Consolas", Font.PLAIN, 14));

        codeScroll = new RTextScrollPane(codeArea);
        codeScroll.setLineNumbersEnabled(true);
        codeScroll.setPreferredSize(new Dimension(1080, 600));

        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(new JMenuItem("Open"));
        JMenu buildMenu = new JMenu("Build");
        JMenu helpMenu = new JMenu("Help");
        menuBar.add(fileMenu);
        menuBar.add(buildMenu);
        menuBar.add(helpMenu);

        this.getContentPane().add(codeScroll, BorderLayout.CENTER);
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
    }

}
