package hu.szeba.hades;

import hu.szeba.hades.control.task.TaskSelectorControl;
import hu.szeba.hades.gui.task.TaskSelectorView;
import hu.szeba.hades.meta.Options;
import hu.szeba.hades.model.campaign.Campaign;
import hu.szeba.hades.model.campaign.CampaignDatabase;

import javax.swing.*;
import java.io.File;

public class Main {

    private Options options;
    private CampaignDatabase campaignDatabase;
    private Campaign campaign;
    private TaskSelectorControl taskSelectorControl;
    private TaskSelectorView taskSelectorView;

    private Main() {
        options = new Options(
                new File("D:/Egyetem/MinGW/bin"),
                new File("D:/Egyetem/Szakdolgozat/hades_Campaigns"),
                new File("D:/Egyetem/Szakdolgozat/hades_WorkingDirectory"));

        campaignDatabase = new CampaignDatabase(options);

        campaign = campaignDatabase.loadCampaign("practice");

        taskSelectorControl = new TaskSelectorControl(campaign);
        taskSelectorView = new TaskSelectorView(taskSelectorControl);

        showTaskSelector();
    }

    private void showTaskSelector() {
        taskSelectorView.show();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

}
