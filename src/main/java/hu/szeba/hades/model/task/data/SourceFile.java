package hu.szeba.hades.model.task.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SourceFile {

    private File file;
    private String name;
    private String data;

    public SourceFile(File file) throws IOException {
        this.file = file;
        this.name = file.getName();
        if (file.exists()) {
            this.data = String.join("\n",
                    Files.readAllLines(
                            Paths.get(file.getAbsolutePath())));
        } else {
            this.data = "";
        }
    }

    public String getName() { return name; }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void save() throws IOException {
        Files.write(Paths.get(file.getAbsolutePath()),
                Arrays.stream(data.split("\n")).collect(Collectors.toList()));
    }

    public void delete() throws IOException {
        if (!file.delete()) {
            throw new IOException("Couldn't delete file: " + file.getAbsolutePath());
        }
    }
}
