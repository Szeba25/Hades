package hu.szeba.hades.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class StreamUtil {

    /**
     * Gets the output of a program. This version is used if the correctness of the program
     * is questionable. The stream may have no valid output, or too much output.
     * This reader handles these situations, and can be shut down during read by an atomic
     * flag, so its thread safe to change this flag.
     * @param stream The programs stream
     * @param maxByteCount The maximum byte count after the reader will stop reading
     * @param stopFlag The flag that the reading should stop
     * @return The list of lines that were read from the stream
     * @throws IOException
     * @throws InterruptedException
     */
    public static List<String> getStream(InputStream stream, int maxByteCount, AtomicBoolean stopFlag)
            throws IOException, InterruptedException {
        InputStreamReader is = new InputStreamReader(stream);
        List<String> messageList = new LinkedList<>();

        StringBuilder builder = new StringBuilder();
        int byteCount = 0;
        int lineCount = 0;

        int tries = 1;
        int maxTries = 10;
        boolean firstTry = true;
        boolean hasOutput = false;

        // First check for any output from the program...
        while(tries <= maxTries && !hasOutput && !stopFlag.get()) {
            System.out.println("Try: " + tries + ".");
            if (firstTry) {
                firstTry = false;
                Thread.sleep(150);
            } else {
                Thread.sleep(300);
            }
            if (is.ready()) {
                hasOutput = true;
            }
            tries++;
        }

        // If the program has output, then enter a loop and read it by character
        if (hasOutput) {
            // Read timed out
            boolean timeout = false;

            // First byte read
            int data = is.read();

            // Loop
            while (data != -1 && !stopFlag.get() && !timeout && byteCount < maxByteCount) {
                byteCount++;
                if (data == 10) {
                    // New line!
                    messageList.add(builder.toString());
                    lineCount++;
                    builder = new StringBuilder();
                } else if (data != 13) {
                    // Carriage return, ignore these...
                    builder.append((char) data);
                }

                // If the stream is ready, read the next char!
                if (is.ready()) {
                    data = is.read();
                } else {
                    // InputStream will not block on next read... Wait for a little bit
                    int timeoutTry = 1;
                    int timeoutMaxTry = 5;
                    // Try 5 times!
                    while (timeoutTry <= timeoutMaxTry) {
                        System.out.println("Timeout try: " + timeoutTry);
                        Thread.sleep(25);
                        if (is.ready()) {
                            data = is.read();
                            break;
                        }
                        timeoutTry++;
                    }
                    // If still not ready, timeout and halt...
                    if (!is.ready()) {
                        System.out.println("Timeout!!!");
                        timeout = true;
                    }
                }
            }

            // If there was input but no newlines, add this one line to the output...
            if (lineCount == 0 && byteCount > 0) {
                messageList.add(builder.toString());
            }
        }

        System.out.println("Bytes: " + byteCount);

        is.close();
        return messageList;
    }

    /**
     * Gets the output of a program. This version is used when the program is correct,
     * in other words: it's guaranteed, that the underlying implementation won't block
     * the read process.
     * This version also reads all the input.
     * @param stream The programs stream
     * @return The list of lines that were read from the stream
     * @throws IOException
     */
    public static List<String> getStream(InputStream stream) throws IOException {
        InputStreamReader is = new InputStreamReader(stream);
        BufferedReader br = new BufferedReader(is);
        List<String> messageList = new LinkedList<>();

        // IMPORTANT: This read blocks when there is no output from the program!!!
        String line = br.readLine();

        while (line != null) {
            messageList.add(line);
            line = br.readLine();
        }

        br.close();
        is.close();
        return messageList;
    }

}
