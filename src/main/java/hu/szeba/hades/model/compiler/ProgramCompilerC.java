package hu.szeba.hades.model.compiler;

import hu.szeba.hades.model.task.data.TaskData;
import hu.szeba.hades.model.task.program.Program;
import hu.szeba.hades.model.task.program.ProgramC;

import java.io.File;

public class ProgramCompilerC extends ProgramCompiler {

    public ProgramCompilerC(File compilerPath, File taskWorkingDirectory) {
        super(compilerPath, taskWorkingDirectory);
    }

    @Override
    public Program compile(TaskData taskData) {
        return new ProgramC();
    }

}
