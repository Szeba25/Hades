package hu.szeba.hades.main.model.task.graph;

import java.util.List;

public interface Graph {

    List<String> getParentNodes(String node);
    List<String> getChildNodes(String node);
    List<String> getNodes();

}
