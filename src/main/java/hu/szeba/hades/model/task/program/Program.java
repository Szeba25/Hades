package hu.szeba.hades.model.task.program;

import hu.szeba.hades.model.task.result.Result;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class Program {

    protected File location;

    public Program(File location) {
        this.location = location;
    }

    public abstract Result run(ProgramInput input) throws IOException, InterruptedException;
    public abstract void setCompileMessages(List<String> messages);
    public abstract List<String> getCompileMessages();
    public abstract boolean isReady();
}
