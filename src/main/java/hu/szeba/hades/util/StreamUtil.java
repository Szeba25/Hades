package hu.szeba.hades.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class StreamUtil {

    public static List<String> getStream(InputStream stream) throws IOException {
        return getStream(stream, 20480);
    }

    public static List<String> getStream(InputStream stream, int maxByteCount) throws IOException {
        InputStreamReader is = new InputStreamReader(stream);
        BufferedReader br = new BufferedReader(is);
        List<String> messageList = new LinkedList<>();

        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[maxByteCount];

        br.read(buffer);

        for (int i = 0; i < maxByteCount; i++) {
            if (buffer[i] == 0) {
                break;
            } else if (buffer[i] == 10) {
                // New line!
                messageList.add(builder.toString());
                builder = new StringBuilder();
            } else if (buffer[i] == 13) {
                // Carriage return, ignore these...
            } else {
                builder.append(buffer[i]);
            }
        }

        br.close();
        is.close();
        return messageList;
    }

}
