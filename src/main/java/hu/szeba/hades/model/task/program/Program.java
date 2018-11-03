package hu.szeba.hades.model.task.program;

import hu.szeba.hades.model.task.result.Result;

import java.io.File;
import java.util.List;

public interface Program {

    Result run(File location, ProgramInput input);
    void setCompileMessages(List<String> messages);
    List<String> getCompileMessages();

}
