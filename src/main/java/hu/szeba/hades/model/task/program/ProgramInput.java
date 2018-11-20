package hu.szeba.hades.model.task.program;

import hu.szeba.hades.io.TabbedFile;

import java.io.File;
import java.io.IOException;

public class ProgramInput {

    private TabbedFile file;

    public ProgramInput(File inputFilePath) throws IOException {
        this.file = new TabbedFile(inputFilePath);
    }

    public TabbedFile getFile() {
        return file;
    }

}
