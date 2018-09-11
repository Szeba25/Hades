package hu.szeba.hades;

import hu.szeba.hades.controller.campaign.TaskSelectorController;
import hu.szeba.hades.view.campaign.TaskSelectorView;
import hu.szeba.hades.meta.Options;
import hu.szeba.hades.model.campaign.Campaign;
import hu.szeba.hades.model.campaign.CampaignDatabase;

import javax.swing.*;

public class Main {

    private CampaignDatabase campaignDatabase;
    private Campaign campaign;
    private TaskSelectorView taskSelectorView;
    private TaskSelectorController taskSelectorController;

    private Main() {
        Options.initialize();

        campaignDatabase = new CampaignDatabase();
        campaign = campaignDatabase.loadCampaign("practice");

        taskSelectorView = new TaskSelectorView();
        taskSelectorController = new TaskSelectorController(taskSelectorView, campaign);
        taskSelectorView.registerController(taskSelectorController);

        start();
    }

    private void start() {
        taskSelectorView.showView();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

}
