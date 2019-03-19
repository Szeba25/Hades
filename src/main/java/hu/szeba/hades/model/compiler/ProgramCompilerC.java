package hu.szeba.hades.model.compiler;

import hu.szeba.hades.meta.Options;
import hu.szeba.hades.model.task.program.ProgramC;
import hu.szeba.hades.util.StreamUtilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProgramCompilerC implements ProgramCompiler {

    @Override
    public CompilerOutput compile(String[] sourceNames, File taskWorkingDirectory) throws IOException, InterruptedException {
        File compilerPath = Options.getPathTo("compiler_c");

        String finalProcessPath = compilerPath.getAbsolutePath() + "/bin/gcc";

        List<String> commands = new ArrayList<>();
        commands.add(finalProcessPath);
        for (String sourceName : sourceNames) {
            commands.add(sourceName);
        }
        commands.add("-o");
        commands.add("program.exe");

        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.directory(taskWorkingDirectory);
        processBuilder.environment().put("Path", compilerPath.getAbsolutePath() + "/bin");

        Process process = processBuilder.start();
        process.waitFor();

        List<String> compilerMessages = new ArrayList<>();
        compilerMessages.addAll(StreamUtilities.getStream(process.getErrorStream()));
        compilerMessages.addAll(StreamUtilities.getStream(process.getInputStream()));
        compilerMessages.add("Exit value: " + process.exitValue());

        return generateOutput(compilerMessages, taskWorkingDirectory, process.exitValue());
    }

    @Override
    public CompilerOutput getCached(File taskWorkingDirectory) {
        return generateOutput(new ArrayList<>(), taskWorkingDirectory, 0);
    }

    private CompilerOutput generateOutput(List<String> compilerMessages, File taskWorkingDirectory, int exitValue) {
        File programLocation = new File(taskWorkingDirectory, "program.exe");
        if (programLocation.exists() && exitValue == 0) {
            return new CompilerOutput(compilerMessages, exitValue, new ProgramC(programLocation));
        } else {
            return new CompilerOutput(compilerMessages, exitValue,null);
        }
    }

}
