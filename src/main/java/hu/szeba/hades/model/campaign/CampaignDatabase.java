package hu.szeba.hades.model.campaign;

import hu.szeba.hades.meta.Options;

import java.util.HashMap;
import java.util.Map;

public class CampaignDatabase {

    private Options options;
    private Map<String, Campaign> campaigns;

    public CampaignDatabase(Options options) {
        this.options = options;
        campaigns = new HashMap<>();
    }

    public Campaign loadCampaign(String campaignName) {
        if (campaigns.containsKey(campaignName)) {
            return campaigns.get(campaignName);
        } else {
            Campaign newCampaign = new Campaign(options.getCampaignDatabasePath(), campaignName);
            campaigns.put(campaignName, newCampaign);
            return newCampaign;
        }
    }

}
