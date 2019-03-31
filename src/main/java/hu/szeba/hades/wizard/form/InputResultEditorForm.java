package hu.szeba.hades.wizard.form;

import hu.szeba.hades.main.util.GridBagSetter;

import javax.swing.*;
import java.awt.*;

public class InputResultEditorForm extends JDialog {

    private JTextField nameField;
    private JPanel topPanel;
    private JPanel mainPanel;
    private JTextArea inputArea;
    private JTextArea resultArea;

    public InputResultEditorForm() {
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.setTitle("Edit input/result pairs");
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(640, 480));
        this.setResizable(false);
        this.setModal(true);

        GridBagSetter gs = new GridBagSetter();

        topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());

        nameField = new JTextField();
        JLabel nameFieldLabel = new JLabel("Name:");
        nameFieldLabel.setLabelFor(nameField);

        gs.setComponent(topPanel);

        gs.add(nameFieldLabel,
                0,
                0,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                0,
                new Insets(5, 5, 5, 5));

        gs.add(nameField,
                1,
                0,
                GridBagConstraints.HORIZONTAL,
                1,
                1,
                1,
                0,
                new Insets(5, 0, 5, 5));

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        inputArea = new JTextArea();
        JScrollPane inputAreaScroll = new JScrollPane(inputArea);
        inputAreaScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        resultArea = new JTextArea();
        JScrollPane resultAreaScroll = new JScrollPane(resultArea);
        resultAreaScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        gs.setComponent(mainPanel);

        gs.add(new JLabel("Input:"),
                0,
                0,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                0,
                new Insets(10, 5, 0, 0));

        gs.add(new JLabel("Result:"),
                2,
                0,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                0,
                new Insets(10, 5, 0, 0));

        gs.add(inputAreaScroll,
                0,
                1,
                GridBagConstraints.BOTH,
                1,
                1,
                0.5,
                1,
                new Insets(0, 5, 5, 5));

        gs.add(new JLabel("=>"),
                1,
                1,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                1,
                new Insets(0, 5, 0, 5));

        gs.add(resultAreaScroll,
                2,
                1,
                GridBagConstraints.BOTH,
                1,
                1,
                0.5,
                1,
                new Insets(0, 5, 5, 5));

        this.getContentPane().add(topPanel, BorderLayout.NORTH);
        this.getContentPane().add(mainPanel, BorderLayout.CENTER);
        this.pack();
    }

    public void setContents(String name, String inputFileData, String resultFileData) {
        nameField.setText(name);
        inputArea.setText(inputFileData);
        resultArea.setText(resultFileData);
    }

    public String getInputFileData() {
        return inputArea.getText();
    }

    public String getResultFileData() {
        return resultArea.getText();
    }

    public String getNewName() {
        return nameField.getText();
    }

}
