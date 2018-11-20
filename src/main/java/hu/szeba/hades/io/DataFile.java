package hu.szeba.hades.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class DataFile {

    private List<String[]> content;
    private String name;

    public DataFile(File file) throws IOException {
        this(file, "|");
    }

    public DataFile(File file, String separator) throws IOException {
        content = new ArrayList<>();
        name = file.getName();
        FileReader fReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fReader);
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.equals("")) {
                String[] tmp = line.split(Pattern.quote(separator));
                for (int i = 0; i < tmp.length; i++) {
                    tmp[i] = tmp[i].trim();
                }
                content.add(tmp);
            }
        }
        fReader.close();
        reader.close();
    }

    public DataFile(DataFile other) {
        this.name = other.name;
        this.content = new ArrayList<>();
        for (String[] line : other.content) {
            String[] copy = line.clone();
            this.content.add(copy);
        }
    }

    public String getData(int lineNumber, int position) {
        if (lineNumber < 0 || lineNumber > content.size() ||
                position < 0 || position >= content.get(lineNumber).length) {
            return "";
        } else {
            return content.get(lineNumber)[position];
        }
    }

    public int getLineCount() {
        return content.size();
    }

    public String getName() {
        return name;
    }
}
