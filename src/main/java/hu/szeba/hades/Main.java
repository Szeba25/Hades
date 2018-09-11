package hu.szeba.hades;

import hu.szeba.hades.control.campaign.TaskSelectorControl;
import hu.szeba.hades.gui.campaign.TaskSelectorView;
import hu.szeba.hades.meta.Options;
import hu.szeba.hades.model.campaign.Campaign;
import hu.szeba.hades.model.campaign.CampaignDatabase;

import javax.swing.*;

public class Main {

    private CampaignDatabase campaignDatabase;
    private Campaign campaign;
    private TaskSelectorControl taskSelectorControl;
    private TaskSelectorView taskSelectorView;

    private Main() {
        Options.initialize();
        campaignDatabase = new CampaignDatabase();
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
