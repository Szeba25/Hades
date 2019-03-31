package hu.szeba.hades.main.model.task.data;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
            List<String> lines = new ArrayList<>();
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
            FileUtils.forceMkdirParent(file);

            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            BufferedWriter writer = new BufferedWriter(osw);

            String[] lines = data.split("\n");
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

    public void rename(String newName, boolean renameInFileSystem) throws IOException {
        File newFile = new File(file.getParentFile(), newName);
        if (readonly) {
            throw new IOException("Source is readonly: " + name);
        } else if (renameInFileSystem) {
            if (!file.renameTo(newFile)) {
                throw new IOException("Couldn't rename file: " + file.getAbsolutePath());
            }
        }
        file = newFile;
        name = newName;
    }

    public void rename(String newName) throws IOException {
        rename(newName, true);
    }

    public boolean isReadonly() {
        return readonly;
    }

}
