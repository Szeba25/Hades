package hu.szeba.hades.model.task;

import hu.szeba.hades.meta.User;
import hu.szeba.hades.model.compiler.ProgramCompiler;
import hu.szeba.hades.model.task.data.TaskData;

public class Task {

    private final User user;
    private final TaskData data;
    private final ProgramCompiler programCompiler;
    private final String language;
    private final CompilerOutputRegister compilerOutputRegister;

    public Task(User user, TaskData data, ProgramCompiler programCompiler, String language) {
        this.user = user;
        this.data = data;
        this.programCompiler = programCompiler;
        this.language = language;
        compilerOutputRegister = new CompilerOutputRegister();
        compilerOutputRegister.setCompilerOutput(programCompiler.getCached(data.getTaskWorkingDirectory()));
    }

    public User getUser() {
        return user;
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

