package hu.szeba.hades.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        Files.lines(Paths.get(file.getAbsolutePath())).forEach(
            (line) -> {
                if (!line.equals("")) {
                    String[] tmpContent = line.split(Pattern.quote(separator));
                    for (int i = 0; i < tmpContent.length; i++) {
                        tmpContent[i] = tmpContent[i].trim();
                    }
                    content.add(tmpContent);
                }
            }
        );
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
