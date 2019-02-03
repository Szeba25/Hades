package hu.szeba.hades.model.task.program;

import hu.szeba.hades.model.task.result.Result;

import java.io.File;
import java.io.IOException;

public abstract class Program {

    protected File location;

    public Program(File location) {
        this.location = location;
    }

    public abstract Result run(ProgramInput input, int maxResultLineCount) throws IOException;

}
