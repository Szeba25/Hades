package hu.szeba.hades.meta;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class User {

    private String id;
    private String name;
    private File userWorkingDirectoryPath;

    public User(String id, String name) throws IOException {
        this.id = id;
        this.name = name;
        userWorkingDirectoryPath = new File(Options.getWorkingDirectoryPath(), id);
        if (!userWorkingDirectoryPath.exists()) {
            FileUtils.forceMkdir(userWorkingDirectoryPath);
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public File getUserWorkingDirectoryPath() {
        return userWorkingDirectoryPath;
    }

}
