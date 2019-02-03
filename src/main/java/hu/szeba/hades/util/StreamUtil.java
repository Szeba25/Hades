package hu.szeba.hades.util;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class StreamUtil {

    public static List<String> getStream(InputStream stream) throws IOException {
        return getStream(stream, Integer.MAX_VALUE);
    }

    public static List<String> getStream(InputStream stream, int maxLine) throws IOException {
        InputStreamReader is = new InputStreamReader(stream);
        BufferedReader br = new BufferedReader(is);
        List<String> messageList = new LinkedList<>();
        int lineCount = 0;
        String line = br.readLine();
        while (line != null && lineCount < maxLine) {
            messageList.add(line);
            line = br.readLine();
            lineCount++;
        }
        br.close();
        is.close();
        return messageList;
    }

}
