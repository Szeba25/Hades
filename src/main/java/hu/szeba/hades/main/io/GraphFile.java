package hu.szeba.hades.main.io;

import hu.szeba.hades.main.model.task.graph.Tuple;
import hu.szeba.hades.wizard.view.elements.GraphNode;

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
    private Map<String, GraphNode> viewData;

    public GraphFile(File file) throws IOException {
        this.file = file;
        this.viewFile = new File(file.getAbsolutePath() + ".view");
        this.tuples = new ArrayList<>();
        this.viewData = new HashMap<>();

        loadTuples();
        loadViewData();
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

    private void loadViewData() throws IOException {
        FileInputStream fis = new FileInputStream(viewFile);
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(isr);

        String line;
        while ((line = reader.readLine()) != null) {
            String[] gvd = line.split(Pattern.quote("|"));
            viewData.put(gvd[0], new GraphNode(gvd));
        }

        reader.close();
        isr.close();
        fis.close();

        // Connect the view data!
        for (Tuple tuple : tuples) {
            if (viewData.containsKey(tuple.getElement1())) {
                if (viewData.containsKey(tuple.getElement2())) {
                    viewData.get(tuple.getElement1()).addConnection(viewData.get(tuple.getElement2()));
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

    public Map<String, GraphNode> getViewData() {
        return viewData;
    }

    public void setViewData(Map<String, GraphNode> viewData) {
        this.viewData = viewData;
    }

    public void save() throws IOException {
        saveTuples();
        saveViewData();
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

    private void saveViewData() throws IOException {
        FileOutputStream fos = new FileOutputStream(viewFile);
        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        BufferedWriter writer = new BufferedWriter(osw);

        for (GraphNode gn : viewData.values()) {
            writer.write(gn.toString());
            writer.newLine();
        }

        writer.close();
        osw.close();
        fos.close();
    }

}
