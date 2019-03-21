package hu.szeba.hades.main.model.helper;

public class ModeData {

    private boolean ignoreDependency;
    private boolean ignoreStory;
    private boolean ironMan;

    public ModeData(boolean ignoreDependency, boolean ignoreStory, boolean ironMan) {
        this.ignoreDependency = ignoreDependency;
        this.ignoreStory = ignoreStory;
        this.ironMan = ironMan;
    }

    public boolean isIgnoreDependency() {
        return ignoreDependency;
    }

    public boolean isIgnoreStory() {
        return ignoreStory;
    }

    public boolean isIronMan() {
        return ironMan;
    }

    public void setIgnoreDependency(boolean ignoreDependency) {
        this.ignoreDependency = ignoreDependency;
    }

    public void setIgnoreStory(boolean ignoreStory) {
        this.ignoreStory = ignoreStory;
    }

    public void setIronMan(boolean ironMan) {
        this.ironMan = ironMan;
    }
}
