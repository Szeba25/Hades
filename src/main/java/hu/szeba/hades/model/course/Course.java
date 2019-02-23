package hu.szeba.hades.model.course;

import hu.szeba.hades.meta.User;
import hu.szeba.hades.model.topic.Topic;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Course {

    private User user;
    private String courseName;
    private Map<String, Topic> topics;

    // Language cannot change!
    private final String language;

    public Course(User user, String courseName) {
        this.user = user;
        this.courseName = courseName;
        this.topics = new HashMap<>();

        // TODO: Read from config file!
        this.language = "C";
    }

    public Topic loadTopic(String topicName) throws IOException, ParserConfigurationException, SAXException {
        if (topics.containsKey(topicName)) {
            return topics.get(topicName);
        } else {
            Topic newTopic = new Topic(user, courseName, topicName, language);
            topics.put(topicName, newTopic);
            return newTopic;
        }
    }

}
