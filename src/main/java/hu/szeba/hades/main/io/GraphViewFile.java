package hu.szeba.hades.main.io;

import hu.szeba.hades.wizard.view.elements.GraphNode;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class GraphViewFile {

    private File file;
    private Map<String, GraphNode> data;

    public GraphViewFile(File file) throws IOException {
        this.file = file;
        this.data = new HashMap<>();

        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(isr);

        String line;
        while ((line = reader.readLine()) != null) {
            String[] gvd = line.split(Pattern.quote("|"));
            data.put(gvd[0], new GraphNode(gvd));
        }

        reader.close();
        isr.close();
        fis.close();
    }

    public Map<String, GraphNode> getData() {
        return data;
    }

    public void setData(Map<String, GraphNode> data) {
        this.data = data;
    }

    public void save() throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        BufferedWriter writer = new BufferedWriter(osw);

        for (GraphNode gn : data.values()) {
            writer.write(gn.toString());
            writer.newLine();
        }

        writer.close();
        osw.close();
        fos.close();
    }

}
