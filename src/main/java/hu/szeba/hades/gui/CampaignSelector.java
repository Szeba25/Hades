package hu.szeba.hades.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CampaignSelector {

    private JFrame mainFrame;

    private JPanel leftPanel;
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
        mainFrame.setResizable(true);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setMinimumSize(new Dimension(640, 480));
        mainFrame.setTitle("Hades: Campaign selection");

        leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(new EmptyBorder(5, 0, 5, 5));

        String[] campaigns = {"Hello world!", "Counting to 10", "Fibonacci"};
        campaignList = new JList(campaigns);
        campaignList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        campaignList.setFixedCellWidth(200);

        campaignList.addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                System.out.println(campaignList.getSelectedIndex());
            }
        });

        campaignListScroller = new JScrollPane(campaignList);
        campaignListScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        campaignListScroller.setBorder(BorderFactory.createEtchedBorder());

        descriptionArea = new JTextArea();
        descriptionArea.setAlignmentX(Component.RIGHT_ALIGNMENT);
        descriptionArea.setEditable(false);
        descriptionArea.setBorder(BorderFactory.createEtchedBorder());

        startButton = new JButton("Start");
        startButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        startButton.setFocusPainted(false);
        startButton.setMaximumSize(new Dimension(120, 30));

        leftPanel.add(campaignListScroller);

        rightPanel.add(descriptionArea);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        rightPanel.add(startButton);

        mainFrame.getContentPane().add(leftPanel, BorderLayout.WEST);
        mainFrame.getContentPane().add(rightPanel, BorderLayout.CENTER);
        mainFrame.pack();
    }

    private void show() {
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

}
