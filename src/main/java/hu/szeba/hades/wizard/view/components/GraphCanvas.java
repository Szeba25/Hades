package hu.szeba.hades.wizard.view.components;

import hu.szeba.hades.main.io.ViewNode;
import hu.szeba.hades.main.model.task.graph.AbstractGraph;
import hu.szeba.hades.main.view.elements.MappedElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

public class GraphCanvas extends JPanel {

    private JList<MappedElement> possibleNodes;

    private AbstractGraph graph;
    private Map<String, String> idToTitleMap;
    private String currentNode;

    private long delayTime;
    private long dragDelayedUntil;

    public GraphCanvas(JList<MappedElement> possibleNodes) {
        this.setBorder(BorderFactory.createEtchedBorder());
        this.setBackground(Color.WHITE);

        this.possibleNodes = possibleNodes;

        idToTitleMap = null;
        graph = null;
        currentNode = null;

        delayTime = 100;
        dragDelayedUntil = 0;

        this.setupMouseEvents();
        this.setupKeyBindings();
    }

    private void setupMouseEvents() {

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (graph != null) {
                    if (SwingUtilities.isLeftMouseButton(e)) {

                        dragDelayedUntil = System.currentTimeMillis() + delayTime;

                        if (!changeSelection(e.getX(), e.getY())) {
                            relocateOrPutNode(currentNode, e.getX(), e.getY());
                        }

                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        addConnectionFromNode(currentNode, e.getX(), e.getY());
                    }

                    // Finally repaint
                    repaint();
                }
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (graph != null) {
                    if (System.currentTimeMillis() > dragDelayedUntil) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            relocateOrPutNode(currentNode, e.getX(), e.getY());
                        }
                    }

                    // Finally repaint
                    repaint();
                }
            }
        });

    }

    private void setupKeyBindings() {
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DELETE"), "delete node");
        this.getActionMap().put("delete node", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (graph != null) {
                    deleteCurrentNode();
                    possibleNodes.clearSelection();
                }
            }
        });
    }

    private boolean changeSelection(int x, int y) {
        for (String node : graph.getNodes()) {
            ViewNode viewNode = graph.getViewNode(node);
            if (viewNode != null) {
                if (!viewNode.getId().equals(currentNode) && viewNode.isInside(x, y)) {
                    // Select this node in the list
                    DefaultListModel<MappedElement> model = (DefaultListModel<MappedElement>) possibleNodes.getModel();
                    for (int i = 0; i < model.getSize(); i++) {
                        if (model.get(i).getId().equals(viewNode.getId())) {
                            possibleNodes.setSelectedIndex(i);
                            break;
                        }
                    }
                    currentNode = viewNode.getId();
                    return true;
                }
            }
        }
        return false;
    }

    private void relocateOrPutNode(String node, int x, int y) {
        if (node != null) {
            ViewNode viewNode = graph.getViewNode(node);
            if (viewNode != null) {
                viewNode.setLocation(x, y);
            } else {
                graph.addViewNode(new ViewNode(node, new Point(x, y)));
            }
        }
    }

    private void addConnectionFromNode(String node, int x, int y) {
        ViewNode viewNode = graph.getViewNode(node);
        if (node != null && viewNode != null) {
            for (String destNode : graph.getNodes()) {
                ViewNode destViewNode = graph.getViewNode(destNode);
                if (destViewNode != null) {
                    if (destViewNode.isInside(x, y)) {
                        if (graph.getChildNodes(node).contains(destNode)) {
                            graph.removeConnection(node, destNode);
                        } else {
                            if (!node.equals(destNode) && !graph.getParentNodes(node).contains(destNode)) {
                                graph.addConnection(node, destNode);
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    public void deleteCurrentNode() {
        ViewNode viewNode = graph.getViewNode(currentNode);
        if (currentNode != null && viewNode != null) {
            graph.removeViewNode(currentNode);
            graph.removeAllConnectionFrom(currentNode);
            graph.removeAllConnectionTo(currentNode);
            currentNode = null;
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (graph != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for (String node : graph.getNodes()) {
                ViewNode viewNode = graph.getViewNode(node);
                if (viewNode != null) {
                    paintNode(g2, node, (currentNode != null && currentNode.equals(node)));
                }
            }

            for (String node : graph.getNodes()) {
                ViewNode viewNode = graph.getViewNode(node);
                if (viewNode != null) {
                    paintConnections(g2, node);
                }
            }
        }
    }

    private void paintNode(Graphics2D g2, String node, boolean current) {
        ViewNode viewNode = graph.getViewNode(node);
        if (viewNode != null) {
            Color color = viewNode.getColor();
            Point location = viewNode.getLocation();
            int radius = viewNode.getRadius();

            if (current) {
                g2.setColor(Color.BLUE);
                g2.drawRect(location.x - radius - 4, location.y - radius - 4, radius *2 + 8, radius *2 + 8);
            }
            g2.setColor(Color.BLACK);
            g2.drawString(node + ": " + idToTitleMap.get(node), location.x, location.y - radius - 5);
            g2.setColor(color);
            g2.fillOval(location.x - radius, location.y - radius, radius *2, radius *2);
        }
    }

    private void paintConnections(Graphics2D g2, String node) {
        ViewNode viewNode = graph.getViewNode(node);

        for (String child : graph.getChildNodes(node)) {
            ViewNode childViewNode = graph.getViewNode(child);

            if (viewNode != null && childViewNode != null) {
                // Get data
                Point location = viewNode.getLocation();
                Point destination = childViewNode.getLocation();
                int destinationRadius = childViewNode.getRadius();

                // Set initial locations
                int x1 = location.x;
                int y1 = location.y;
                int x2 = destination.x;
                int y2 = destination.y;

                // Put the line to (0, 0)
                double nx = x2 - x1;
                double ny = y2 - y1;

                // Get the vectors length
                double length = Math.sqrt(nx * nx + ny * ny);

                // Normalize the vector
                if (length > 0) {
                    nx /= length;
                    ny /= length;
                }

                // Shorten the vector by 10
                nx *= length - destinationRadius;
                ny *= length - destinationRadius;

                // Set the new destination coordinates
                x2 = (int) (x1 + nx); // x3
                y2 = (int) (y1 + ny); // y3

                // Get arrow head positions (math magic)
                int d = 6;
                int h = 6;
                int dx = x2 - x1, dy = y2 - y1;

                double D = Math.sqrt(dx * dx + dy * dy);
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
    }

    public void setSelectedNode(String currentNode) {
        this.currentNode = currentNode;
        repaint();
    }

    public void setGraph(AbstractGraph graph, Map<String, String> idToTitleMap) {
        this.graph = graph;
        this.idToTitleMap = idToTitleMap;
    }

}
