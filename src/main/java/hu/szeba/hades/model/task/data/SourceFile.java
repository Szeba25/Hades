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
    private boolean readonly;

    public SourceFile(File file, boolean readonly) throws IOException {
        this.file = file;
        this.name = file.getName();
        if (file.exists()) {
            this.data = String.join("\n",
                    Files.readAllLines(
                            Paths.get(file.getAbsolutePath())));
        } else {
            this.data = "";
        }
        this.readonly = readonly;
    }

    public String getName() { return name; }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        if (!readonly) {
            this.data = data;
        }
    }

    public void save() throws IOException {
        if (!readonly) {
            Files.write(Paths.get(file.getAbsolutePath()),
                    Arrays.stream(data.split("\n")).collect(Collectors.toList()));
        }
    }

    public void delete() throws IOException {
        if (readonly) {
            throw new IOException("Source is readonly: " + name);
        } else if (!file.delete()) {
            throw new IOException("Couldn't delete file: " + file.getAbsolutePath());
        }
    }

    public boolean isReadonly() {
        return readonly;
    }

}
