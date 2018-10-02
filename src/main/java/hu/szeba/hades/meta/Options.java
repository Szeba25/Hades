package hu.szeba.hades.meta;

import java.io.File;

public class Options {

    private static File compilerPath_C;
    private static File databasePath;
    private static File workingDirectoryPath;

    public static void initialize() {
        compilerPath_C = new File("D:/Egyetem/MinGW/bin");
        databasePath = new File("D:/Egyetem/Szakdolgozat/hades_Campaigns");
        workingDirectoryPath = new File("D:/Egyetem/Szakdolgozat/hades_WorkingDirectory");
        checkPaths();
    }

    public static File getCompilerPath_C() {
        return compilerPath_C;
    }

    public static File getDatabasePath() {
        return databasePath;
    }

    public static File getWorkingDirectoryPath() { return workingDirectoryPath; }

    private static void checkPaths() {
        System.out.println("Check -> Compiler path for C exists: " + compilerPath_C.exists());
        System.out.println("Check -> Campaign database path exists: " + databasePath.exists());
        System.out.println("Check -> Working directory exists: " + workingDirectoryPath.exists());
    }

}
