package hu.szeba.hades.model.compiler;

import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.model.task.program.Program;
import hu.szeba.hades.model.task.program.ProgramC;

import java.io.File;

public class CompilerC implements Compiler {

    private File compilerPath;

    public CompilerC(File compilerPath) {
        this.compilerPath = compilerPath;
    }

    @Override
    public Program compile(Task task) {
        return new ProgramC();
    }

}
