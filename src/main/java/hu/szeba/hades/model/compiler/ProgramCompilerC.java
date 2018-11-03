package hu.szeba.hades.model.compiler;

import hu.szeba.hades.model.task.data.SourceFile;
import hu.szeba.hades.model.task.program.Program;
import hu.szeba.hades.model.task.program.ProgramC;
import hu.szeba.hades.util.StreamUtil;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class ProgramCompilerC extends ProgramCompiler {

    public ProgramCompilerC(File compilerPath) {
        super(compilerPath);
    }

    @Override
    public Program compile(List<SourceFile> sources, File taskWorkingDirectory) throws IOException, InterruptedException {
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

        Program program = new ProgramC(new File(taskWorkingDirectory, "program.exe"));
        program.setCompileMessages(compileMessages);
        return program;
    }

    @Override
    public Program getProgram(File taskWorkingDirectory) {
        File programFile = new File(taskWorkingDirectory, "program.exe");
        return new ProgramC(programFile);
    }

}
