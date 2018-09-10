package hu.szeba.hades.model.campaign;

import java.util.HashMap;
import java.util.Map;

public class CampaignDatabase {

    private Map<String, Campaign> campaigns;

    public CampaignDatabase() {
        campaigns = new HashMap<>();
    }

    public Campaign loadCampaign(String campaignName) {
        if (campaigns.containsKey(campaignName)) {
            return campaigns.get(campaignName);
        } else {
            Campaign newCampaign = new Campaign(campaignName);
            campaigns.put(campaignName, newCampaign);
            return newCampaign;
        }
    }

}
