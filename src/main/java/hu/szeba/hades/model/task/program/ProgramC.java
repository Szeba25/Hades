package hu.szeba.hades.model.task.program;

import hu.szeba.hades.model.task.result.Result;

import java.io.File;

public class ProgramC implements Program {

    @Override
    public Result run(File location, ProgramInput input) {
        return new Result();
    }

}
