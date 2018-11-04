package hu.szeba.hades.model.compiler;

import hu.szeba.hades.model.task.program.ProgramC;
import hu.szeba.hades.util.StreamUtil;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ProgramCompilerC extends ProgramCompiler {

    public ProgramCompilerC(File compilerPath) {
        super(compilerPath);
    }

    @Override
    public CompilerOutput compile(String[] sources, File taskWorkingDirectory) throws IOException, InterruptedException {
        String finalProcessPath = compilerPath.getAbsolutePath() + "/bin/gcc";

        List<String> commands = new LinkedList<>();
        commands.add(finalProcessPath);
        commands.addAll(Arrays.asList(sources));
        commands.add("-o");
        commands.add("program.exe");

        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.directory(taskWorkingDirectory);
        processBuilder.environment().put("Path", compilerPath.getAbsolutePath() + "/bin");

        Process process = processBuilder.start();
        process.waitFor();

        List<String> compilerMessages = new LinkedList<>();
        compilerMessages.addAll(StreamUtil.getStream(process.getErrorStream()));
        compilerMessages.addAll(StreamUtil.getStream(process.getInputStream()));
        compilerMessages.add("Exit value: " + process.exitValue());

        return generateOutput(compilerMessages, taskWorkingDirectory, process.exitValue());
    }

    @Override
    public CompilerOutput getCached(File taskWorkingDirectory) {
        return generateOutput(new LinkedList<>(), taskWorkingDirectory, 0);
    }

    private CompilerOutput generateOutput(List<String> compilerMessages, File taskWorkingDirectory, int exitValue) {
        File programLocation = new File(taskWorkingDirectory, "program.exe");
        if (programLocation.exists() && exitValue == 0) {
            return new CompilerOutput(compilerMessages, new ProgramC(programLocation));
        } else {
            return new CompilerOutput(compilerMessages, null);
        }
    }

}
