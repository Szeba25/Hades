package hu.szeba.hades.model.task;

import hu.szeba.hades.model.compiler.ProgramCompiler;
import hu.szeba.hades.model.task.data.TaskData;

public class Task {

    private final TaskData data;
    private final ProgramCompiler programCompiler;
    private final String language;
    private final CompilerOutputRegister compilerOutputRegister;

    public Task(TaskData data, ProgramCompiler programCompiler, String language) {
        this.data = data;
        this.programCompiler = programCompiler;
        this.language = language;
        compilerOutputRegister = new CompilerOutputRegister();
        compilerOutputRegister.setCompilerOutput(programCompiler.getCached(data.getTaskWorkingDirectory()));
    }

    public TaskData getData() {
        return data;
    }

    public ProgramCompiler getProgramCompiler() {
        return programCompiler;
    }

    public CompilerOutputRegister getCompilerOutputRegister() {
        return compilerOutputRegister;
    }

}

