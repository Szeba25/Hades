package hu.szeba.hades.wizard.components;

import hu.szeba.hades.wizard.elements.GraphNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphCanvas extends JPanel {

    private Map<String, GraphNode> nodes;
    private String currentNode;

    public GraphCanvas() {
        this.setBorder(BorderFactory.createEtchedBorder());
        this.setBackground(Color.WHITE);

        nodes = new HashMap<>();
        currentNode = null;

        this.setupMouseEvents();
    }

    private void setupMouseEvents() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (currentNode != null) {
                        if (nodes.containsKey(currentNode)) {
                            GraphNode node = nodes.get(currentNode);
                            node.getLocation().setLocation(e.getX(), e.getY());
                        } else {
                            nodes.put(currentNode, new GraphNode(currentNode, new Point(e.getX(), e.getY())));
                        }
                        repaint();
                    }
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    if (currentNode != null) {
                        if (nodes.containsKey(currentNode)) {
                            for (GraphNode n : nodes.values()) {
                                if (n.isMouseInside(e.getX(), e.getY())) {
                                    GraphNode gn = nodes.get(currentNode);
                                    if (gn.hasConnectionTo(n)) {
                                        nodes.get(currentNode).removeConnection(n);
                                    } else {
                                        nodes.get(currentNode).addConnection(n);
                                    }
                                    repaint();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (GraphNode n : nodes.values()) {
            n.paintNode(g2);
        }

        for (GraphNode n : nodes.values()) {
            n.paintConnections(g2);
        }

    }

    public void setSelectedNode(String currentNode) {
        this.currentNode = currentNode;
    }

}
