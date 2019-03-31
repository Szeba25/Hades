package hu.szeba.hades.main.model.task.graph;

import hu.szeba.hades.main.io.ViewNode;

import java.io.IOException;
import java.util.Set;

public interface Graph {

    void save() throws IOException;

    Set<String> getNodes();
    Set<String> getChildNodes(String node);
    Set<String> getParentNodes(String node);

    void addNode(String node);
    void removeNode(String node);
    boolean containsNode(String node);
    void addConnection(String node, String connection);
    void removeConnection(String node, String connection);
    void removeAllConnectionFrom(String node);
    void removeAllConnectionTo(String node);

    ViewNode getViewNode(String node);
    void addViewNode(ViewNode viewNode);
    void removeViewNode(String node);

    void clear();
}
