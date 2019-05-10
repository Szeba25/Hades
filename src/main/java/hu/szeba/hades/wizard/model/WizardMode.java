package hu.szeba.hades.wizard.model;

import hu.szeba.hades.main.io.ConfigFile;
import hu.szeba.hades.main.io.SingleDataFile;
import hu.szeba.hades.main.model.task.graph.Graph;
import hu.szeba.hades.main.model.task.graph.AdjacencyList;

import java.io.File;
import java.io.IOException;

public class WizardMode {

    private String modeId;

    private File modePath;

    private SingleDataFile titleFile;
    private ConfigFile metaFile;

    private Graph graph;

    public WizardMode(File modesPath, String modeId) throws IOException {
        this.modeId = modeId;
        this.modePath = new File(modesPath, modeId);

        titleFile = new SingleDataFile(new File(modePath, "title.dat"));
        metaFile = new ConfigFile(new File(modePath, "meta.conf"));
        graph = new AdjacencyList(new File(modePath, "task_collections.graph"));
    }

    public void fillWithDefaults() {
        titleFile.clear();
        titleFile.addData("");
        metaFile.clear();
        metaFile.addData("ignore_dependency", "false");
        metaFile.addData("ignore_story", "false");
        metaFile.addData("iron_man", "false");
        graph.clear();
    }

    public void save() throws IOException {
        titleFile.save();
        metaFile.save();
        graph.save();
    }

    public String getModeId() {
        return modeId;
    }

    public String getTitle() {
        return titleFile.getData(0, 0);
    }

    public boolean isIgnoreDependency() {
        return Boolean.parseBoolean(metaFile.getData(0, 1));
    }

    public boolean isIgnoreStory() {
        return Boolean.parseBoolean(metaFile.getData(1, 1));
    }

    public boolean isIronMan() {
        return Boolean.parseBoolean(metaFile.getData(2, 1));
    }

    public Graph getGraph() {
        return graph;
    }

    public void setTitle(String title) {
        titleFile.setData(0, 0, title);
    }

    public void setIgnoreDependency(boolean value) {
        metaFile.setData(0, 1, Boolean.toString(value));
    }

    public void setIgnoreStory(boolean value) {
        metaFile.setData(1, 1, Boolean.toString(value));
    }

    public void setIronMan(boolean value) {
        metaFile.setData(2, 1, Boolean.toString(value));
    }

}
