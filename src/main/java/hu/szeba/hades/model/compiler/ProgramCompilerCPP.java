package hu.szeba.hades.model.compiler;

import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.model.task.program.Program;
import hu.szeba.hades.model.task.program.ProgramCPP;

import java.io.File;

public class ProgramCompilerCPP implements ProgramCompiler {

    private File compilerPath;
    private File taskWorkingDirectory;

    public ProgramCompilerCPP(File compilerPath, File taskWorkingDirectory) {
        this.compilerPath = compilerPath;
        this.taskWorkingDirectory = taskWorkingDirectory;
    }

    @Override
    public Program compile(Task task) {
        return new ProgramCPP();
    }

}
