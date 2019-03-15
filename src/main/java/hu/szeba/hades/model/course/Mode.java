package hu.szeba.hades.model.course;

import hu.szeba.hades.io.GraphFile;
import hu.szeba.hades.io.TabbedFile;
import hu.szeba.hades.meta.Options;
import hu.szeba.hades.meta.User;
import hu.szeba.hades.model.task.graph.AdjacencyMatrix;
import hu.szeba.hades.view.MappedElement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Mode {

    private User user;
    private String courseId;
    private String modeId;
    private File modeDirectory;
    private AdjacencyMatrix taskCollectionMatrix;
    private List<MappedElement> possibleTaskCollections;
    private List<String> unavailableTaskCollectionIds;

    private final String language;

    public Mode(User user, String courseId, String modeId, String language) throws IOException {
        this.user = user;
        this.courseId = courseId;
        this.modeId = modeId;

        this.modeDirectory = new File(Options.getDatabasePath(), courseId + "/modes/" + modeId);
        GraphFile graphFile = new GraphFile(new File(modeDirectory, "task_collections.graph"));
        this.taskCollectionMatrix = new AdjacencyMatrix(graphFile.getTuples());

        possibleTaskCollections = new ArrayList<>();
        for (String id : taskCollectionMatrix.getNodeNames()) {
            TabbedFile metaFile = new TabbedFile(new File(modeDirectory, "title.dat"));
            possibleTaskCollections.add(new MappedElement(id, metaFile.getData(0, 0)));
        }

        unavailableTaskCollectionIds = new ArrayList<>();

        this.language = language;
    }

}
