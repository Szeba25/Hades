package hu.szeba.hades.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class StreamUtilities {

    /**
     * This is a convenience method to start the getStream method for "potentially non correct programs"
     * with low wait times. Useful for reading stderr AFTER stdin.
     */
    public static List<String> getStreamLowLatency(InputStream stream, int maxByteCount, AtomicBoolean stopFlag)
            throws IOException, InterruptedException {
        return getStream(stream, maxByteCount, stopFlag,
                4, 2, 50, 15);
    }

    /**
     * This is a convenience method to start the getStream method for "potentially non correct programs"
     * with default wait times. Useful for reading stdin without reading anything first.
     */
    public static List<String> getStreamPatient(InputStream stream, int maxByteCount, AtomicBoolean stopFlag)
            throws IOException, InterruptedException {
        return getStream(stream, maxByteCount, stopFlag,
                6, 3, 300, 30);
    }

    /**
     * Gets the output of a program. This version is used if the correctness of the program
     * is questionable. The stream may have no valid output, or too much output.
     * This reader handles these situations, and can be shut down during read by an atomic
     * flag, so its thread safe to change this flag.
     * @param stream The programs stream
     * @param maxByteCount The maximum byte count after the reader will stop reading
     * @param stopFlag The flag that the reading should stop
     * @param startMaxTries The maximum try count when program is just starting
     * @param timeoutMaxTries The maximum try count when the program is already running, but output halts
     * @param maxStartWaitInMillis The maximum time the reader should wait in millis in one try if there is no input
     *                             at the start
     * @param maxTimeoutWaitInMillis The maximum time the reader should wait in millis in one try if
     *                               there is no input during an intermediate halt
     * @return The list of lines that were read from the stream
     * @throws IOException
     * @throws InterruptedException
     */
    public static List<String> getStream(InputStream stream, int maxByteCount, AtomicBoolean stopFlag,
                                         int startMaxTries, int timeoutMaxTries,
                                         int maxStartWaitInMillis, int maxTimeoutWaitInMillis)
            throws IOException, InterruptedException {
        InputStreamReader is = new InputStreamReader(stream);
        List<String> messageList = new LinkedList<>();

        StringBuilder builder = new StringBuilder();
        int byteCount = 0;

        int tries = 1;
        boolean firstTry = true;
        boolean hasOutput = false;

        // First check for any output from the program...
        // Wait for half the specified time, to speed up processing...
        while(tries <= startMaxTries && !hasOutput && !stopFlag.get()) {
            //System.out.println("Try: " + tries + ".");
            if (firstTry) {
                firstTry = false;
                Thread.sleep(maxStartWaitInMillis / 2);
            } else {
                Thread.sleep(maxStartWaitInMillis);
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
                    // Try n times! (defined by timeoutMaxTries)
                    while (timeoutTry <= timeoutMaxTries) {
                        //System.out.println("Timeout try: " + timeoutTry);
                        Thread.sleep(maxTimeoutWaitInMillis);
                        if (is.ready()) {
                            data = is.read();
                            break;
                        }
                        timeoutTry++;
                    }
                    // If still not ready, timeout and halt...
                    if (!is.ready()) {
                        //System.out.println("Timeout!!!");
                        timeout = true;
                    }
                }
            }

            // If there was any leftover output, add this too...
            if (builder.length() > 0) {
                messageList.add(builder.toString());
            }
        }

        //System.out.println("Bytes: " + byteCount);

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
