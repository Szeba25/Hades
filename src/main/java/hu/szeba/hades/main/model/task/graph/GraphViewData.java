package hu.szeba.hades.main.model.task.graph;

import hu.szeba.hades.wizard.view.elements.GraphNode;

public class GraphViewData {

    private String name;
    private int r;
    private int g;
    private int b;
    private int x;
    private int y;

    public GraphViewData(GraphNode node) {
        name = node.getDescription().getId();
        r = node.getColor().getRed();
        g = node.getColor().getGreen();
        b = node.getColor().getBlue();
        x = node.getLocation().x;
        y = node.getLocation().y;
    }

    public GraphViewData(String[] data) {
        name = data[0];
        r = Integer.parseInt(data[1]);
        g = Integer.parseInt(data[2]);
        b = Integer.parseInt(data[3]);
        x = Integer.parseInt(data[4]);
        y = Integer.parseInt(data[5]);
    }

    public String getName() {
        return name;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return name + "," + r + "," + g + "," + b + "," + x + "," + y;
    }

}
