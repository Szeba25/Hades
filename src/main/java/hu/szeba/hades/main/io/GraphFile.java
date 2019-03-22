package hu.szeba.hades.main.io;

import hu.szeba.hades.main.model.task.graph.Tuple;
import hu.szeba.hades.wizard.view.elements.GraphViewNode;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class GraphFile {

    private File file;
    private File viewFile;
    private List<Tuple> tuples;
    private Map<String, GraphViewNode> viewNodes;

    public GraphFile(File file) throws IOException {
        this.file = file;
        this.viewFile = new File(file.getAbsolutePath() + ".view");
        this.tuples = new ArrayList<>();
        this.viewNodes = new HashMap<>();

        loadTuples();
        loadViewNodes();
    }

    private void loadTuples() throws IOException {
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(isr);

        String line;
        while ((line = reader.readLine()) != null) {
            tuples.add(new Tuple(line));
        }

        reader.close();
        isr.close();
        fis.close();
    }

    private void loadViewNodes() throws IOException {
        FileInputStream fis = new FileInputStream(viewFile);
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(isr);

        String line;
        while ((line = reader.readLine()) != null) {
            String[] gvd = line.split(Pattern.quote("|"));
            viewNodes.put(gvd[0], new GraphViewNode(gvd));
        }

        reader.close();
        isr.close();
        fis.close();

        // Connect the view nodes!
        for (Tuple tuple : tuples) {
            if (viewNodes.containsKey(tuple.getElement1())) {
                if (viewNodes.containsKey(tuple.getElement2())) {
                    viewNodes.get(tuple.getElement1()).addConnection(viewNodes.get(tuple.getElement2()));
                }
            }
        }
    }

    public List<Tuple> getTuples() {
        return tuples;
    }

    public void setTuples(List<Tuple> tuples) {
        this.tuples = tuples;
    }

    public Map<String, GraphViewNode> getViewNodes() {
        return viewNodes;
    }

    public void setViewNodes(Map<String, GraphViewNode> viewNodes) {
        this.viewNodes = viewNodes;
    }

    public void save() throws IOException {
        saveTuples();
        saveViewNodes();
    }

    private void saveTuples() throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        BufferedWriter writer = new BufferedWriter(osw);

        for (Tuple tuple : tuples) {
            writer.write(tuple.toString());
            writer.newLine();
        }

        writer.close();
        osw.close();
        fos.close();
    }

    private void saveViewNodes() throws IOException {
        FileOutputStream fos = new FileOutputStream(viewFile);
        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        BufferedWriter writer = new BufferedWriter(osw);

        for (GraphViewNode gn : viewNodes.values()) {
            writer.write(gn.toString());
            writer.newLine();
        }

        writer.close();
        osw.close();
        fos.close();
    }

}
