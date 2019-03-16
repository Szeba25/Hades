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

    private Set<String> completedTaskCollections;
    private Set<String> completedTasks;
    private Set<String> startedTasks;
    private File userWorkingDirectoryPath;

    private TaskCollectionInfo currentTaskCollectionInfo;
    private String currentTaskCollectionFullId;
    private String currentTaskFullId;

    public User(String id, String name) throws IOException {
        this.id = id;
        this.name = name;

        completedTaskCollections = new HashSet<>();
        completedTasks = new HashSet<>();
        startedTasks = new HashSet<>();
        userWorkingDirectoryPath = new File(Options.getWorkingDirectoryPath(), id);

        if (!userWorkingDirectoryPath.exists()) {
            // Create the meta folder, (and the id folder in the meantime)
            FileUtils.forceMkdir(new File(Options.getWorkingDirectoryPath(), id + "/.meta"));
            // Create the completed_task_collections.txt file
            if (!new File(Options.getWorkingDirectoryPath(), id + "/.meta/completed_task_collections.txt").createNewFile()) {
                throw new IOException("Couldn't create completed_task_collections.txt for user: " + id);
            }
            // Create the completed_tasks.txt file
            if (!new File(Options.getWorkingDirectoryPath(), id + "/.meta/completed_tasks.txt").createNewFile()) {
                throw new IOException("Couldn't create completed_tasks.txt for user: " + id);
            }
            // Create the started_tasks.txt file
            if (!new File(Options.getWorkingDirectoryPath(), id + "/.meta/started_tasks.txt").createNewFile()) {
                throw new IOException("Couldn't create started_tasks.txt for user: " + id);
            }
        } else {
            // Load completed_task_collections.txt file!
            TabbedFile tFile = new TabbedFile(new File(userWorkingDirectoryPath, ".meta/completed_task_collections.txt"));
            for (int i = 0; i < tFile.getLineCount(); i++) {
                completedTaskCollections.add(tFile.getData(i, 0));
            }
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

        currentTaskCollectionInfo = null;
        currentTaskCollectionFullId = null;
        currentTaskFullId = null;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isTaskStarted(String taskFullId) {
        return startedTasks.contains(taskFullId);
    }

    public boolean isTaskCompleted(String taskFullId) {
        return completedTasks.contains(taskFullId);
    }

    @Override
    public void setCurrentTaskCollectionInfo(TaskCollectionInfo info) {
        currentTaskCollectionInfo = info;
    }

    @Override
    public synchronized void setCurrentTaskCollection(String taskCollectionFullId) {
        currentTaskCollectionFullId = taskCollectionFullId;
    }

    @Override
    public synchronized void setCurrentTask(String taskFullId) {
        currentTaskFullId = taskFullId;
    }

    @Override
    public synchronized boolean isCurrentTaskCompleted() {
        // Check in the set
        return completedTasks.contains(currentTaskFullId);
    }

    @Override
    public synchronized void markCurrentTaskAsCompleted() throws IOException {
        // Add only if does not exist!
        if (!completedTasks.contains(currentTaskFullId)) {
            // Complete the task
            completedTasks.add(currentTaskFullId);
            TabbedFile file = new TabbedFile(new File(userWorkingDirectoryPath, ".meta/completed_tasks.txt"));
            file.addData(currentTaskFullId);
            file.save();
            // A new task was completed, now check if we finished the task collection too!
            if (!completedTaskCollections.contains(currentTaskCollectionFullId)) {
                // First, count the completed tasks
                int count = 0;
                for (String taskFullId : completedTasks) {
                    if (currentTaskCollectionFullId.contains(taskFullId)) {
                        count++;
                    }
                }
                // Then check against threshold and complete the task collection too
                if (count >= currentTaskCollectionInfo.getTaskCompletionCount()) {
                    completedTaskCollections.add(currentTaskCollectionFullId);
                    TabbedFile file2 = new TabbedFile(new File(userWorkingDirectoryPath, ".meta/completed_task_collections.txt"));
                    file2.addData(currentTaskCollectionFullId);
                    file2.save();
                }
            }

        }
    }

    @Override
    public synchronized void markCurrentTaskAsStarted() throws IOException {
        // Add only if does not exist!
        if (!startedTasks.contains(currentTaskFullId)) {
            startedTasks.add(currentTaskFullId);
            TabbedFile file = new TabbedFile(new File(userWorkingDirectoryPath, ".meta/started_tasks.txt"));
            file.addData(currentTaskFullId);
            file.save();
        }
    }

    @Override
    public synchronized File getCurrentTaskWorkingDirectoryPath() {
        return new File(userWorkingDirectoryPath, currentTaskFullId);
    }

}
