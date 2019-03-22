package hu.szeba.hades.wizard.model;

import hu.szeba.hades.main.io.ConfigFile;
import hu.szeba.hades.main.io.GraphFile;
import hu.szeba.hades.main.io.TabbedFile;
import hu.szeba.hades.main.model.task.graph.AdjacencyMatrix;
import hu.szeba.hades.main.model.task.graph.Tuple;
import hu.szeba.hades.wizard.view.elements.GraphNode;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class WizardMode {

    private String modeId;

    private File modePath;

    private TabbedFile titleFile;
    private ConfigFile metaFile;
    private GraphFile graph;

    private AdjacencyMatrix adjacencyMatrix;

    public WizardMode(File modesPath, String modeId) throws IOException {
        this.modeId = modeId;
        this.modePath = new File(modesPath, modeId);

        titleFile = new TabbedFile(new File(modePath, "title.dat"));
        metaFile = new ConfigFile(new File(modePath, "meta.conf"));
        graph = new GraphFile(new File(modePath, "task_collections.graph"));

        adjacencyMatrix = new AdjacencyMatrix(this.graph.getTuples());
    }

    public void save() throws IOException {
        titleFile.save();
        metaFile.save();
        graph.save();
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

    public Map<String, GraphNode> getGraphViewData() {
        return graph.getViewData();
    }

    public AdjacencyMatrix getAdjacencyMatrix() {
        return adjacencyMatrix;
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

    public void setGraphData(List<Tuple> tuples) {
        this.graph.setTuples(tuples);
        this.adjacencyMatrix = new AdjacencyMatrix(this.graph.getTuples());
    }

    public void setGraphViewData(Map<String, GraphNode> data) {
        this.graph.setViewData(data);
    }

}
