package hu.szeba.hades.model.task;

import hu.szeba.hades.model.compiler.CompilerOutput;
import hu.szeba.hades.model.compiler.ProgramCompiler;
import hu.szeba.hades.model.task.data.TaskData;
import hu.szeba.hades.model.task.result.ResultMatcher;

public class Task implements CompilerOutputRegister {

    private TaskData data;
    private ProgramCompiler programCompiler;
    private CompilerOutput compilerOutput;

    public Task(TaskData data, ProgramCompiler programCompiler) {
        this.data = data;
        this.programCompiler = programCompiler;
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

