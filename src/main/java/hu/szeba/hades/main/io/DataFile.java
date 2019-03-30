package hu.szeba.hades.main.io;

import java.io.*;
import java.nio.charset.StandardCharsets;
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
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(isr);

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
            isr.close();
            fis.close();
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

    public String[] getLine(int lineNumber) {
        return content.get(lineNumber);
    }

    public void setData(int lineNumber, int position, String data) {
        if (!(lineNumber < 0 || lineNumber >= content.size() ||
                position < 0 || position >= content.get(lineNumber).length)) {
            String[] line = content.get(lineNumber);
            line[position] = data;
        }
    }

    public void addData(String... data) {
        content.add(data);
    }

    public void save() throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        BufferedWriter writer = new BufferedWriter(osw);

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
        osw.close();
        fos.close();
    }

    public int getLineCount() {
        return content.size();
    }

    public int getDataCount(int lineNumber) {
        return content.get(lineNumber).length;
    }

    public String getName() {
        return name;
    }

    public void clear() {
        content.clear();
    }

    public String getAllData() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < content.size(); i++) {
            builder.append(String.join(Pattern.quote(separator), content.get(i)));
            if (i < content.size()-1) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    public void setAllData(String data) {
        clear();
        for (String line : data.split("\n")) {
            addData(line);
        }
    }
}
