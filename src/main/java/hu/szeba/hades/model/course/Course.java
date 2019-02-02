package hu.szeba.hades.model.course;

import hu.szeba.hades.model.topic.Topic;
import hu.szeba.hades.model.topic.TopicCollection;

import java.io.IOException;

public class Course {

    private TopicCollection topicCollection;

    // Language cannot change!
    private final String language;

    public Course(String courseName) {
        topicCollection = new TopicCollection(courseName);

        // TODO: Read from config file!
        language = "C";
    }

    public Topic loadCampaign(String campaignName) throws IOException {
        return topicCollection.loadTopic(campaignName, language);
    }

}
