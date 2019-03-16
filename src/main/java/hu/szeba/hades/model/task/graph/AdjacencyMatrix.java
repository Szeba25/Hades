package hu.szeba.hades.model.task.graph;

import java.util.*;

public class AdjacencyMatrix {

    private Map<String, Integer> indexByNodes;
    private String[] nodesByIndex;
    private boolean[][] edges;

    public AdjacencyMatrix(List<Tuple> tuples) {
        createIndexByNodes(tuples);
        createNodesByIndex();
        setEdges(tuples);
    }

    private void createIndexByNodes(List<Tuple> tuples) {
        indexByNodes = new HashMap<>();
        for (Tuple tuple : tuples) {
            if (!indexByNodes.containsKey(tuple.getElement1())) {
                indexByNodes.put(tuple.getElement1(), getSize());
            }
            if (!indexByNodes.containsKey(tuple.getElement2())) {
                indexByNodes.put(tuple.getElement2(), getSize());
            }
        }
    }

    private void createNodesByIndex() {
        nodesByIndex = new String[indexByNodes.size()];
        for (String key : indexByNodes.keySet()) {
            nodesByIndex[indexByNodes.get(key)] = key;
        }
    }

    private void setEdges(List<Tuple> tuples) {
        edges = new boolean[getSize()][getSize()];
        for (Tuple tuple : tuples) {
            int i = indexByNodes.get(tuple.getElement1());
            int j = indexByNodes.get(tuple.getElement2());
            edges[i][j] = true;
        }
    }

    private int getSize() {
        return indexByNodes.size();
    }

    public List<String> getParentNodes(String node) {
        List<String> list = new ArrayList<>();
        int i = indexByNodes.get(node);
        for (int j = 0; j < getSize(); j++) {
            if (edges[j][i]) {
                list.add(nodesByIndex[j]);
            }
        }
        return list;
    }

    public List<String> getNodeNames() {
        // Return a list of node names EXCLUDING the NULL node.
        List<String> nodeNames = new LinkedList<>();
        for (String str : nodesByIndex) {
            if (!str.equals("NULL")) {
                nodeNames.add(str);
            }
        }
        return nodeNames;
    }

}
