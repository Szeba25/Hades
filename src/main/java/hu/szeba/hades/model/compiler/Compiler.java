package hu.szeba.hades.model.compiler;

import java.io.File;

public abstract class Compiler {

    private File compilerPath;

    public Compiler(File compilerPath) {
        this.compilerPath = compilerPath;
    }

}
