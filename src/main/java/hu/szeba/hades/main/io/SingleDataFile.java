package hu.szeba.hades.main.io;

import java.io.File;
import java.io.IOException;

public class SingleDataFile extends DataFile {

    public SingleDataFile(File file) throws IOException {
        super(file, "\t");
    }

    public SingleDataFile(SingleDataFile file) {
        super(file);
    }

}
