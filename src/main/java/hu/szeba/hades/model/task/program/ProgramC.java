package hu.szeba.hades.model.task.program;

import hu.szeba.hades.io.TabbedFile;
import hu.szeba.hades.model.task.result.Result;
import hu.szeba.hades.model.task.result.ResultLine;
import hu.szeba.hades.util.StreamUtil;

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

        for (String line : StreamUtil.getStreamPatient(process.getInputStream(), maxByteCount, stopFlag)) {
            result.addResultLine(new ResultLine(line));
        }

        for (String line : StreamUtil.getStreamLowLatency(process.getErrorStream(), maxByteCount, stopFlag)) {
            result.addDebugLine(line);
        }

        process.waitFor(250, TimeUnit.MILLISECONDS);
        process.destroy();

        return result;
    }

}
