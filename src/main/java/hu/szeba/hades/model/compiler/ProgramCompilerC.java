package hu.szeba.hades.model.compiler;

import hu.szeba.hades.model.task.data.SourceFile;
import hu.szeba.hades.model.task.program.Program;
import hu.szeba.hades.model.task.program.ProgramC;

import java.io.*;
import java.util.List;

public class ProgramCompilerC extends ProgramCompiler {

    public ProgramCompilerC(File compilerPath) {
        super(compilerPath);
    }

    @Override
    public Program compile(List<SourceFile> sources, File taskWorkingDirectory) throws IOException {
        System.out.println(compilerPath.getAbsolutePath());
        String finalProcessPath = compilerPath.getAbsolutePath() + "/bin/gcc";

        ProcessBuilder processBuilder =
                new ProcessBuilder(finalProcessPath);
        Process process = processBuilder.start();

        printStream(process, process.getErrorStream());
        printStream(process, process.getInputStream());

        return new ProgramC();
    }

    private void printStream(Process process, InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
            builder.append(System.getProperty("line.separator"));
        }
        System.out.println(builder.toString());
    }

}
