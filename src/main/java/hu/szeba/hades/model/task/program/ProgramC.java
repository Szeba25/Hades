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

        OutputStream os = process.getOutputStream();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
        TabbedFile file = input.getFile();
        for (int i = 0; i < file.getLineCount(); i++) {
            bw.write(file.getData(i, 0) + "\n");
        }
        bw.flush();
        bw.close();
        os.close();

        for (String line : StreamUtil.getStream(process.getInputStream(), maxByteCount, stopFlag)) {
            result.addResultLine(new ResultLine(line));
        }

        while(process.isAlive() && !stopFlag.get()) {
            process.waitFor(1, TimeUnit.SECONDS);
        }
        process.destroy();

        return result;
    }

}
