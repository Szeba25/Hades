package hu.szeba.hades.meta;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class User {

    private String id;
    private String name;
    private File userWorkingDirectory;

    public User(String id, String name) throws IOException {
        this.id = id;
        this.name = name;
        userWorkingDirectory = new File(Options.getWorkingDirectoryPath(), id);
        if (!userWorkingDirectory.exists()) {
            FileUtils.forceMkdir(userWorkingDirectory);
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public File getUserWorkingDirectory() {
        return userWorkingDirectory;
    }

}
