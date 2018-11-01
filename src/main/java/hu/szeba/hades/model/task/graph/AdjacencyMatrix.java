package hu.szeba.hades.model.task.graph;

import java.util.*;
import java.util.stream.Collectors;

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
        indexByNodes.forEach(this::putToNodesByIndex);
    }

    private void putToNodesByIndex(String name, int i) {
        nodesByIndex[i] = name;
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

    public void printConnectedNodes(String node) {
        System.out.println("Nodes connected to " + node + ":");
        int i = indexByNodes.get(node);
        for (int j = 0; j < getSize(); j++) {
            if (edges[i][j]) {
                System.out.println("* " + nodesByIndex[j]);
            }
        }
    }

    public List<String> getNodeNames() {
        // Return a list of node names EXCLUDING the NULL node.
        // Filter the list using the stream API
        return Arrays.stream(nodesByIndex).filter((s) -> !s.equals("NULL")).collect(Collectors.toList());
    }

    private String edgeValueToPrintValue(boolean edgeValue) {
        if (edgeValue)
            return "1";
        else
            return "0";
    }

}
