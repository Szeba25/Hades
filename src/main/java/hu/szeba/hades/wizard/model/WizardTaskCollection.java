package hu.szeba.hades.wizard.model;

import hu.szeba.hades.main.io.ConfigFile;
import hu.szeba.hades.main.io.TabbedFile;
import hu.szeba.hades.main.model.task.graph.AdjacencyList;
import hu.szeba.hades.main.model.task.graph.Graph;

import java.io.File;
import java.io.IOException;

public class WizardTaskCollection {

    private String taskCollectionId;

    private File taskCollectionPath;

    private TabbedFile titleFile;
    private ConfigFile metaFile;
    private Graph graph;

    public WizardTaskCollection(File taskCollectionsPath, String taskCollectionId) throws IOException {
        this.taskCollectionId = taskCollectionId;
        this.taskCollectionPath = new File(taskCollectionsPath, taskCollectionId);

        titleFile = new TabbedFile(new File(taskCollectionPath, "title.dat"));
        metaFile = new ConfigFile(new File(taskCollectionPath, "meta.conf"));
        graph = new AdjacencyList(new File(taskCollectionPath, "tasks.graph"));
    }

    public void fillWithDefaults() {
        titleFile.clear();
        titleFile.addData("");
        metaFile.clear();
        metaFile.addData("completion_threshold", "1.0");
        graph.clear();
    }

    public void save() throws IOException {
        titleFile.save();
        metaFile.save();
        graph.save();
    }

    public String getTitle() {
        return titleFile.getData(0, 0);
    }

    public int getCompletionThreshold() {
        return (int) (Double.parseDouble(metaFile.getData(0, 1)) * 100);
    }

    public Graph getGraph() {
        return graph;
    }

    public void setTitle(String title) {
        titleFile.setData(0, 0, title);
    }

    public void setCompletionThreshold(int threshold) {
        metaFile.setData(0, 1, String.valueOf((double)threshold / 100.0));
    }

}
