package hu.szeba.hades.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class DataFile {

    private File file;
    private List<String[]> content;
    private String name;
    private String separator;

    public DataFile(File file) throws IOException {
        this(file, "|");
    }

    public DataFile(File file, String separator) throws IOException {
        this.file = file;
        this.content = new ArrayList<>();
        this.name = file.getName();
        this.separator = separator;
        if (file.exists()) {
            FileReader fReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fReader);
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.equals("")) {
                    String[] tmp = line.split(Pattern.quote(separator));
                    for (int i = 0; i < tmp.length; i++) {
                        tmp[i] = tmp[i].trim();
                    }
                    this.content.add(tmp);
                }
            }
            reader.close();
            fReader.close();
        } else if (!file.createNewFile()) {
            throw new IOException("Couldn't create new file at: " + file.getAbsolutePath());
        }
    }

    public DataFile(DataFile other) {
        this.file = new File(other.file.getAbsolutePath());
        this.content = new ArrayList<>();
        this.name = other.name;
        this.separator = other.separator;
        for (String[] line : other.content) {
            String[] copy = line.clone();
            this.content.add(copy);
        }
    }

    public String getData(int lineNumber, int position) {
        if (lineNumber < 0 || lineNumber >= content.size() ||
                position < 0 || position >= content.get(lineNumber).length) {
            return "";
        } else {
            return content.get(lineNumber)[position];
        }
    }

    public void addData(String... data) {
        content.add(data);
    }

    public void save() throws IOException {
        FileWriter fWriter = new FileWriter(file);
        BufferedWriter writer = new BufferedWriter(fWriter);

        for (String[] data : content) {
            for (int i = 0; i < data.length; i++) {
                writer.write(data[i]);
                if (i != data.length-1) {
                    writer.write(separator);
                }
            }
            writer.newLine();
        }

        writer.close();
        fWriter.close();
    }

    public int getLineCount() {
        return content.size();
    }

    public String getName() {
        return name;
    }
}
