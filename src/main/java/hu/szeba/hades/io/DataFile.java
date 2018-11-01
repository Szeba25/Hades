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

    public DataFile(File file, String separator) throws IOException {
        content = new ArrayList<>();
        Files.lines(Paths.get(file.getAbsolutePath())).forEach(
            (line) -> {
                if (!line.equals("")) content.add(line.split(Pattern.quote(separator)));
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

}
