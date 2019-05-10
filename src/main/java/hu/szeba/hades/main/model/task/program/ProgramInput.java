package hu.szeba.hades.main.model.task.program;

import hu.szeba.hades.main.io.SingleDataFile;

import java.io.File;
import java.io.IOException;

public class ProgramInput {

    private SingleDataFile file;

    public ProgramInput(File inputFilePath) throws IOException {
        this.file = new SingleDataFile(inputFilePath);
    }

    public ProgramInput(ProgramInput other) {
        this.file = new SingleDataFile(other.file);
    }

    public SingleDataFile getFile() {
        return file;
    }

}
