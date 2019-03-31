package hu.szeba.hades.wizard.form;

import hu.szeba.hades.main.controller.SourceUpdaterForClosableTabs;
import hu.szeba.hades.main.model.task.data.SourceFile;
import hu.szeba.hades.main.util.GridBagSetter;
import hu.szeba.hades.main.view.TaskSolvingView;
import hu.szeba.hades.main.view.components.ClosableTabComponent;
import hu.szeba.hades.main.view.elements.MappedElement;
import hu.szeba.hades.wizard.view.components.DynamicButtonListPanel;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

public class CodeEditorForm extends JDialog implements SourceUpdaterForClosableTabs {

    private JPanel mainPanel;

    private DynamicButtonListPanel filePanel;

    private JTabbedPane codeTab;
    private Map<String, JTextArea> codeTabByName;

    private List<SourceFile> files;

    public CodeEditorForm() {
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.setTitle("Edit files");
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(1000, 650));
        this.setResizable(true);
        this.setModal(true);

        initializeComponents();
        setupEvents();

        this.getContentPane().add(mainPanel, BorderLayout.CENTER);
        this.pack();
    }

    private void initializeComponents() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        filePanel = new DynamicButtonListPanel("Files", 200, "+", "-", "rename");

        codeTab = new JTabbedPane();
        codeTabByName = new HashMap<>();

        GridBagSetter gs = new GridBagSetter();
        gs.setComponent(mainPanel);

        gs.add(filePanel,
                0,
                0,
                GridBagConstraints.VERTICAL,
                1,
                1,
                0,
                1,
                new Insets(0, 0, 0, 0));

        gs.add(codeTab,
                1,
                0,
                GridBagConstraints.BOTH,
                1,
                1,
                1,
                1,
                new Insets(5, 5, 5, 5));
    }

    private void setupEvents() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                for (SourceFile src : files) {
                    src.setData(codeTabByName.get(src.getName()).getText());
                }
                super.windowClosing(e);
                CodeEditorForm.this.setVisible(false);
            }
        });

        // Switching and opening tabs with list
        filePanel.getList().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JList list = (JList) e.getSource();
                    boolean found = false;
                    String value = ((MappedElement) list.getSelectedValue()).getId();
                    for (int i = 0; i < codeTab.getTabCount(); i++) {
                        String title = codeTab.getTitleAt(i);
                        if (title.equals(value)) {
                            codeTab.setSelectedIndex(i);
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        addCodeArea(value, RSyntaxTextArea.SYNTAX_STYLE_C);
                        codeTabByName.get(value).setText(getSourceFileByName(value).getData());
                        // Select the opened tab
                        codeTab.setSelectedIndex(codeTab.getTabCount() - 1);
                    }
                }
            }
        });
    }

    private void addCodeArea(String name, String syntaxStyle) {
        RSyntaxTextArea codeTabArea = new RSyntaxTextArea();
        codeTabArea.setTabSize(4);
        codeTabArea.setSyntaxEditingStyle(syntaxStyle);
        codeTabArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        codeTabArea.setAutoIndentEnabled(true);
        codeTabArea.setCodeFoldingEnabled(true);
        codeTabArea.setCurrentLineHighlightColor(new Color(10, 30, 140, 35));

        RTextScrollPane codeTabScroll = new RTextScrollPane(codeTabArea);
        codeTabScroll.setLineNumbersEnabled(true);

        codeTab.add(name, codeTabScroll);
        codeTab.setTabComponentAt(codeTab.getTabCount()-1, new ClosableTabComponent(codeTab, this));

        codeTabByName.put(name, codeTabArea);
    }

    private void removeAllCodeArea() {
        codeTab.removeAll();
        codeTabByName.clear();
    }

    public void setFiles(List<SourceFile> files) {
        this.files = files;

        removeAllCodeArea();

        DefaultListModel<MappedElement> model = (DefaultListModel<MappedElement>) filePanel.getList().getModel();
        model.removeAllElements();

        for (SourceFile src : files) {
            model.addElement(new MappedElement(src.getName(), src.getName()));
            addCodeArea(src.getName(), RSyntaxTextArea.SYNTAX_STYLE_C);
            codeTabByName.get(src.getName()).setText(src.getData());
        }
    }

    private SourceFile getSourceFileByName(String name) {
        for (SourceFile src : files) {
            if (src.getName().equals(name)) {
                return src;
            }
        }
        return null;
    }

    @Override
    public void updateSourceFileData(String name, JTextArea codeArea) {
        getSourceFileByName(name).setData(codeArea.getText());
    }

}

