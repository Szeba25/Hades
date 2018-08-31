package hu.szeba.hades.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class CampaignSelector {

    private JFrame mainFrame;

    private JPanel leftPanel;

    private JList campaignList;
    private JScrollPane campaignListScroller;

    private JPanel rightPanel;

    private JTextArea descriptionArea;
    private JButton startButton;

    public CampaignSelector() {
        create();
        show();
    }

    private void create() {
        mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(true);
        mainFrame.setLayout(new BoxLayout(mainFrame.getContentPane(), BoxLayout.X_AXIS));
        mainFrame.setTitle("Hades: Campaign selection");

        leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        String[] campaigns = {"Hello world!", "Counting to 10"};
        campaignList = new JList(campaigns);
        campaignList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        campaignListScroller = new JScrollPane(campaignList);
        campaignListScroller.setPreferredSize(new Dimension(200, 500));
        campaignListScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        campaignListScroller.setBorder(BorderFactory.createEtchedBorder());

        leftPanel.add(campaignListScroller);

        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(new EmptyBorder(5, 0, 5, 5));

        descriptionArea = new JTextArea();
        descriptionArea.setPreferredSize(new Dimension(400, 500));
        descriptionArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionArea.setEditable(false);
        descriptionArea.setBorder(BorderFactory.createEtchedBorder());

        startButton = new JButton("Start");
        startButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        startButton.setFocusPainted(false);

        rightPanel.add(descriptionArea);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        rightPanel.add(startButton);

        mainFrame.getContentPane().add(leftPanel);
        mainFrame.getContentPane().add(rightPanel);
        mainFrame.pack();
    }

    private void show() {
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

}
