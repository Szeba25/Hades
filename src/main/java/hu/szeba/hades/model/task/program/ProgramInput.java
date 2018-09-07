package hu.szeba.hades.model.task.program;

import java.io.File;

public class ProgramInput {

    private String input;

    public ProgramInput(File inputFilePath) {
        input = ""; // TODO: Load from file!
    }

    public String getInput() {
        return input;
    }
}
