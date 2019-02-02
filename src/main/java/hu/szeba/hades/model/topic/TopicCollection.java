package hu.szeba.hades.model.topic;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TopicCollection {

    private String courseName;
    private Map<String, Topic> topics;

    public TopicCollection(String courseName) {
        this.courseName = courseName;
        topics = new HashMap<>();
    }

    public Topic loadTopic(String topicName, String language) throws IOException, ParserConfigurationException, SAXException {
        if (topics.containsKey(topicName)) {
            return topics.get(topicName);
        } else {
            Topic newTopic = new Topic(courseName, topicName, language);
            topics.put(topicName, newTopic);
            return newTopic;
        }
    }

}
