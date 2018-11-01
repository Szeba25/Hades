package hu.szeba.hades.io;

import java.io.File;
import java.io.IOException;

public class ConfigFile extends DataFile {

    public ConfigFile(File file) throws IOException {
        super(file, "=");
    }

}
