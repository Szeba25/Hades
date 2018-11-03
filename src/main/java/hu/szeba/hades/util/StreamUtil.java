package hu.szeba.hades.util;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class StreamUtil {

    public static List<String> getStream(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        List<String> messageList = new LinkedList<>();
        String line = reader.readLine();
        while (line != null && !line.equals("")) {
            messageList.add(line);
            line = reader.readLine();
        }
        return messageList;
    }

}
