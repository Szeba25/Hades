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

    public Map<String, TaskDescription> parseTaskDescriptions() {
        HashMap<String, TaskDescription> descriptions = new HashMap<>();
        NodeList nodeList = documentElement.getElementsByTagName("Description");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String taskName = element.getElementsByTagName("Task").item(0).getTextContent();
                String title = element.getElementsByTagName("Title").item(0).getTextContent();
                String shortDescription = element.getElementsByTagName("Short").item(0).getTextContent();
                String longDescription = element.getElementsByTagName("Long").item(0).getTextContent();
                descriptions.put(taskName, new TaskDescription(taskName, title, shortDescription, longDescription));
            }
        }
        return descriptions;
    }

}
