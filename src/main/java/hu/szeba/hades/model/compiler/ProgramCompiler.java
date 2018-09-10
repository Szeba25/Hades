package hu.szeba.hades.model.compiler;

import hu.szeba.hades.model.task.data.TaskData;
import hu.szeba.hades.model.task.program.Program;

import java.io.File;

public abstract class ProgramCompiler {

    protected File compilerPath;
    protected File taskWorkingDirectory;

    public ProgramCompiler(File compilerPath, File taskWorkingDirectory) {
        this.compilerPath = compilerPath;
        this.taskWorkingDirectory = taskWorkingDirectory;
    }

    public abstract Program compile(TaskData taskData);

}
