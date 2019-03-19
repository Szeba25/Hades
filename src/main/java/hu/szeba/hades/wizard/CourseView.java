package hu.szeba.hades.wizard;

import hu.szeba.hades.util.GridBagSetter;
import hu.szeba.hades.view.ViewableFrame;

import javax.swing.*;
import java.awt.*;

public class CourseView extends JFrame implements ViewableFrame {

    private JPanel mainPanel;

    private JList<DescriptiveElement> courseList;
    private JPanel rightPanel;
    private JButton newButton;
    private JButton editButton;
    private JButton deleteButton;

    public CourseView() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Wizard: Courses");
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(380, 500));
        this.setResizable(false);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        courseList = new JList<>();
        courseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        courseList.setFixedCellWidth(200);
        JScrollPane courseListScroller = new JScrollPane(courseList);
        courseListScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        rightPanel = new JPanel();
        rightPanel.setBorder(BorderFactory.createEtchedBorder());
        rightPanel.setLayout(new GridBagLayout());

        newButton = new JButton("New course");
        newButton.setFocusPainted(false);
        editButton = new JButton("Edit course");
        editButton.setFocusPainted(false);
        deleteButton = new JButton("Delete course");
        deleteButton.setFocusPainted(false);

        GridBagSetter gs = new GridBagSetter();

        gs.setComponent(rightPanel);

        gs.add(newButton,
                0,
                0,
                GridBagConstraints.HORIZONTAL,
                1,
                1,
                0,
                0,
                new Insets(0, 0, 10, 0));

        gs.add(editButton,
                0,
                1,
                GridBagConstraints.HORIZONTAL,
                1,
                1,
                0,
                0,
                new Insets(0, 0, 10, 0));

        gs.add(deleteButton,
                0,
                2,
                GridBagConstraints.HORIZONTAL,
                1,
                1,
                0,
                0,
                new Insets(0, 0, 10, 0));

        gs.setComponent(mainPanel);

        gs.add(courseListScroller,
                0,
                0,
                GridBagConstraints.VERTICAL,
                1,
                1,
                0,
                1,
                new Insets(5, 5, 5, 5));

        gs.add(rightPanel,
                1,
                0,
                GridBagConstraints.BOTH,
                1,
                1,
                1,
                1,
                new Insets(5, 0, 5, 5));

        this.getContentPane().add(mainPanel, BorderLayout.CENTER);
        this.pack();
    }

    @Override
    public void showView() {
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.requestFocus();
    }

    @Override
    public void showViewMaximized() {
        this.showView();
    }

    @Override
    public void hideView() {
        this.setVisible(false);
    }

}
