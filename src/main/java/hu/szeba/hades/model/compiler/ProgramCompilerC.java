package hu.szeba.hades.model.compiler;

import hu.szeba.hades.model.task.data.SourceFile;
import hu.szeba.hades.model.task.program.ProgramC;
import hu.szeba.hades.util.StreamUtil;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ProgramCompilerC extends ProgramCompiler {

    public ProgramCompilerC(File compilerPath) {
        super(compilerPath);
    }

    @Override
    public CompilerOutput compile(List<SourceFile> sources, File taskWorkingDirectory) throws IOException, InterruptedException {
        String finalProcessPath = compilerPath.getAbsolutePath() + "/bin/gcc";

        List<String> commands = new LinkedList<>();
        commands.add(finalProcessPath);
        for (SourceFile s : sources)
            commands.add(s.getName());
        commands.add("-o");
        commands.add("program.exe");

        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.directory(taskWorkingDirectory);
        processBuilder.environment().put("Path", compilerPath.getAbsolutePath() + "/bin");

        Process process = processBuilder.start();
        process.waitFor();

        List<String> compileMessages = new LinkedList<>();
        compileMessages.addAll(StreamUtil.getStream(process.getErrorStream()));
        compileMessages.addAll(StreamUtil.getStream(process.getInputStream()));
        compileMessages.add("Exit value: " + process.exitValue());

        return generateOutput(compileMessages, taskWorkingDirectory);
    }

    @Override
    public CompilerOutput getCached(File taskWorkingDirectory) {
        return generateOutput(new LinkedList<>(), taskWorkingDirectory);
    }

    private CompilerOutput generateOutput(List<String> compileMessages, File taskWorkingDirectory) {
        File programLocation = new File(taskWorkingDirectory, "program.exe");
        if (programLocation.exists()) {
            return new CompilerOutput(compileMessages, new ProgramC(programLocation));
        } else {
            return new CompilerOutput(compileMessages, null);
        }
    }

}
