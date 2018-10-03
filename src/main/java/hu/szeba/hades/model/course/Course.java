package hu.szeba.hades.model.course;

import hu.szeba.hades.model.campaign.Campaign;
import hu.szeba.hades.model.campaign.CampaignCollection;

public class Course {

    private CampaignCollection campaignCollection;

    // Language cannot change!
    private final String language;

    public Course(String courseName) {
        campaignCollection = new CampaignCollection(courseName);

        // TODO: Read from config file!
        language = "C";
    }

    public Campaign loadCampaign(String campaignName) {
        return campaignCollection.loadCampaign(campaignName, language);
    }

}
