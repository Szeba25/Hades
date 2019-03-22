package hu.szeba.hades.wizard.model;

import hu.szeba.hades.main.io.ConfigFile;
import hu.szeba.hades.main.io.GraphFile;
import hu.szeba.hades.main.io.GraphViewFile;
import hu.szeba.hades.main.io.TabbedFile;

import java.io.File;
import java.io.IOException;

public class WizardMode {

    private String modeId;

    private File modePath;

    private TabbedFile titleFile;
    private ConfigFile metaFile;
    private GraphViewFile graphView;
    private GraphFile graph;

    public WizardMode(File modesPath, String modeId) throws IOException {
        this.modeId = modeId;
        this.modePath = new File(modesPath, modeId);

        titleFile = new TabbedFile(new File(modePath, "title.dat"));
        metaFile = new ConfigFile(new File(modePath, "meta.conf"));
        graphView = new GraphViewFile(new File(modePath, "task_collections.graph.view"));
        graph = new GraphFile(new File(modePath, "task_collections.graph"));
    }

    public void save() throws IOException {
        titleFile.save();
        metaFile.save();
        graphView.save();
        graph.save();
    }

}
