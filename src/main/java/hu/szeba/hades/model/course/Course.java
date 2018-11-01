package hu.szeba.hades.model.course;

import hu.szeba.hades.model.campaign.Campaign;
import hu.szeba.hades.model.campaign.CampaignCollection;

import java.io.IOException;

public class Course {

    private CampaignCollection campaignCollection;

    // Language cannot change!
    private final String language;

    public Course(String courseName) {
        campaignCollection = new CampaignCollection(courseName);

        // TODO: Read from config file!
        language = "C";
    }

    public Campaign loadCampaign(String campaignName) throws IOException {
        return campaignCollection.loadCampaign(campaignName, language);
    }

}
