package hu.szeba.hades.meta;

import hu.szeba.hades.io.TabbedFile;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class User {

    private String id;
    private String name;

    private Set<String> completedTasks;
    private File userWorkingDirectoryPath;

    public User(String id, String name) throws IOException {
        this.id = id;
        this.name = name;

        completedTasks = new HashSet<>();
        userWorkingDirectoryPath = new File(Options.getWorkingDirectoryPath(), id);

        if (!userWorkingDirectoryPath.exists()) {
            // Create the meta folder, (and the id folder in the meantime)
            FileUtils.forceMkdir(new File(Options.getWorkingDirectoryPath(), id + "/.meta"));
            // Create the progress.txt file
            if (!new File(Options.getWorkingDirectoryPath(), id + "/.meta/progress.txt").createNewFile()) {
                throw new IOException("Couldn't create progress.txt for user: " + id);
            }
        } else {
            // Load progress.txt file!
            TabbedFile file = new TabbedFile(new File(userWorkingDirectoryPath, ".meta/progress.txt"));
            for (int i = 0; i < file.getLineCount(); i++) {
                completedTasks.add(file.getData(i, 0));
            }
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

    public boolean progressExistsForTask(String courseName, String topicName, String taskId) {
        return new File(userWorkingDirectoryPath, courseName + "/" + topicName + "/" + taskId).exists();
    }

    public boolean isTaskCompleted(String courseName, String topicName, String taskId) {
        // Create the identifier string
        String identifierString = courseName + "/" + topicName + "/" + taskId;
        // Check in the set
        return completedTasks.contains(identifierString);
    }

    public void completeTask(String courseName, String topicName, String taskId) throws IOException {
        // Create the identifier string
        String identifierString = courseName + "/" + topicName + "/" + taskId;

        // Add only if does not exists!
        if (!completedTasks.contains(identifierString)) {
            completedTasks.add(identifierString);
            TabbedFile file = new TabbedFile(new File(userWorkingDirectoryPath, ".meta/progress.txt"));
            file.addData(identifierString);
            file.save();
        }
    }

}
