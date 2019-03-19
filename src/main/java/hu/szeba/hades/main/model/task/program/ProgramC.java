package hu.szeba.hades.main.model.task.program;

import hu.szeba.hades.main.io.TabbedFile;
import hu.szeba.hades.main.model.task.result.Result;
import hu.szeba.hades.main.model.task.result.ResultLine;
import hu.szeba.hades.main.util.StreamUtilities;

import java.io.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProgramC extends Program {

    public ProgramC(File location) {
        super(location);
    }

    @Override
    public Result run(ProgramInput input, int maxByteCount, AtomicBoolean stopFlag)
            throws IOException, InterruptedException {
        Result result = new Result();

        ProcessBuilder processBuilder = new ProcessBuilder(location.getAbsolutePath());

        Process process = processBuilder.start();

        OutputStreamWriter ow = new OutputStreamWriter(process.getOutputStream());
        TabbedFile file = input.getFile();
        for (int i = 0; i < file.getLineCount(); i++) {
            ow.write(file.getData(i, 0) + "\n");
        }
        ow.flush();
        ow.close();

        for (String line : StreamUtilities.getStreamPatient(process.getInputStream(), maxByteCount, stopFlag)) {
            result.addResultLine(new ResultLine(line));
        }

        for (String line : StreamUtilities.getStreamLowLatency(process.getErrorStream(), maxByteCount, stopFlag)) {
            result.addDebugLine(line);
        }

        if (!process.waitFor(2, TimeUnit.SECONDS)) {
            process.destroy();
            result.clearAll();
        }

        return result;
    }

}
