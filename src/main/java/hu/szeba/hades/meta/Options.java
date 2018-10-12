package hu.szeba.hades.meta;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Options {

    private static Map<String, File> paths;
    private static File databasePath;
    private static File workingDirectoryPath;

    public static void initialize() {
        paths = new HashMap<>();
        paths.put("compiler_c", new File("C:/Users/Zsuzsy/Desktop/Szeba/MinGW"));
        databasePath = new File("D:/Egyetem/Szakdolgozat/hades_Database");
        workingDirectoryPath = new File("D:/Egyetem/Szakdolgozat/hades_WorkingDirectory");
        checkPaths();
    }

    public static File getPathTo(String location) {
        return paths.get(location);
    }

    public static File getDatabasePath() {
        return databasePath;
    }

    public static File getWorkingDirectoryPath() { return workingDirectoryPath; }

    private static void checkPaths() {
        System.out.println("Check -> Campaign database path exists: " + databasePath.exists());
        System.out.println("Check -> Working directory exists: " + workingDirectoryPath.exists());
        paths.forEach((location, file) -> System.out.println("Check -> " + location + " exists: " + file.exists()));
    }

}
