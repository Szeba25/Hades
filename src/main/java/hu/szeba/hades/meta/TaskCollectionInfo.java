package hu.szeba.hades.meta;

public class TaskCollectionInfo {

    private int taskCount;
    private double threshold;

    public TaskCollectionInfo(int taskCount, double threshold) {
        this.taskCount = taskCount;
        this.threshold = threshold;
    }

    public int getTaskCount() {
        return taskCount;
    }

    public double getThreshold() {
        return threshold;
    }

}
