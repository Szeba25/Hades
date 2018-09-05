package hu.szeba.hades.model.campaign;

import java.io.File;

public class CampaignFactory {

    public static Campaign createCampaign(File campaignDatabasePath, String campaignName) {
        return new Campaign(campaignDatabasePath, campaignName);
    }

}
