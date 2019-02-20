package hu.szeba.hades.meta;

import hu.szeba.hades.io.ConfigFile;
import hu.szeba.hades.io.DataFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Options {

    private static Map<String, String> config;
    private static Map<String, File> paths;

    public static void initialize() throws IOException {
        config = new HashMap<>();
        DataFile configFile = new ConfigFile(new File("config/main.conf"));
        for (int i = 0; i < configFile.getLineCount(); i++) {
            config.put(configFile.getData(i, 0), configFile.getData(i, 1));
        }

        paths = new HashMap<>();
        DataFile pathsFile = new ConfigFile(new File("config/paths.conf"));
        for (int i = 0; i < pathsFile.getLineCount(); i++) {
            paths.put(pathsFile.getData(i, 0),
                    new File(pathsFile.getData(i, 1)));
        }
        checkPaths();
    }

    public static String getConfigData(String configDataIdentifier) {
        return config.get(configDataIdentifier);
    }

    public static int getConfigIntData(String configDataIdentifier) {
        return Integer.parseInt(config.get(configDataIdentifier));
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
