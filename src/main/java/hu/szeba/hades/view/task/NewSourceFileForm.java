package hu.szeba.hades.view.task;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class NewSourceFileForm extends JDialog {

    private NewSourceFileListener listener;
    private JLabel label;
    private JTextField fileName;
    private JButton addButton;

    public NewSourceFileForm(NewSourceFileListener listener) {
        super();

        this.listener = listener;

        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.setPreferredSize(new Dimension(250, 100));
        this.setLayout(new BorderLayout());
        this.setVisible(false);
        this.setModal(true);
        this.setTitle("Add new source file");
        this.setResizable(false);

        label = new JLabel("New source file name:");
        fileName = new JTextField();
        addButton = new JButton("Add");

        addButton.addActionListener((event) -> {
            try {
                listener.addNewSourceFileTrigger(fileName.getText());
                this.setVisible(false);
                this.fileName.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.getContentPane().add(label, BorderLayout.NORTH);
        this.getContentPane().add(fileName, BorderLayout.CENTER);
        this.getContentPane().add(addButton, BorderLayout.SOUTH);

        this.pack();
        this.setLocationRelativeTo(null);
    }

}
