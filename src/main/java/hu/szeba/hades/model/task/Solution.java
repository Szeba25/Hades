package hu.szeba.hades.model.task;

import hu.szeba.hades.model.task.program.ProgramInput;
import hu.szeba.hades.model.task.result.Result;

public class Solution {

    private ProgramInput programInput;
    private Result desiredResult;

    public Solution(ProgramInput programInput, Result desiredResult) {
        this.programInput = programInput;
        this.desiredResult = desiredResult;
    }

    public ProgramInput getProgramInput() {
        return programInput;
    }

    public Result getDesiredResult() {
        return desiredResult;
    }
}
