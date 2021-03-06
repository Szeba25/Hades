package hu.szeba.hades.wizard.form;

import hu.szeba.hades.main.io.EditableTextFile;
import hu.szeba.hades.main.meta.Languages;
import hu.szeba.hades.main.util.FileUtilities;
import hu.szeba.hades.main.util.GridBagSetter;
import hu.szeba.hades.main.util.SortUtilities;
import hu.szeba.hades.main.view.components.DialogFactory;
import hu.szeba.hades.main.view.elements.MappedElement;
import hu.szeba.hades.wizard.view.components.DynamicButtonListPanel;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CodeEditorForm extends JDialog {

    private JPanel mainPanel;

    private DynamicButtonListPanel filePanel;
    private JTextArea readonlySourcesEditor;
    private RSyntaxTextArea codeArea;
    private RTextScrollPane codeAreaScroll;

    private Map<String, EditableTextFile> files;

    private String lastSourceFile;
    private File filesPath;

    public CodeEditorForm() {
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.setTitle(Languages.translate("Edit files"));
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(1100, 700));
        this.setResizable(true);
        this.setModal(true);

        lastSourceFile = null;
        filesPath = null;

        initializeComponents();
        setupEvents();

        this.getContentPane().add(mainPanel, BorderLayout.CENTER);
        this.pack();
    }

    private void initializeComponents() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        filePanel = new DynamicButtonListPanel(Languages.translate("File list:"),
                200,
                "+", "-", Languages.translate("rename"));

        readonlySourcesEditor = new JTextArea();
        JScrollPane readonlySourcesEditorScroll = new JScrollPane(readonlySourcesEditor);

        codeArea = new RSyntaxTextArea();
        codeArea.setTabSize(4);
        codeArea.setSyntaxEditingStyle(RSyntaxTextArea.SYNTAX_STYLE_C);
        codeArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        codeArea.setAutoIndentEnabled(true);
        codeArea.setCurrentLineHighlightColor(new Color(10, 30, 140, 35));
        codeArea.setEnabled(false);

        codeAreaScroll = new RTextScrollPane(codeArea);
        codeAreaScroll.setLineNumbersEnabled(true);

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

        gs.add(new JLabel(Languages.translate("Readonly file name list:")),
                0,
                1,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                0,
                new Insets(0, 5, 0, 0));

        gs.add(readonlySourcesEditorScroll,
                0,
                2,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                1,
                new Insets(5, 5, 5, 5));

        gs.add(codeAreaScroll,
                1,
                0,
                GridBagConstraints.BOTH,
                1,
                3,
                1,
                1,
                new Insets(5, 5, 5, 5));
    }

    private void setupEvents() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if (lastSourceFile != null) {
                    files.get(lastSourceFile).setData(codeArea.getText());
                }
                CodeEditorForm.this.setVisible(false);
            }
        });

        filePanel.getList().getSelectionModel().addListSelectionListener((event) ->  {
            ListSelectionModel listSelectionModel = (ListSelectionModel) event.getSource();
            ListModel listModel = filePanel.getList().getModel();
            if (!listSelectionModel.isSelectionEmpty()) {
                int idx = listSelectionModel.getMinSelectionIndex();
                if (listSelectionModel.isSelectedIndex(idx)) {
                    MappedElement value = (MappedElement) listModel.getElementAt(idx);
                    if (lastSourceFile != null) {
                        files.get(lastSourceFile).setData(codeArea.getText());
                    }
                    codeArea.setEnabled(true);
                    codeArea.setText(files.get(value.getId()).getData());
                    codeArea.setCaretPosition(0);
                    lastSourceFile = value.getId();
                }
            }
        });

        filePanel.getModifier().getButton(0).addActionListener((event) -> {
            // Add

            String name = DialogFactory.showCustomInputDialog(
                    "",
                    Languages.translate("New file name:"),
                    Languages.translate("Add new file"),
                    Languages.translate("Ok"),
                    Languages.translate("Cancel"));

            if (files.containsKey(name)) {

                DialogFactory.showCustomWarning(
                        Languages.translate("The specified file already exists:") + name,
                        Languages.translate("File exists"));

            } else if (name != null) {
                try {
                    // Test for invalid file names
                    if (FileUtilities.validFileName(name)) {
                        // Proceed
                        files.put(name, new EditableTextFile(new File(filesPath, name), false));
                        DefaultListModel<MappedElement> model = (DefaultListModel<MappedElement>) filePanel.getList().getModel();
                        model.addElement(new MappedElement(name, name));

                        // Sort the list, and select the old value!
                        MappedElement oldElement = filePanel.getList().getSelectedValue();
                        sortFileList();
                        if (oldElement != null) {
                            filePanel.getList().setSelectedValue(oldElement, true);
                        }
                    } else {
                        DialogFactory.showCustomWarning(
                                Languages.translate("Invalid file name:") + name,
                                Languages.translate("Filename invalid"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        filePanel.getModifier().getButton(1).addActionListener((event) -> {
            // Delete
            MappedElement selected = filePanel.getList().getSelectedValue();
            if (selected == null) {
                DialogFactory.showCustomWarning(
                        Languages.translate("Please select a file from the list!"),
                        Languages.translate("No file selected"));
            } else {

                int result = DialogFactory.showCustomChoiceDialog(
                        Languages.translate("Delete selected source file?"),
                        Languages.translate("Delete source..."),
                        Languages.translate("Yes"),
                        Languages.translate("No"));

                if (result == JOptionPane.YES_OPTION) {
                    files.remove(selected.getId());
                    DefaultListModel<MappedElement> model = (DefaultListModel<MappedElement>) filePanel.getList().getModel();
                    model.removeElement(selected);

                    // Reset GUI
                    lastSourceFile = null;
                    resetListSelection();
                }
            }
        });

        filePanel.getModifier().getButton(2).addActionListener((event) -> {
            // Rename
            MappedElement selected = filePanel.getList().getSelectedValue();
            if (selected == null) {
                DialogFactory.showCustomWarning(
                        Languages.translate("Please select a file from the list!"),
                        Languages.translate("No file selected"));
            } else {

                String newName = DialogFactory.showCustomInputDialog(selected.getId(),
                        Languages.translate("Rename selected source file:"),
                        Languages.translate("Rename source..."),
                        Languages.translate("Ok"),
                        Languages.translate("Cancel"));

                if (files.containsKey(newName)) {

                    DialogFactory.showCustomWarning(
                            Languages.translate("The specified file already exists:"),
                            Languages.translate("File exists"));

                } else if (newName != null) {
                    try {
                        // Test for invalid file names!
                        if (FileUtilities.validFileName(newName)) {
                            // Proceed
                            EditableTextFile src = files.remove(selected.getId());
                            src.rename(newName, false);
                            files.put(newName, src);

                            // Set in list data
                            selected.setId(newName);
                            selected.setTitle(newName);

                            // Last source is changing too!
                            lastSourceFile = newName;

                            // Sort list, and select the value there
                            sortFileList();
                            filePanel.getList().setSelectedValue(selected, true);

                            // Repaint list
                            filePanel.getList().repaint();
                        } else {

                            DialogFactory.showCustomWarning(
                                    Languages.translate("Invalid file name:") + newName,
                                    Languages.translate("Filename invalid"));

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void setFiles(Map<String, EditableTextFile> files, File filesPath, String readonlySourcesData) {
        lastSourceFile = null;

        this.files = files;
        this.filesPath = filesPath;

        if (readonlySourcesData == null) {
            readonlySourcesEditor.setEnabled(false);
        } else {
            readonlySourcesEditor.setEnabled(true);
            readonlySourcesEditor.setText(readonlySourcesData);
        }

        DefaultListModel<MappedElement> model = (DefaultListModel<MappedElement>) filePanel.getList().getModel();
        model.removeAllElements();
        for (EditableTextFile src : files.values()) {
            model.addElement(new MappedElement(src.getName(), src.getName()));
        }

        sortFileList();
        resetListSelection();
        if (files.size() > 0) {
            filePanel.getList().setSelectedIndex(0);
            lastSourceFile = filePanel.getList().getSelectedValue().getId();
        }
    }

    public String getReadonlySourcesData() {
        return readonlySourcesEditor.getText();
    }

    private void resetListSelection() {
        codeArea.setEnabled(false);
        codeArea.setText("");
        filePanel.getList().setSelectedIndex(-1);
    }

    private void sortFileList() {
        DefaultListModel<MappedElement> model = (DefaultListModel<MappedElement>) filePanel.getList().getModel();
        List<MappedElement> list = Collections.list(model.elements());
        list.sort(SortUtilities::mappedElementStringComparator);
        model.clear();
        for (MappedElement added : list) {
            model.addElement(added);
        }
    }

}

