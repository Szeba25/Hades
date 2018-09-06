package hu.szeba.hades.meta;

import java.io.File;

public class Options {

    private File compilerPath_C;
    private File compilerPath_CPP;
    private File campaignDatabasePath;
    private File workingDirectoryPath;

    public Options(File compilerPath_C,
                   File compilerPath_CPP,
                   File campaignDatabasePath,
                   File workingDirectoryPath) {
        this.compilerPath_C = compilerPath_C;
        this.compilerPath_CPP = compilerPath_CPP;
        this.campaignDatabasePath = campaignDatabasePath;
        this.workingDirectoryPath = workingDirectoryPath;
    }

    public void checkPaths() {
        System.out.println(compilerPath_C.exists());
        System.out.println(campaignDatabasePath.exists());
        System.out.println(workingDirectoryPath.exists());
    }

    public File getCompilerPath_C() {
        return compilerPath_C;
    }

    public File getCompilerPath_CPP() {
        return compilerPath_CPP;
    }

    public File getCampaignDatabasePath() {
        return campaignDatabasePath;
    }

    public File getWorkingDirectoryPath() {
        return workingDirectoryPath;
    }
}
