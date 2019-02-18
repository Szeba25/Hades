package hu.szeba.hades.view.task;

import javax.swing.*;
import java.awt.*;

public class NewSourceFileForm extends JDialog {

    private JLabel label;
    private JTextArea fileName;

    public NewSourceFileForm() {
        super();

        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.setPreferredSize(new Dimension(320, 140));
        this.setLayout(new BorderLayout());
        this.setVisible(false);
        this.setModal(true);
        this.setTitle("Add new source file");

        label = new JLabel("New source file name:");

        this.getContentPane().add(label, BorderLayout.WEST);

        this.pack();
        this.setLocationRelativeTo(null);
    }

}
