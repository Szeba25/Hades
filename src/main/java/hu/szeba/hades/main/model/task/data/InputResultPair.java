package hu.szeba.hades.main.model.task.data;

import hu.szeba.hades.main.model.task.program.ProgramInput;
import hu.szeba.hades.main.model.task.result.Result;

public class InputResultPair {

    private final ProgramInput programInput;
    private final Result desiredResult;

    public InputResultPair(ProgramInput programInput, Result desiredResult) {
        this.programInput = programInput;
        this.desiredResult = desiredResult;
    }

    public InputResultPair(InputResultPair other) {
        this.programInput = new ProgramInput(other.programInput);
        this.desiredResult = new Result(other.desiredResult);
    }

    public ProgramInput getProgramInput() {
        return programInput;
    }

    public Result getDesiredResult() {
        return desiredResult;
    }
}
