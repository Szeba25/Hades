package hu.szeba.hades.model.helper;

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

}
