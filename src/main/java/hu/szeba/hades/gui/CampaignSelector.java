package hu.szeba.hades.gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CampaignSelector {

    private JFrame mainFrame;

    private JPanel rightPanel;

    private JList campaignList;
    private JScrollPane campaignListScroller;

    private JTextArea descriptionArea;
    private JButton startButton;

    public CampaignSelector() {
        create();
        show();
    }

    private void create() {
        mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);
        mainFrame.setLayout(new BorderLayout());

        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(new EmptyBorder(5, 0, 5, 5));

        String[] campaigns = {"Hello world!", "Counting to 10"};
        campaignList = new JList(campaigns);
        campaignListScroller = new JScrollPane(campaignList);
        campaignListScroller.setPreferredSize(new Dimension(300, 500));
        campaignListScroller.setBorder(new EmptyBorder(5, 5, 5, 5));

        descriptionArea = new JTextArea();
        descriptionArea.setPreferredSize(new Dimension(500, 400));
        descriptionArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionArea.setEditable(false);

        startButton = new JButton();
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setText("Start");
        startButton.setMinimumSize(new Dimension(150, 30));

        rightPanel.add(descriptionArea);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        rightPanel.add(startButton);

        mainFrame.getContentPane().add(campaignListScroller, BorderLayout.WEST);
        mainFrame.getContentPane().add(rightPanel, BorderLayout.EAST);
        mainFrame.pack();
    }

    private void show() {
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

}
