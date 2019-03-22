package hu.szeba.hades.main.io;

import hu.szeba.hades.main.model.task.graph.GraphViewData;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class GraphViewFile {

    private File file;
    private Map<String, GraphViewData> data;

    public GraphViewFile(File file) throws IOException {
        this.file = file;
        this.data = new HashMap<>();

        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(isr);

        String line;
        while ((line = reader.readLine()) != null) {
            String[] gvd = line.split(",");
            data.put(gvd[0], new GraphViewData(gvd));
        }

        reader.close();
        isr.close();
        fis.close();
    }

    public Map<String, GraphViewData> getData() {
        return data;
    }

    public void setData(Map<String, GraphViewData> data) {
        this.data = data;
    }

    public void save() throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        BufferedWriter writer = new BufferedWriter(osw);

        for (GraphViewData d : data.values()) {
            writer.write(d.toString());
            writer.newLine();
        }

        writer.close();
        osw.close();
        fos.close();
    }

}
