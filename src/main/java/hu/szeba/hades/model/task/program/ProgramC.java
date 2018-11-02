package hu.szeba.hades.model.task.program;

import hu.szeba.hades.model.task.result.Result;

import java.io.File;
import java.util.List;

public class ProgramC implements Program {

    private List<String> messages;

    @Override
    public Result run(File location, ProgramInput input) {
        return new Result();
    }

    @Override
    public void setCompileMessages(List<String> messages) {
        this.messages = messages;
    }

    @Override
    public List<String> getCompileMessages() {
        return messages;
    }

}
