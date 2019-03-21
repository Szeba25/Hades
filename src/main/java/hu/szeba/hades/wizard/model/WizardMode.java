package hu.szeba.hades.wizard.model;

import hu.szeba.hades.main.io.ConfigFile;
import hu.szeba.hades.main.io.GraphFile;
import hu.szeba.hades.main.io.GraphViewFile;
import hu.szeba.hades.main.io.TabbedFile;
import hu.szeba.hades.main.model.helper.ModeData;

import java.io.File;
import java.io.IOException;

public class WizardMode {

    private String modeId;
    private String modeTitle;

    private File modePath;

    private ModeData modeData;
    private GraphViewFile graphView;
    private GraphFile graph;

    public WizardMode(File modesPath, String modeId) throws IOException {
        this.modeId = modeId;
        this.modePath = new File(modesPath, modeId);

        TabbedFile titleFile = new TabbedFile(new File(modePath, "title.dat"));
        this.modeTitle = titleFile.getData(0, 0);

        ConfigFile file = new ConfigFile(new File(modePath, "meta.conf"));
        this.modeData = new ModeData(
                Boolean.parseBoolean(file.getData(0, 1)),
                Boolean.parseBoolean(file.getData(1, 1)),
                Boolean.parseBoolean(file.getData(2, 1)));

        graphView = new GraphViewFile(new File(modePath, "task_collections.graph.view"));
        graph = new GraphFile(new File(modePath, "task_collections.graph"));
    }

}
