package hu.szeba.hades.model.compiler;

import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.model.task.program.Program;

import java.io.File;

public class CompilerCPP implements Compiler {

    private File compilerPath;

    public CompilerCPP(File compilerPath) {
        this.compilerPath = compilerPath;
    }

    @Override
    public void compile(Task task) {

    }

    @Override
    public Program getProgram() {
        return null;
    }
}
