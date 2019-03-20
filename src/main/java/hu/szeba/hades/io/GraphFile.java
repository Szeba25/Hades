package hu.szeba.hades.io;

import hu.szeba.hades.model.task.graph.Tuple;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GraphFile {

    private List<Tuple> tuples;

    public GraphFile(File file) throws IOException {
        this.tuples = new ArrayList<>();

        ArrayList<String> strings = new ArrayList<>();

        FileReader fReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fReader);
        String line;
        while ((line = reader.readLine()) != null) {
            strings.add(line);
        }
        fReader.close();
        reader.close();

        for (String tuple : strings) {
            tuples.add(new Tuple(tuple));
        }
    }

    public List<Tuple> getTuples() {
        return tuples;
    }
}
