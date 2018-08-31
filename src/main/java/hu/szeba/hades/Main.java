package hu.szeba.hades;

import hu.szeba.hades.gui.CampaignSelector;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CampaignSelector::new);
    }

}
