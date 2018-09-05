package hu.szeba.hades.model.task.program;

import hu.szeba.hades.model.task.result.Result;

public interface Program {

    Result run(ProgramInput input);

}
