package hu.szeba.hades.main.controller;

import hu.szeba.hades.main.model.compiler.CompilerOutput;
import hu.szeba.hades.main.model.compiler.ProgramCompiler;

import java.io.File;
import java.io.IOException;

public class TaskCompilerWork implements Work {

    private ProgramCompiler compiler;
    private String[] sources;
    private File path;

    private CompilerOutput output;

    public TaskCompilerWork(ProgramCompiler compiler,
                            String[] sources,
                            File path) {
        this.compiler = compiler;
        this.sources = sources;
        this.path = path;
        output = null;
    }

    @Override
    public void execute(Publisher publisher) throws IOException, InterruptedException {
        publisher.customPublish(">>> Compilation started...\n\n");
        output = compiler.compile(sources, path);
        for (String message : output.getCompilerMessages()) {
            publisher.customPublish("@" + message + "\n");
        }
    }

    public CompilerOutput getOutput() {
        return output;
    }

}
