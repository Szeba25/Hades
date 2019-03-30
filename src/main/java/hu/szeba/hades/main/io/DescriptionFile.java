package hu.szeba.hades.main.io;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DescriptionFile {

    private File file;
    private Element documentElement;

    private String title;
    private String shortInstructions;
    private String instructions;
    private String shortStory;
    private String story;
    private String difficulty;
    private String length;
    private List<String> tags;

    private String shortDescription;
    private HTMLDocument cachedDocument;

    public DescriptionFile(File file, boolean ignoreStory) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();

        this.file = file;
        this.documentElement = builder.parse(file).getDocumentElement();
        this.documentElement.normalize();

        load(ignoreStory);
    }

    public void load(boolean ignoreStory) {
        title = documentElement.getElementsByTagName("Title").item(0).getTextContent().trim();
        shortInstructions = documentElement.getElementsByTagName("ShortInstructions").item(0).getTextContent().trim();
        instructions = documentElement.getElementsByTagName("Instructions").item(0).getTextContent().trim();

        shortStory = "";
        if (!ignoreStory) {
            shortStory = documentElement.getElementsByTagName("ShortStory").item(0).getTextContent().trim();
            if (shortStory.length() > 0) {
                shortStory += "<br><hr>";
            }
        }

        story = "";
        if (!ignoreStory) {
            story = documentElement.getElementsByTagName("Story").item(0).getTextContent().trim();
        }

        difficulty = documentElement.getElementsByTagName("Difficulty").item(0).getTextContent().trim();
        length = documentElement.getElementsByTagName("Length").item(0).getTextContent().trim();

        tags = new ArrayList<>();
        NodeList tagNodeList = documentElement.getElementsByTagName("Tag");
        for (int i = 0; i < tagNodeList.getLength(); i++) {
            tags.add(tagNodeList.item(i).getTextContent().trim());
        }

        shortDescription = shortStory + shortInstructions + "<br><hr>";
        shortDescription +=
                "<footer>" +
                "<p><b>Tags: </b>" + String.join(", ", tags) + "</p>" +
                "</footer>";

        cachedDocument = null;
    }

    public void save() throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        BufferedWriter writer = new BufferedWriter(osw);

        writer.write("<Task>");
        writer.newLine(); writer.newLine();

        writer.write("<Title>");
        writer.write(title);
        writer.write("</Title>");
        writer.newLine();

        writer.write("<ShortInstructions>");
        writer.newLine();
        if (shortInstructions.length() > 0) {
            shortInstructions = "<![CDATA[\n" + shortInstructions + "\n]]>";
            writer.write(shortInstructions);
            writer.newLine();
        }
        writer.write("</ShortInstructions>");
        writer.newLine();

        writer.write("<Instructions>");
        writer.newLine();
        if (instructions.length() > 0) {
            instructions = "<![CDATA[\n" + instructions + "\n]]>";
            writer.write(instructions);
            writer.newLine();
        }
        writer.write("</Instructions>");
        writer.newLine();

        writer.write("<ShortStory>");
        writer.newLine();
        if (shortStory.length() > 0) {
            shortStory = "<![CDATA[\n" + shortStory + "\n]]>";
            writer.write(shortStory);
            writer.newLine();
        }
        writer.write("</ShortStory>");
        writer.newLine();

        writer.write("<Story>");
        writer.newLine();
        if (story.length() > 0) {
            story = "<![CDATA[\n" + story + "\n]]>";
            writer.write(story);
            writer.newLine();
        }
        writer.write("</Story>");
        writer.newLine();

        writer.write("<Difficulty>");
        writer.write(difficulty);
        writer.write("</Difficulty>");
        writer.newLine();

        writer.write("<Length>");
        writer.write(length);
        writer.write("</Length>");
        writer.newLine();

        writer.write("<Tags>");
        writer.newLine();
        for (String tag : tags) {
            writer.write("\t<Tag>");;
            writer.write(tag);
            writer.write("</Tag>");
            writer.newLine();
        }
        writer.write("</Tags>");
        writer.newLine();

        writer.newLine();
        writer.write("</Task>");
        writer.newLine();

        writer.close();
        osw.close();
        fos.close();
    }

    public String getTitle() {
        return title;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getStory() {
        return story;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getLength() {
        return length;
    }

    public List<String> getTags() {
        return tags;
    }

    public HTMLDocument getCachedDocument(HTMLEditorKit kit) {
        if (cachedDocument == null) {
            cachedDocument = (HTMLDocument) kit.createDefaultDocument();
            try {
                kit.insertHTML(cachedDocument, 0, shortDescription, 0, 0, null);
            } catch (BadLocationException | IOException e) {
                e.printStackTrace();
            }
        }
        return cachedDocument;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
