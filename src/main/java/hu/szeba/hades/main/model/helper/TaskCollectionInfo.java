package hu.szeba.hades.main.model.helper;

public class TaskCollectionInfo {

    private int taskCount;
    private double completionThreshold;

    public TaskCollectionInfo(int taskCount, double completionThreshold) {
        this.taskCount = taskCount;
        this.completionThreshold = completionThreshold;
    }

    public int getTaskCompletionCount() {
        return (int)Math.ceil((double)taskCount * completionThreshold);
    }

    public double getCompletionThreshold() {
        return completionThreshold;
    }
}
