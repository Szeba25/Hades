package hu.szeba.hades.meta;

import org.apache.commons.io.filefilter.DirectoryFileFilter;

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

        String[] directories = campaignDatabasePath.list(DirectoryFileFilter.INSTANCE);
        for (int i = 0; i < directories.length; i++) {
            System.out.println(directories[i]);
        }

    }

}
