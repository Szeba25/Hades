package hu.szeba.hades.controller.task;

public class ProcessCache {

    private Process process;

    public ProcessCache() {
        this.process = null;
    }

    public synchronized void setProcess(Process process) {
        this.process = process;
    }

    public synchronized void stopProcess() {
        if (process != null) {
            process.destroy();
        }
    }

    public synchronized void clearProcess() {
        process = null;
    }

}
