package hu.szeba.hades.gui.task;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;

public class TaskSolvingView {

    private JFrame mainFrame;

    private RSyntaxTextArea codeArea;
    private RTextScrollPane codeScroll;

    private JMenuBar menuBar;

    public TaskSolvingView() {}

    private void create() {
        mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

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

        mainFrame.getContentPane().add(codeScroll, BorderLayout.CENTER);
        mainFrame.getContentPane().add(menuBar, BorderLayout.NORTH);
        mainFrame.pack();
    }

    private void show() {
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

}
