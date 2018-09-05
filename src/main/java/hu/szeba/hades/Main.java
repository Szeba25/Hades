package hu.szeba.hades;

import hu.szeba.hades.gui.task.TaskSelector;
import hu.szeba.hades.meta.Options;
import hu.szeba.hades.model.campaign.Campaign;
import hu.szeba.hades.model.campaign.CampaignFactory;

import javax.swing.*;
import java.io.File;

public class Main {

    private Options options;
    private Campaign campaign;
    private TaskSelector taskSelector;

    private Main() {
        options = new Options(
                new File("D:/Egyetem/MinGW/bin"),
                new File("D:/Egyetem/Szakdolgozat/hades_Campaigns"),
                new File("D:/Egyetem/Szakdolgozat/hades_WorkingDirectory"));
        campaign = CampaignFactory.createCampaign(options.getCampaignDatabasePath(), "practice");
        taskSelector = new TaskSelector(campaign);
        showTaskSelector();
    }

    private void showTaskSelector() {
        taskSelector.show();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

}
