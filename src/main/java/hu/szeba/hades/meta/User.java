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

    private String currentTaskCollectionFullIdentifier;
    private String currentTaskFullIdentifier;

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

        currentTaskCollectionFullIdentifier = null;
        currentTaskFullIdentifier = null;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isTaskStarted(String taskFullIdentifier) {
        return startedTasks.contains(taskFullIdentifier);
    }

    public boolean isTaskCompleted(String taskFullIdentifier) {
        return completedTasks.contains(taskFullIdentifier);
    }

    @Override
    public synchronized void setCurrentTaskCollection(String taskCollectionFullIdentifier) {
        currentTaskCollectionFullIdentifier = taskCollectionFullIdentifier;
    }

    @Override
    public synchronized void setCurrentTask(String taskFullIdentifier) {
        currentTaskFullIdentifier = taskFullIdentifier;
    }

    @Override
    public synchronized boolean isCurrentTaskCompleted() {
        // Check in the set
        return completedTasks.contains(currentTaskFullIdentifier);
    }

    @Override
    public synchronized void markCurrentTaskAsCompleted() throws IOException {
        // Add only if does not exist!
        if (!completedTasks.contains(currentTaskFullIdentifier)) {
            completedTasks.add(currentTaskFullIdentifier);
            TabbedFile file = new TabbedFile(new File(userWorkingDirectoryPath, ".meta/completed_tasks.txt"));
            file.addData(currentTaskFullIdentifier);
            file.save();
        }
    }

    @Override
    public synchronized void markCurrentTaskAsStarted() throws IOException {
        // Add only if does not exist!
        if (!startedTasks.contains(currentTaskFullIdentifier)) {
            startedTasks.add(currentTaskFullIdentifier);
            TabbedFile file = new TabbedFile(new File(userWorkingDirectoryPath, ".meta/started_tasks.txt"));
            file.addData(currentTaskFullIdentifier);
            file.save();
        }
    }

    @Override
    public File getCurrentTaskWorkingDirectoryPath() {
        return new File(userWorkingDirectoryPath, currentTaskFullIdentifier);
    }

}
