package hu.szeba.hades.model.task;

import hu.szeba.hades.model.compiler.CompilerOutput;
import hu.szeba.hades.model.compiler.ProgramCompiler;
import hu.szeba.hades.model.task.data.TaskData;
import hu.szeba.hades.model.task.result.ResultMatcher;

public class Task implements CompilerOutputRegister {

    private final TaskData data;
    private final ProgramCompiler programCompiler;
    private final String language;
    private CompilerOutput compilerOutput;

    public Task(TaskData data, ProgramCompiler programCompiler, String language) {
        this.data = data;
        this.programCompiler = programCompiler;
        this.language = language;
        this.compilerOutput = programCompiler.getCached(data.getTaskWorkingDirectory());
    }

    public TaskData getData() {
        return data;
    }

    public ProgramCompiler getProgramCompiler() {
        return programCompiler;
    }

    public CompilerOutput getCompilerOutput() {
        return compilerOutput;
    }

    @Override
    public void registerCompilerOutput(CompilerOutput compilerOutput) {
        this.compilerOutput = compilerOutput;
    }

}

