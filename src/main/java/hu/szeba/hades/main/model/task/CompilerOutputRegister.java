package hu.szeba.hades.main.model.task;

import hu.szeba.hades.main.model.compiler.CompilerOutput;

public class CompilerOutputRegister {

    private CompilerOutput compilerOutput;

    public CompilerOutputRegister() {
        compilerOutput = null;
    }

    public synchronized void setCompilerOutput(CompilerOutput compilerOutput) {
        this.compilerOutput = compilerOutput;
    }

    public synchronized CompilerOutput getCompilerOutput() {
        return compilerOutput;
    }

}
