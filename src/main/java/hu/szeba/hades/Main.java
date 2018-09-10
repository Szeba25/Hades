package hu.szeba.hades;

import hu.szeba.hades.gui.task.TaskSelectorView;
import hu.szeba.hades.meta.Options;
import hu.szeba.hades.model.campaign.Campaign;
import hu.szeba.hades.model.campaign.CampaignDatabase;

import javax.swing.*;

public class Main {

    private CampaignDatabase campaignDatabase;
    private Campaign campaign;
    private TaskSelectorView taskSelectorView;

    private Main() {
        Options.initialize();
        campaignDatabase = new CampaignDatabase();
        campaign = campaignDatabase.loadCampaign("practice");
        taskSelectorView = new TaskSelectorView(campaign);
        showTaskSelector();
    }

    private void showTaskSelector() {
        taskSelectorView.show();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

}
