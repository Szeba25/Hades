package hu.szeba.hades.model.task;

import hu.szeba.hades.model.compiler.CompilerOutput;

public class CompilerOutputRegister {

    private CompilerOutput compilerOutput;

    public CompilerOutputRegister() {
        compilerOutput = null;
    }

    public void setCompilerOutput(CompilerOutput compilerOutput) {
        this.compilerOutput = compilerOutput;
    }

    public CompilerOutput getCompilerOutput() {
        return compilerOutput;
    }

}
