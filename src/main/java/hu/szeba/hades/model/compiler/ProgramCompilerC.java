package hu.szeba.hades.model.compiler;

import hu.szeba.hades.Main;
import hu.szeba.hades.model.task.data.SourceFile;
import hu.szeba.hades.model.task.program.Program;
import hu.szeba.hades.model.task.program.ProgramC;

import java.io.*;
import java.util.List;
import java.util.Map;

public class ProgramCompilerC extends ProgramCompiler {

    public ProgramCompilerC(File compilerPath) {
        super(compilerPath);
    }

    @Override
    public Program compile(List<SourceFile> sources, File taskWorkingDirectory) throws IOException, InterruptedException {
        String finalProcessPath = compilerPath.getAbsolutePath() + "/bin/gcc.exe";
        System.out.println(finalProcessPath);

        String cTestResource = new File(ProgramCompilerC.class.getResource("test.c").getFile()).getAbsolutePath();

        System.out.println(cTestResource);

        ProcessBuilder processBuilder =
                new ProcessBuilder(finalProcessPath,
                        cTestResource, "-o", taskWorkingDirectory + "/program.exe");

        Map<String, String> envs = processBuilder.environment();
        envs.put("Path", compilerPath.getAbsolutePath() + "/bin");

        Process process = processBuilder.start();

        process.waitFor();

        System.out.println("Exit value: " + process.exitValue());

        printStream(process.getErrorStream());
        printStream(process.getInputStream());

        System.out.println("End of running...");

        return new ProgramC();
    }

    private void printStream(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        String line = reader.readLine();
        while (line != null) {
            builder.append(line);
            builder.append(System.getProperty("line.separator"));
            line = reader.readLine();
        }
        System.out.println(builder.toString());
    }

}
