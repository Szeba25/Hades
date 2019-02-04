package hu.szeba.hades.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class StreamUtil {

    public static List<String> getStream(InputStream stream, int maxByteCount, AtomicBoolean stopFlag)
            throws IOException {
        InputStreamReader is = new InputStreamReader(stream);
        List<String> messageList = new LinkedList<>();

        StringBuilder builder = new StringBuilder();
        int byteCount = 0;
        int lineCount = 0;

        int data = is.read();
        while (data != -1 && !stopFlag.get() && byteCount < maxByteCount) {
            byteCount++;
            if (data == 10) {
                // New line!
                messageList.add(builder.toString());
                lineCount++;
                builder = new StringBuilder();
            } else if (data != 13) {
                // Carriage return, ignore these...
                builder.append((char)data);
            }
            data = is.read();
        }

        if (lineCount == 0 && byteCount > 0) {
            messageList.add(builder.toString());
        }

        System.out.println("Bytes: " + byteCount);

        is.close();
        return messageList;
    }

    public static List<String> getStream(InputStream stream) throws IOException {
        return getStream(stream, 20480);
    }

    public static List<String> getStream(InputStream stream, int maxByteCount) throws IOException {
        InputStreamReader is = new InputStreamReader(stream);
        BufferedReader br = new BufferedReader(is);
        List<String> messageList = new LinkedList<>();

        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[maxByteCount];

        // TODO: This read blocks when there is no output from the program!!!
        int count = br.read(buffer);
        int lineCount = 0;

        System.out.println("Bytes: " + count);

        if (count >= 0) {
            for (int i = 0; i < maxByteCount; i++) {
                if (buffer[i] == 0) {
                    if (lineCount == 0)
                        messageList.add(builder.toString());
                    break;
                } else if (buffer[i] == 10) {
                    // New line!
                    messageList.add(builder.toString());
                    lineCount++;
                    builder = new StringBuilder();
                } else if (buffer[i] == 13) {
                    // Carriage return, ignore these...
                } else {
                    builder.append(buffer[i]);
                }
            }
        }

        br.close();
        is.close();
        return messageList;
    }

}
