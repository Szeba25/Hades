package hu.szeba.hades.main.model.task.graph;

import hu.szeba.hades.main.io.DataFile;
import hu.szeba.hades.main.io.ViewNode;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AdjacencyList implements Graph {

    private File dataFilePath;
    private Set<String> nodes;
    private Map<String, Set<String>> childNodes;
    private Map<String, Set<String>> parentNodes;
    private Map<String, ViewNode> viewNodes;

    public AdjacencyList(File dataFilePath) throws IOException {
        // Load the file
        this.dataFilePath = dataFilePath;
        Map<String, Set<String>> connections = loadConnections();

        // Create graph structure
        nodes = new HashSet<>();
        childNodes = new HashMap<>();
        parentNodes = new HashMap<>();

        nodes.addAll(connections.keySet());
        childNodes.putAll(connections);

        // Create parent nodes
        for (String node : connections.keySet()) {
            parentNodes.put(node, new HashSet<>());
        }
        for (String node : connections.keySet()) {
            Set<String> children = connections.get(node);
            for (String child : children) {
                parentNodes.get(child).add(node);
            }
        }

        // And load view nodes
        this.viewNodes = loadViewNodes();
    }

    private Map<String, Set<String>> loadConnections() throws IOException {
        DataFile dataFile = new DataFile(dataFilePath);
        Map<String, Set<String>> connections = new HashMap<>();
        for (int i = 0; i < dataFile.getLineCount(); i++) {
            Set<String> set = new HashSet<>();
            for (int j = 1; j < dataFile.getDataCount(i); j++) {
                set.add(dataFile.getData(i, j));
            }
            connections.put(dataFile.getData(i, 0), set);
        }
        return connections;
    }

    private Map<String, ViewNode> loadViewNodes() throws IOException {
        DataFile viewFile = new DataFile(new File(dataFilePath.getAbsolutePath() + ".view"));
        Map<String, ViewNode> data = new HashMap<>();
        for (int i = 0; i < viewFile.getLineCount(); i++) {
            data.put(viewFile.getData(i, 0), new ViewNode(viewFile.getLine(i)));
        }
        return data;
    }

    @Override
    public void save() throws IOException {
        DataFile dataFile = new DataFile(dataFilePath);
        dataFile.clear();
        for (String node : childNodes.keySet()) {
            Set<String> children = childNodes.get(node);
            String[] data = new String[children.size() + 1];
            data[0] = node;
            int dataIndex = 1;
            for (String child : children) {
                data[dataIndex] = child;
                dataIndex++;
            }
            dataFile.addData(data);
        }
        dataFile.save();

        DataFile viewFile = new DataFile(new File(dataFilePath.getAbsolutePath() + ".view"));
        viewFile.clear();
        for (ViewNode viewData : viewNodes.values()) {
            viewFile.addData(viewData.getDataArray());
        }
        viewFile.save();
    }

    @Override
    public Set<String> getNodes() {
        return nodes;
    }

    @Override
    public Set<String> getChildNodes(String node) {
        return childNodes.get(node);
    }

    @Override
    public Set<String> getParentNodes(String node) {
        return parentNodes.get(node);
    }

    @Override
    public void addNode(String node) {
        if (!nodes.contains(node)) {
            nodes.add(node);
            childNodes.put(node, new HashSet<>());
            parentNodes.put(node, new HashSet<>());
        }
    }

    @Override
    public void removeNode(String node) {
        if (nodes.contains(node)) {
            nodes.remove(node);
            childNodes.remove(node);
            parentNodes.remove(node);
        }
    }

    @Override
    public boolean containsNode(String node) {
        return nodes.contains(node);
    }

    @Override
    public void addConnection(String node, String connection) {
        if (nodes.contains(node) && nodes.contains(connection)) {
            childNodes.get(node).add(connection);
            parentNodes.get(connection).add(node);
        }
    }

    @Override
    public void removeConnection(String node, String connection) {
        if (nodes.contains(node) && nodes.contains(connection)) {
            childNodes.get(node).remove(connection);
            parentNodes.get(connection).remove(node);
        }
    }

    @Override
    public void removeAllConnectionFrom(String node) {
        Set<String> children = new HashSet<>(childNodes.get(node));
        for (String child : children) {
            removeConnection(node, child);
        }
    }

    @Override
    public void removeAllConnectionTo(String node) {
        for (String other : nodes) {
            removeConnection(other, node);
        }
    }

    @Override
    public ViewNode getViewNode(String node) {
        return viewNodes.get(node);
    }

    @Override
    public void addViewNode(ViewNode viewNode) {
        viewNodes.put(viewNode.getId(), viewNode);
    }

    @Override
    public void removeViewNode(String node) {
        viewNodes.remove(node);
    }

    @Override
    public void clear() {
        nodes.clear();
        childNodes.clear();
        parentNodes.clear();
        viewNodes.clear();
    }

}
