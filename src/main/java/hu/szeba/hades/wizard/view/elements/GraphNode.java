package hu.szeba.hades.wizard.view.elements;

import hu.szeba.hades.main.view.elements.MappedElement;

import java.awt.*;
import java.util.*;

public class GraphNode {

    private MappedElement description;
    private int radius;
    private Point location;
    private Map<String, GraphNode> connections;
    private Color color;

    public GraphNode(MappedElement description, Point location) {
        this(description, -1, -1, -1, location);
    }

    public GraphNode(MappedElement description, int r, int g, int b, Point location) {
        this.description = description;
        this.radius = 12;
        this.location = location;
        this.connections = new HashMap<>();

        Random random = new Random();
        if (r == -1) {
            r = random.nextInt(160);
        }
        if (g == -1) {
            g = random.nextInt(160);
        }
        if (b == -1) {
            b = random.nextInt(160);
        }

        this.color = new Color(r, g, b);
    }

    public MappedElement getDescription() {
        return description;
    }

    public Point getLocation() {
        return location;
    }

    public void addConnection(GraphNode node) {
        if (!connections.containsKey(node.getDescription().getId()) && !node.hasConnectionTo(this) && node != this) {
            connections.put(node.getDescription().getId(), node);
        }
    }

    public void removeConnection(GraphNode node) {
        if (connections.containsKey(node.getDescription().getId()) && this.hasConnectionTo(node) && node != this) {
            connections.remove(node.getDescription().getId());
        }
    }

    public boolean hasConnectionTo(GraphNode node) {
        return (connections.containsKey(node.getDescription().getId()));
    }

    public boolean isMouseInside(int x, int y) {
        return Math.abs(x - location.x) < radius * 2 && Math.abs(y - location.y) < radius * 2;
    }

    public void paintNode(Graphics2D g2, boolean current) {
        if (current) {
            g2.setColor(Color.BLUE);
            g2.drawRect(location.x - radius - 4, location.y - radius - 4, radius *2 + 8, radius *2 + 8);
        }
        g2.setColor(Color.BLACK);
        g2.drawString(description.toString(), location.x, location.y - radius - 5);
        g2.setColor(color);
        g2.fillOval(location.x - radius, location.y - radius, radius *2, radius *2);
    }

    public void paintConnections(Graphics2D g2) {
        for (GraphNode connNode : connections.values()) {

            // Set initial locations
            int x1 = location.x;
            int y1 = location.y;
            int x2 = connNode.location.x;
            int y2 = connNode.location.y;


            // Put the line to (0, 0)
            double nx = x2 - x1;
            double ny = y2 - y1;

            // Get the vectors length
            double length = Math.sqrt(nx * nx + ny * ny);

            // Normalize the vector
            if (length > 0)
            {
                nx /= length;
                ny /= length;
            }

            // Shorten the vector by 10
            nx *= length - connNode.radius;
            ny *= length - connNode.radius;

            // Set the new destination coordinates
            x2 = (int)(x1 + nx); // x3
            y2 = (int)(y1 + ny); // y3

            // Get arrow head positions (math magic)
            int d = 6;
            int h = 6;
            int dx = x2 - x1, dy = y2 - y1;

            double D = Math.sqrt(dx*dx + dy*dy);
            double xm = D - d, xn = xm, ym = h, yn = -h, x;
            double sin = dy / D, cos = dx / D;

            x = xm * cos - ym * sin + x1;
            ym = xm * sin + ym * cos + y1;
            xm = x;

            x = xn * cos - yn * sin + x1;
            yn = xn * sin + yn * cos + y1;
            xn = x;

            int[] xpoints = {x2, (int) xm, (int) xn};
            int[] ypoints = {y2, (int) ym, (int) yn};

            // Final line
            g2.setColor(Color.RED);
            g2.drawLine(x1, y1, x2, y2);

            // Arrow head
            g2.setColor(Color.MAGENTA);
            g2.fillPolygon(xpoints, ypoints, 3);
        }
    }

    public Map<String, GraphNode> getConnections() {
        return connections;
    }

}