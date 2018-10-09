package hu.szeba.hades.io;

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

public class StoryXMLFile {

    private Document document;
    private Element documentElement;

    public StoryXMLFile(File file) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Finally load the xml document
        this.document = builder.parse(file);
        this.documentElement = this.document.getDocumentElement();
        this.documentElement.normalize();
    }

    public void printStoryContents() {
        System.out.println("Root: " + documentElement.getNodeName());
        NodeList nodeList = documentElement.getElementsByTagName("Story");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            System.out.println("Current element: " + node.getNodeName());

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                System.out.println("Dependency: " + element.getElementsByTagName("Dependency").item(0).getTextContent());
                System.out.println("Title: " + element.getElementsByTagName("Title").item(0).getTextContent());
                System.out.println("Description: " + element.getElementsByTagName("Description").item(0).getTextContent());
            }
        }
    }

}
