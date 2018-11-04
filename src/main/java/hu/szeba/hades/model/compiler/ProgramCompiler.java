package hu.szeba.hades.model.compiler;

import java.io.File;
import java.io.IOException;

public abstract class ProgramCompiler {

    protected File compilerPath;

    public ProgramCompiler(File compilerPath) {
        this.compilerPath = compilerPath;
    }

    public abstract CompilerOutput compile(String[] sources, File taskWorkingDirectory) throws IOException, InterruptedException;

    public abstract CompilerOutput getCached(File taskWorkingDirectory);

}
