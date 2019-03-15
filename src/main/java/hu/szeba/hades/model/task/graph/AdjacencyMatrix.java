package hu.szeba.hades.model.task.graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AdjacencyMatrix {

    private Map<String, Integer> indexByNodes;
    private String[] nodesByIndex;
    private boolean[][] edges;

    public AdjacencyMatrix(String[] nodes) {
        for (int i = 0; i < nodes.length; i++) {
            this.indexByNodes.put(nodes[i], i);
        }
        nodesByIndex = nodes.clone();
        edges = new boolean[getSize()][getSize()];
    }

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

    public void print() {
        System.out.println("Nodes: ");
        for (int i = 0; i < nodesByIndex.length; i++) {
            System.out.println(i + ": " + nodesByIndex[i]);
        }
        System.out.println("Matrix: ");
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                System.out.print(edgeValueToPrintValue(edges[i][j]));
            }
            System.out.println();
        }
    }

    public void printChildNodes(String node) {
        System.out.println("Children of " + node + ":");
        int i = indexByNodes.get(node);
        for (int j = 0; j < getSize(); j++) {
            if (edges[i][j]) {
                System.out.println("* " + nodesByIndex[j]);
            }
        }
    }

    public List<String> getParentNodes(String node) {
        List<String> list = new LinkedList<>();
        int i = indexByNodes.get(node);
        for (int j = 0; j < getSize(); j++) {
            if (edges[j][i]) {
                list.add(nodesByIndex[j]);
            }
        }
        return list;
    }

    public void printParentNodes(String node) {
        System.out.println("Parents of " + node + ":");
        int i = indexByNodes.get(node);
        for (int j = 0; j < getSize(); j++) {
            if (edges[j][i]) {
                System.out.println("* " + nodesByIndex[j]);
            }
        }
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

    private String edgeValueToPrintValue(boolean edgeValue) {
        if (edgeValue)
            return "1";
        else
            return "0";
    }

}
