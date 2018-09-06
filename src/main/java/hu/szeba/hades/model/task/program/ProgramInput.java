package hu.szeba.hades.model.task.program;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProgramInput {

    private List<String> input;

    public ProgramInput(File inputFilePath) {
        input = new ArrayList<>();
    }

    public List<String> getInput() {
        return input;
    }
}
