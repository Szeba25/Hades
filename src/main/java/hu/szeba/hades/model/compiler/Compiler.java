package hu.szeba.hades.model.compiler;

import hu.szeba.hades.model.task.Program;
import hu.szeba.hades.model.task.Task;

import java.io.File;

public abstract class Compiler {

    private File compilerPath;

    public Compiler(File compilerPath) {
        this.compilerPath = compilerPath;
    }

    public abstract void compile(Task task);
    public abstract Program getRunnableProgram();

}
