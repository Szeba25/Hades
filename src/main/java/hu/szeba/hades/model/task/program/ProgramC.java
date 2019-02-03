package hu.szeba.hades.model.task.program;

import hu.szeba.hades.controller.task.ProcessCache;
import hu.szeba.hades.controller.task.TaskThreadObserver;
import hu.szeba.hades.io.TabbedFile;
import hu.szeba.hades.model.task.result.Result;
import hu.szeba.hades.model.task.result.ResultLine;

import java.io.*;

public class ProgramC extends Program {

    public ProgramC(File location) {
        super(location);
    }

    @Override
    public Result run(ProgramInput input, TaskThreadObserver taskThreadObserver, ProcessCache processCache)
            throws IOException, InterruptedException {
        Result result = new Result();

        ProcessBuilder processBuilder = new ProcessBuilder(location.getAbsolutePath());

        Process process = processBuilder.start();
        processCache.setProcess(process);

        OutputStream os = process.getOutputStream();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
        TabbedFile file = input.getFile();
        for (int i = 0; i < file.getLineCount(); i++) {
            bw.write(file.getData(i, 0) + "\n");
        }
        bw.flush();
        bw.close();
        os.close();

        InputStreamReader is = new InputStreamReader(process.getInputStream());
        BufferedReader br = new BufferedReader(is);
        String line = br.readLine();
        while (line != null && !taskThreadObserver.shouldStop()) {
            result.addResultLine(new ResultLine(line + " -> " + taskThreadObserver.shouldStop()));
            line = br.readLine();
        }
        br.close();
        is.close();

        process.waitFor();

        return result;
    }

}
