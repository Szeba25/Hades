package hu.szeba.hades.main.model.task.program;

import hu.szeba.hades.main.model.task.result.Result;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Program {

    protected File location;

    public Program(File location) {
        this.location = location;
    }

    public abstract Result run(ProgramInput input, int maxByteCount, AtomicBoolean stopFlag)
            throws IOException, InterruptedException;

}
