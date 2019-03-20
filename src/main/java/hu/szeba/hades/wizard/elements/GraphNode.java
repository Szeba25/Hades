package hu.szeba.hades.wizard.elements;

import java.awt.*;
import java.util.*;
import java.util.List;

public class GraphNode {

    private String name;
    private Point location;
    private Map<String, GraphNode> connections;
    private Color color;

    public GraphNode(String name, Point location) {
        this.name = name;
        this.location = location;
        this.connections = new HashMap<>();

        Random random = new Random();
        this.color = new Color(random.nextInt(160), random.nextInt(160), random.nextInt(160));
    }

    public String getName() {
        return name;
    }

    public Point getLocation() {
        return location;
    }

    public void addConnection(GraphNode node) {
        if (!connections.containsKey(node.getName()) && !node.hasConnectionTo(this) && node != this) {
            connections.put(node.getName(), node);
        }
    }

    public void removeConnection(GraphNode node) {
        if (connections.containsKey(node.getName()) && this.hasConnectionTo(node) && node != this) {
            connections.remove(node.getName());
        }
    }

    public boolean hasConnectionTo(GraphNode node) {
        return (connections.containsKey(node.getName()));
    }

    public boolean isMouseInside(int x, int y) {
        return Math.abs(x - location.x) < 20 && Math.abs(y - location.y) < 20;
    }

    public void paintNode(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.drawString(name, location.x, location.y - 10);
        g2.setColor(color);
        g2.fillOval(location.x - 10, location.y - 10, 20, 20);
    }

    public void paintConnections(Graphics2D g2) {
        for (GraphNode n : connections.values()) {

            g2.setColor(Color.RED);
            int x1 = location.x;
            int y1 = location.y;
            int x2 = n.location.x;
            int y2 = n.location.y;

            double dx_s = x2 - x1;
            double dy_s = y2 - y1;
            double length = Math.sqrt(dx_s * dx_s + dy_s * dy_s);
            if (length > 0)
            {
                dx_s /= length;
                dy_s /= length;
            }
            dx_s *= length - 10; // radius
            dy_s *= length - 10; // radius
            x2 = (int)(x1 + dx_s); // x3
            y2 = (int)(y1 + dy_s); // y3

            int d = 5;
            int h = 5;

            int dx = x2 - x1, dy = y2 - y1;
            double D = Math.sqrt(dx*dx + dy*dy);
            double xm = D - d, xn = xm, ym = h, yn = -h, x;
            double sin = dy / D, cos = dx / D;

            x = xm*cos - ym*sin + x1;
            ym = xm*sin + ym*cos + y1;
            xm = x;

            x = xn*cos - yn*sin + x1;
            yn = xn*sin + yn*cos + y1;
            xn = x;

            int[] xpoints = {x2, (int) xm, (int) xn};
            int[] ypoints = {y2, (int) ym, (int) yn};

            g2.drawLine(x1, y1, x2, y2);
            g2.setColor(Color.MAGENTA);
            g2.fillPolygon(xpoints, ypoints, 3);
        }
    }

}
