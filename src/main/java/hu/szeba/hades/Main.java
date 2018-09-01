package hu.szeba.hades;

import hu.szeba.hades.gui.CampaignSelector;
import hu.szeba.hades.meta.Options;

import javax.swing.*;
import java.io.File;

public class Main {

    private Options options;
    private CampaignSelector campaignSelector;

    private Main() {
        options = new Options(
                new File("D:/Egyetem/MinGW/bin"),
                new File("D:/Egyetem/Szakdolgozat/hades_Campaigns"),
                new File("D:/Egyetem/Szakdolgozat/hades_WorkingDirectory"));

        campaignSelector = new CampaignSelector();
        campaignSelector.show();

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

}
