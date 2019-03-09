package hu.szeba.hades.io;

import hu.szeba.hades.model.task.data.TaskStory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StoryXMLFile {

    private Document document;
    private Element documentElement;

    public StoryXMLFile(File file) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();

        this.document = builder.parse(file);
        this.documentElement = this.document.getDocumentElement();
        this.documentElement.normalize();
    }

    public Map<String, TaskStory> parseTaskStories() {
        HashMap<String, TaskStory> stories = new HashMap<>();
        NodeList nodeList = documentElement.getElementsByTagName("Story");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String taskId = element.getElementsByTagName("Id").item(0).getTextContent();
                String taskStory = element.getElementsByTagName("Text").item(0).getTextContent();
                stories.put(taskId, new TaskStory(taskId, taskStory));
            }
        }
        return stories;
    }

}
