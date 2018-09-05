package hu.szeba.hades.meta;

import java.io.File;

public class Options {

    private File compilerPath;
    private File campaignDatabasePath;
    private File workingDirectoryPath;

    public Options(File compilerPath, File campaignDatabasePath, File workingDirectoryPath) {
        this.compilerPath = compilerPath;
        this.campaignDatabasePath = campaignDatabasePath;
        this.workingDirectoryPath = workingDirectoryPath;
    }

    public void checkPaths() {
        System.out.println(compilerPath.exists());
        System.out.println(campaignDatabasePath.exists());
        System.out.println(workingDirectoryPath.exists());
    }

    public File getCompilerPath() {
        return compilerPath;
    }

    public File getCampaignDatabasePath() {
        return campaignDatabasePath;
    }

    public File getWorkingDirectoryPath() {
        return workingDirectoryPath;
    }
}
