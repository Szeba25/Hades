package hu.szeba.hades.model.task.data;

import java.io.*;
import java.nio.charset.StandardCharsets;
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
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(isr);

            String line;
            List<String> lines = new LinkedList<>();
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            this.data = String.join("\n", lines);

            reader.close();
            isr.close();
            fis.close();
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
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            BufferedWriter writer = new BufferedWriter(osw);

            List<String> lines = Arrays.stream(data.split("\n")).collect(Collectors.toList());
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }

            writer.close();
            osw.close();
            fos.close();
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
