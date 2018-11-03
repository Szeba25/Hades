package hu.szeba.hades.model.compiler;

import hu.szeba.hades.model.task.program.Program;

import java.util.List;

public class CompilerOutput {

    private List<String> compilerMessages;
    private Program program;

    public CompilerOutput(List<String> compilerMessages, Program program) {
        this.compilerMessages = compilerMessages;
        this.program = program;
    }

    public List<String> getCompilerMessages() {
        return compilerMessages;
    }

    public boolean isReady() {
        return program != null;
    }

    public Program getProgram() {
        return program;
    }

}
