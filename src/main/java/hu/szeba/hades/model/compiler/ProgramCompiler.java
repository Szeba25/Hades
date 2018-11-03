package hu.szeba.hades.model.compiler;

import hu.szeba.hades.model.task.data.SourceFile;
import hu.szeba.hades.model.task.program.Program;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class ProgramCompiler {

    protected File compilerPath;

    public ProgramCompiler(File compilerPath) {
        this.compilerPath = compilerPath;
    }

    public abstract Program compile(List<SourceFile> sources, File taskWorkingDirectory) throws IOException, InterruptedException;

    public abstract Program getProgram(File taskWorkingDirectory);

}
