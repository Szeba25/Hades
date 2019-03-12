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

    public TaskDescription parse() {
        NodeList nodeList = documentElement.getElementsByTagName("Task");
        Node node = nodeList.item(0);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            String taskTitle = element.getElementsByTagName("Title").item(0).getTextContent();
            String shortDescription = element.getElementsByTagName("ShortDescription").item(0).getTextContent();
            String instructions = element.getElementsByTagName("Instructions").item(0).getTextContent();
            String story = element.getElementsByTagName("Story").item(0).getTextContent();
            return new TaskDescription(taskTitle, shortDescription, instructions, story);
        } else {
            return null;
        }
    }

}
