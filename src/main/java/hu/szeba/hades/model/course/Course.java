package hu.szeba.hades.model.course;

import hu.szeba.hades.model.topic.Topic;
import hu.szeba.hades.model.topic.TopicCollection;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
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

    public Topic loadTopic(String topicName) throws IOException, ParserConfigurationException, SAXException {
        return topicCollection.loadTopic(topicName, language);
    }

}
