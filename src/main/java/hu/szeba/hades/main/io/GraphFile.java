package hu.szeba.hades.main.io;

import hu.szeba.hades.main.model.task.graph.Tuple;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GraphFile {

    private List<Tuple> tuples;

    public GraphFile(File file) throws IOException {
        this.tuples = new ArrayList<>();

        ArrayList<String> strings = new ArrayList<>();

        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(isr);

        String line;
        while ((line = reader.readLine()) != null) {
            strings.add(line);
        }

        reader.close();
        isr.close();
        fis.close();

        for (String tuple : strings) {
            tuples.add(new Tuple(tuple));
        }
    }

    public List<Tuple> getTuples() {
        return tuples;
    }
}
