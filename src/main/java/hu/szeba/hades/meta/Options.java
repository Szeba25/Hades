package hu.szeba.hades.meta;

import hu.szeba.hades.io.DataFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Options {

    private static Map<String, File> paths;

    public static void initialize() throws IOException {
        paths = new HashMap<>();
        DataFile pathsFile = new DataFile(new File("hades_paths.dat"));
        for (int i = 0; i < pathsFile.getLineCount(); i++) {
            paths.put(pathsFile.getData(i, 0),
                    new File(pathsFile.getData(i, 1)));
        }
        checkPaths();
    }

    public static File getPathTo(String location) {
        return paths.get(location);
    }

    public static File getDatabasePath() {
        return paths.get("database");
    }

    public static File getWorkingDirectoryPath() { return paths.get("working_directory"); }

    private static void checkPaths() {
        paths.forEach((location, file) -> System.out.println("Check -> " + location + " exists: " + file.exists()));
    }

}
