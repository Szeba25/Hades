package hu.szeba.hades.wizard.form;

import hu.szeba.hades.main.util.GridBagSetter;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;

public class HTMLDescriptionsEditorForm extends JDialog {

    private JPanel mainPanel;

    private RSyntaxTextArea shortStoryArea;
    private RSyntaxTextArea storyArea;
    private RSyntaxTextArea shortInstructionsArea;
    private RSyntaxTextArea instructionsArea;

    public HTMLDescriptionsEditorForm() {
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.setTitle("Edit HTML descriptions");
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(1100, 680));
        this.setResizable(true);
        this.setModal(true);

        initializeComponents();

        this.getContentPane().add(mainPanel, BorderLayout.CENTER);
        this.pack();
    }

    private RSyntaxTextArea createHTMLEditor() {
        RSyntaxTextArea editor = new RSyntaxTextArea();
        editor.setTabSize(4);
        editor.setSyntaxEditingStyle(RSyntaxTextArea.SYNTAX_STYLE_HTML);
        editor.setFont(new Font("Monospaced", Font.PLAIN, 12));
        editor.setCodeFoldingEnabled(true);
        editor.setAutoIndentEnabled(true);
        editor.setCurrentLineHighlightColor(new Color(10, 30, 140, 35));
        return editor;
    }

    private JPanel createPanelWithLabel(RTextScrollPane scrollPane, String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagSetter gs = new GridBagSetter();
        gs.setComponent(panel);

        gs.add(new JLabel(title),
                0,
                0,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                0,
                new Insets(5, 5, 5, 0));

        gs.add(scrollPane,
                0,
                1,
                GridBagConstraints.BOTH,
                1,
                1,
                1,
                1,
                new Insets(0, 0, 0, 0));

        return panel;
    }

    private void initializeComponents() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        shortStoryArea = createHTMLEditor();
        RTextScrollPane shortStoryAreaScroll = new RTextScrollPane(shortStoryArea);
        shortStoryAreaScroll.setLineNumbersEnabled(true);
        shortStoryAreaScroll.setPreferredSize(new Dimension(0, 250));

        storyArea = createHTMLEditor();
        RTextScrollPane storyAreaScroll = new RTextScrollPane(storyArea);
        storyAreaScroll.setLineNumbersEnabled(true);

        shortInstructionsArea = createHTMLEditor();
        RTextScrollPane shortInstructionsAreaScroll = new RTextScrollPane(shortInstructionsArea);
        shortInstructionsAreaScroll.setLineNumbersEnabled(true);
        shortInstructionsAreaScroll.setPreferredSize(new Dimension(0, 250));

        instructionsArea = createHTMLEditor();
        RTextScrollPane instructionsAreaScroll = new RTextScrollPane(instructionsArea);
        instructionsAreaScroll.setLineNumbersEnabled(true);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.add(createPanelWithLabel(shortStoryAreaScroll, "Short story:"), BorderLayout.NORTH);
        leftPanel.add(createPanelWithLabel(storyAreaScroll, "Story:"), BorderLayout.CENTER);
        leftPanel.setPreferredSize(new Dimension(450, 0));

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(createPanelWithLabel(shortInstructionsAreaScroll, "Short instructions:"), BorderLayout.NORTH);
        rightPanel.add(createPanelWithLabel(instructionsAreaScroll, "Instructions:"), BorderLayout.CENTER);

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
    }

    public void setup(String shortStory, String story, String shortInstructions, String instructions) {
        shortStoryArea.setText(shortStory);
        storyArea.setText(story);
        shortInstructionsArea.setText(shortInstructions);
        instructionsArea.setText(instructions);
    }

    public String getShortStory() {
        return shortStoryArea.getText();
    }

    public String getStory() {
        return storyArea.getText();
    }

    public String getShortInstructions() {
        return shortInstructionsArea.getText();
    }

    public String getInstructions() {
        return instructionsArea.getText();
    }

}
