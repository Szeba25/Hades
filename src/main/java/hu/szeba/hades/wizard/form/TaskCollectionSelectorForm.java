package hu.szeba.hades.wizard.form;

import hu.szeba.hades.util.GridBagSetter;
import hu.szeba.hades.wizard.elements.DescriptiveElement;

import javax.swing.*;
import java.awt.*;

public class TaskCollectionSelectorForm extends JDialog {

    private JPanel mainPanel;
    private JList<DescriptiveElement> taskCollectionList;
    private JButton okButton;

    public TaskCollectionSelectorForm() {
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setTitle("Wizard: Select task collections");
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(300, 500));
        this.setResizable(false);
        this.setModal(true);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        taskCollectionList = new JList<>();
        taskCollectionList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane taskCollectionListScroll = new JScrollPane(taskCollectionList);

        okButton = new JButton("Select");
        okButton.setFocusPainted(false);

        GridBagSetter gs = new GridBagSetter();

        gs.setComponent(mainPanel);

        gs.add(taskCollectionListScroll,
                0,
                0,
                GridBagConstraints.BOTH,
                1,
                1,
                1,
                1,
                new Insets(5, 5, 5, 5));

        gs.add(okButton,
                0,
                1,
                GridBagConstraints.NONE,
                1,
                1,
                0,
                0,
                new Insets(0, 5, 5, 5));

        this.getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

}
