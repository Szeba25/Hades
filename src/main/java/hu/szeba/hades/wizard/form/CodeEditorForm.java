package hu.szeba.hades.wizard.form;

import hu.szeba.hades.main.model.task.data.SourceFile;
import hu.szeba.hades.main.util.GridBagSetter;
import hu.szeba.hades.wizard.model.WizardTask;
import hu.szeba.hades.wizard.view.components.DynamicButtonListPanel;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class CodeEditorForm extends JDialog {

    private JPanel mainPanel;

    private DynamicButtonListPanel filesPanel;
    private RSyntaxTextArea textArea;

    private Map<String, SourceFile> data;

    public CodeEditorForm() {
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.setTitle("Edit shit");
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(1000, 650));
        this.setResizable(false);
        this.setModal(true);

        GridBagSetter gs = new GridBagSetter();

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        filesPanel = new DynamicButtonListPanel("Files", 200, "+", "-", "rename");

        textArea = new RSyntaxTextArea();
        textArea.setTabSize(4);
        textArea.setSyntaxEditingStyle(RSyntaxTextArea.SYNTAX_STYLE_C); // TODO!
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setAutoIndentEnabled(true);
        textArea.setCodeFoldingEnabled(true);
        textArea.setCurrentLineHighlightColor(new Color(10, 30, 140, 35));

        RTextScrollPane textAreaScroll = new RTextScrollPane(textArea);
        textAreaScroll.setLineNumbersEnabled(true);

        gs.setComponent(mainPanel);

        gs.add(filesPanel,
                0,
                0,
                GridBagConstraints.VERTICAL,
                1,
                1,
                0,
                1,
                new Insets(0, 0, 0, 0));

        gs.add(textAreaScroll,
                1,
                0,
                GridBagConstraints.BOTH,
                1,
                1,
                1,
                1,
                new Insets(5, 5, 5, 5));

        this.getContentPane().add(mainPanel, BorderLayout.CENTER);
        this.pack();
    }

    public void setData(Map<String, SourceFile> data) {
        this.data = data;
    }

}

