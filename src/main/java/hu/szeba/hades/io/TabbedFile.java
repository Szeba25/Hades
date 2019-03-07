package hu.szeba.hades.io;

import java.io.File;
import java.io.IOException;

public class TabbedFile extends DataFile {

    public TabbedFile(File file) throws IOException {
        super(file, "\t");
    }

    public TabbedFile(TabbedFile file) {
        super(file);
    }

}
