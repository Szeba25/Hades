package hu.szeba.hades.meta;

public class TaskCollectionInfo {

    private int taskCount;
    private double completionThreshold;

    public TaskCollectionInfo(int taskCount, double completionThreshold) {
        this.taskCount = taskCount;
        this.completionThreshold = completionThreshold;
    }

    public int getTaskCompletionCount() {
        return (int)(taskCount * completionThreshold);
    }

}
