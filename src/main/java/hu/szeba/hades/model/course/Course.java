package hu.szeba.hades.model.course;

import hu.szeba.hades.model.campaign.Campaign;
import hu.szeba.hades.model.campaign.CampaignCollection;
import hu.szeba.hades.model.task.languages.SupportedLanguages;

public class Course {

    private CampaignCollection campaignCollection;

    // Language cannot change!
    private final String language;

    public Course(String courseName) {
        campaignCollection = new CampaignCollection(courseName);

        // TODO: Read from config file!
        language = SupportedLanguages.C;
    }

    public Campaign loadCampaign(String campaignName) {
        return campaignCollection.loadCampaign(campaignName, language);
    }

}
