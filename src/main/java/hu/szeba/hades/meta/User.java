package hu.szeba.hades.meta;

import hu.szeba.hades.io.TabbedFile;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class User implements TaskSolverAgent {

    private String id;
    private String name;

    private Set<String> completedTasks;
    private Set<String> startedTasks;
    private File userWorkingDirectoryPath;

    public User(String id, String name) throws IOException {
        this.id = id;
        this.name = name;

        completedTasks = new HashSet<>();
        startedTasks = new HashSet<>();
        userWorkingDirectoryPath = new File(Options.getWorkingDirectoryPath(), id);

        if (!userWorkingDirectoryPath.exists()) {
            // Create the meta folder, (and the id folder in the meantime)
            FileUtils.forceMkdir(new File(Options.getWorkingDirectoryPath(), id + "/.meta"));
            // Create the completed_tasks.txt file
            if (!new File(Options.getWorkingDirectoryPath(), id + "/.meta/completed_tasks.txt").createNewFile()) {
                throw new IOException("Couldn't create completed_tasks.txt for user: " + id);
            }
            // Create the started_tasks.txt file
            if (!new File(Options.getWorkingDirectoryPath(), id + "/.meta/started_tasks.txt").createNewFile()) {
                throw new IOException("Couldn't create started_tasks.txt for user: " + id);
            }
        } else {
            // Load completed_tasks.txt file!
            TabbedFile cFile = new TabbedFile(new File(userWorkingDirectoryPath, ".meta/completed_tasks.txt"));
            for (int i = 0; i < cFile.getLineCount(); i++) {
                completedTasks.add(cFile.getData(i, 0));
            }
            // Load started_tasks.txt file!
            TabbedFile pFile = new TabbedFile(new File(userWorkingDirectoryPath, ".meta/started_tasks.txt"));
            for (int i = 0; i < pFile.getLineCount(); i++) {
                startedTasks.add(pFile.getData(i, 0));
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

    public boolean isTaskStarted(String identifierString) {
        // Optimized (cached) version
        return startedTasks.contains(identifierString);
    }

    @Override
    public synchronized boolean isTaskCompleted(String identifierString) {
        // Check in the set
        return completedTasks.contains(identifierString);
    }

    @Override
    public synchronized void markTaskAsCompleted(String identifierString) throws IOException {
        // Add only if does not exist!
        if (!completedTasks.contains(identifierString)) {
            completedTasks.add(identifierString);
            TabbedFile file = new TabbedFile(new File(userWorkingDirectoryPath, ".meta/completed_tasks.txt"));
            file.addData(identifierString);
            file.save();
        }
    }

    public void markTaskAsStarted(String identifierString) throws IOException {
        // Add only if does not exist!
        if (!startedTasks.contains(identifierString)) {
            startedTasks.add(identifierString);
            TabbedFile file = new TabbedFile(new File(userWorkingDirectoryPath, ".meta/started_tasks.txt"));
            file.addData(identifierString);
            file.save();
        }
    }

}
