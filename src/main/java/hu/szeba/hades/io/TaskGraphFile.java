package hu.szeba.hades.io;

import hu.szeba.hades.model.task.graph.Tuple;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TaskGraphFile {

    private File file;
    private List<Tuple> tuples;

    public TaskGraphFile(File file) throws IOException {
        this.file = file;
        this.tuples = new ArrayList<>();

        ArrayList<String> strings = new ArrayList<>();
        Files.lines(Paths.get(file.getAbsolutePath())).forEach(strings::add);

        for (String tuple : strings) {
            tuples.add(new Tuple(tuple));
        }
    }

    public void printContents() {
        for (Tuple tuple : tuples) {
            tuple.print();
        }
    }

    public List<Tuple> getTuples() {
        return tuples;
    }
}
