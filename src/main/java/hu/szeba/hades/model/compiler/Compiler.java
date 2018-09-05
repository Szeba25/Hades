package hu.szeba.hades.model.compiler;

import hu.szeba.hades.model.task.program.Program;
import hu.szeba.hades.model.task.Task;

import java.io.File;

public interface Compiler {

    void compile(Task task);
    Program getProgram();

}
