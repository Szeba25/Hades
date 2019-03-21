package hu.szeba.hades.main.io;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class GraphViewFile {

    private Map<String, GraphViewData> data;

    public GraphViewFile(File file) throws IOException {
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

    private class GraphViewData {

        private String name;
        private int r;
        private int g;
        private int b;
        private int x;
        private int y;

        public GraphViewData(String[] data) {
            name = data[0];
            r = Integer.parseInt(data[1]);
            g = Integer.parseInt(data[2]);
            b = Integer.parseInt(data[3]);
            x = Integer.parseInt(data[4]);
            y = Integer.parseInt(data[5]);
        }

    }

}
