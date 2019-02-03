package hu.szeba.hades.model.task.program;

import hu.szeba.hades.controller.task.ProcessCache;
import hu.szeba.hades.io.TabbedFile;
import hu.szeba.hades.model.task.result.Result;
import hu.szeba.hades.model.task.result.ResultLine;
import hu.szeba.hades.util.StreamUtil;

import java.io.*;
import java.util.concurrent.TimeUnit;

public class ProgramC extends Program {

    public ProgramC(File location) {
        super(location);
    }

    @Override
    public Result run(ProgramInput input, ProcessCache processCache, int maxResultLineCount) throws IOException, InterruptedException {
        Result result = new Result();

        ProcessBuilder processBuilder = new ProcessBuilder(location.getAbsolutePath());

        Process process = processBuilder.start();
        processCache.set(process);

        OutputStream os = process.getOutputStream();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
        TabbedFile file = input.getFile();
        for (int i = 0; i < file.getLineCount(); i++) {
            bw.write(file.getData(i, 0) + "\n");
        }
        bw.flush();
        bw.close();
        os.close();

        StreamUtil.getStream(process.getInputStream(), maxResultLineCount).forEach(
                (line) -> result.addResultLine(new ResultLine(line))
        );

        process.waitFor();
        processCache.clear();

        return result;
    }

}
