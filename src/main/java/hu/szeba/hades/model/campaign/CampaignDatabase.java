package hu.szeba.hades.model.campaign;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CampaignDatabase {

    private File campaignDatabasePath;
    private Map<String, Campaign> campaigns;

    public CampaignDatabase(File campaignDatabasePath) {
        this.campaignDatabasePath = campaignDatabasePath;
        campaigns = new HashMap<>();
    }

    public Campaign loadCampaign(File campaignDatabasePath, String campaignName) {
        if (campaigns.containsKey(campaignName)) {
            return campaigns.get(campaignName);
        } else {
            Campaign newCampaign = new Campaign(campaignDatabasePath, campaignName);
            campaigns.put(campaignName, newCampaign);
            return newCampaign;
        }
    }

}
