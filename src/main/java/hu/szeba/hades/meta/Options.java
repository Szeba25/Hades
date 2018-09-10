package hu.szeba.hades.meta;

import java.io.File;

public class Options {

    private static File compilerPath_C;
    private static File campaignDatabasePath;
    private static File workingDirectoryPath;

    public static void initialize() {
        compilerPath_C = new File("D:/Egyetem/MinGW/bin");
        campaignDatabasePath = new File("D:/Egyetem/Szakdolgozat/hades_Campaigns");
        workingDirectoryPath = new File("D:/Egyetem/Szakdolgozat/hades_WorkingDirectory");
        checkPaths();
    }

    public static File getCompilerPath_C() {
        return compilerPath_C;
    }

    public static File getCampaignDatabasePath() {
        return campaignDatabasePath;
    }

    public static File getWorkingDirectoryPath() { return workingDirectoryPath; }

    private static void checkPaths() {
        System.out.println("Check -> Compiler path for C exists: " + compilerPath_C.exists());
        System.out.println("Check -> Campaign database path exists: " + campaignDatabasePath.exists());
        System.out.println("Check -> Working directory exists: " + workingDirectoryPath.exists());
    }

}
