package hu.szeba.hades.model.compiler;

import hu.szeba.hades.model.task.program.Program;

import java.util.List;

public class CompilerOutput {

    private List<String> compileMessages;
    private Program program;

    public CompilerOutput(List<String> compileMessages, Program program) {
        this.compileMessages = compileMessages;
        this.program = program;
    }

    public List<String> getCompileMessages() {
        return compileMessages;
    }

    public boolean isReady() {
        return program != null;
    }

    public Program getProgram() {
        return program;
    }

}
