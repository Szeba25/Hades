package hu.szeba.hades.io;

import hu.szeba.hades.model.task.data.TaskDescription;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DescriptionXMLFile {

    private Document document;
    private Element documentElement;

    public DescriptionXMLFile(File file) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();

        this.document = builder.parse(file);
        this.documentElement = this.document.getDocumentElement();
        this.documentElement.normalize();
    }

    public TaskDescription parse(boolean ignoreStory) {
        String taskTitle = documentElement.getElementsByTagName("Title").item(0).getTextContent();
        String shortDescription = documentElement.getElementsByTagName("ShortDescription").item(0).getTextContent();
        String instructions = documentElement.getElementsByTagName("Instructions").item(0).getTextContent();

        String story = "";
        if (!ignoreStory) {
            story = documentElement.getElementsByTagName("Story").item(0).getTextContent();
        }

        int difficulty = Integer.parseInt(documentElement.getElementsByTagName("Difficulty").item(0).getTextContent());
        int length = Integer.parseInt(documentElement.getElementsByTagName("Length").item(0).getTextContent());
        List<String> tags = new LinkedList<>();
        NodeList tagNodeList = documentElement.getElementsByTagName("Tag");
        for (int i = 0; i < tagNodeList.getLength(); i++) {
            tags.add(tagNodeList.item(i).getTextContent());
        }

        return new TaskDescription(taskTitle, shortDescription, instructions, story, difficulty, length, tags);
    }

}
