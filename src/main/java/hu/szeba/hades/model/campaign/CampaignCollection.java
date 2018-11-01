package hu.szeba.hades.model.campaign;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CampaignCollection {

    private String courseName;
    private Map<String, Campaign> campaigns;

    public CampaignCollection(String courseName) {
        this.courseName = courseName;
        campaigns = new HashMap<>();
    }

    public Campaign loadCampaign(String campaignName, String language) throws IOException {
        if (campaigns.containsKey(campaignName)) {
            return campaigns.get(campaignName);
        } else {
            Campaign newCampaign = new Campaign(courseName, campaignName, language);
            campaigns.put(campaignName, newCampaign);
            return newCampaign;
        }
    }

}
