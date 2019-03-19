package hu.szeba.hades.model.compiler;

import hu.szeba.hades.model.task.program.Program;

import java.util.List;

public class CompilerOutput {

    private List<String> compilerMessages;
    private int exitValue;
    private Program program;

    public CompilerOutput(List<String> compilerMessages, int exitValue, Program program) {
        this.compilerMessages = compilerMessages;
        this.exitValue = exitValue;
        this.program = program;
    }

    public List<String> getCompilerMessages() {
        return compilerMessages;
    }

    public int getExitValue() {
        return exitValue;
    }

    public boolean isReady() {
        return program != null;
    }

    public Program getProgram() {
        return program;
    }

}
