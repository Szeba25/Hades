package hu.szeba.hades.main.model.compiler;

import java.io.File;
import java.io.IOException;

public interface ProgramCompiler {

    CompilerOutput compile(String[] sourceNames, File taskWorkingDirectory)
            throws IOException, InterruptedException;

    CompilerOutput getCached(File taskWorkingDirectory);

}
