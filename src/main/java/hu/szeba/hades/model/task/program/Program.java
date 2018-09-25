package hu.szeba.hades.model.task.program;

import hu.szeba.hades.model.task.result.Result;

import java.io.File;

public interface Program {

    Result run(File location, ProgramInput input);

}
