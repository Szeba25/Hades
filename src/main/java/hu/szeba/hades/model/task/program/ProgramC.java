package hu.szeba.hades.model.task.program;

import hu.szeba.hades.io.TabbedFile;
import hu.szeba.hades.model.task.result.Result;
import hu.szeba.hades.model.task.result.ResultLine;
import hu.szeba.hades.util.StreamUtil;

import java.io.*;
import java.util.List;

public class ProgramC extends Program {

    public ProgramC(File location) {
        super(location);
    }

    @Override
    public Result run(ProgramInput input) throws IOException, InterruptedException {
        Result result = new Result();

        ProcessBuilder processBuilder = new ProcessBuilder(location.getAbsolutePath());

        Process process = processBuilder.start();

        OutputStream os = process.getOutputStream();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
        TabbedFile file = input.getFile();
        for (int i = 0; i < file.getLineCount(); i++) {
            bw.write(file.getData(i, 0));
        }
        bw.flush();
        bw.close();
        os.close();

        process.waitFor();

        StreamUtil.getStream(process.getInputStream()).forEach((res) -> {
            result.addResultLine(new ResultLine(res));
        });

        return result;
    }

}
