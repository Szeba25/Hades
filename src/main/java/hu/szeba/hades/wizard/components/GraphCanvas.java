package hu.szeba.hades.wizard.components;

import hu.szeba.hades.wizard.elements.GraphNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class GraphCanvas extends JPanel {

    private JList<String> possibleNodes;

    private Map<String, GraphNode> nodes;
    private String currentNodeName;

    private long delayTime;
    private long dragDelayedUntil;

    public GraphCanvas(JList<String> possibleNodes) {
        this.setBorder(BorderFactory.createEtchedBorder());
        this.setBackground(Color.WHITE);

        this.possibleNodes = possibleNodes;

        nodes = new HashMap<>();
        currentNodeName = null;
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
                if (SwingUtilities.isLeftMouseButton(e)) {

                    dragDelayedUntil = System.currentTimeMillis() + delayTime;

                    boolean selectionChange = changeSelection(e.getX(), e.getY());

                    if (!selectionChange) {
                        relocateOrPutNode(currentNodeName, e.getX(), e.getY());
                    }

                } else if (SwingUtilities.isRightMouseButton(e)) {
                    addConnectionFromNode(currentNodeName, e.getX(), e.getY());
                }

                // Finally repaint
                repaint();
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);

                if (System.currentTimeMillis() > dragDelayedUntil) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        relocateOrPutNode(currentNodeName, e.getX(), e.getY());
                    }
                }

                // Finally repaint
                repaint();
            }
        });

    }

    private void setupKeyBindings() {
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DELETE"), "delete node");
        this.getActionMap().put("delete node", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCurrentNode();
            }
        });
    }

    private boolean changeSelection(int x, int y) {
        for (GraphNode n : nodes.values()) {
            if (!n.getName().equals(currentNodeName) && n.isMouseInside(x, y)) {
                GraphNode gn = nodes.get(n.getName());
                possibleNodes.setSelectedValue(gn.getName(), true);
                currentNodeName = gn.getName();
                return true;
            }
        }
        return false;
    }

    private void relocateOrPutNode(String nodeName, int x, int y) {
        if (nodeName != null) {
            if (nodes.containsKey(nodeName)) {
                GraphNode node = nodes.get(nodeName);
                node.getLocation().setLocation(x, y);
            } else {
                nodes.put(nodeName, new GraphNode(nodeName, new Point(x, y)));
            }
        }
    }

    private void addConnectionFromNode(String nodeName, int x, int y) {
        if (nodeName != null) {
            if (nodes.containsKey(nodeName)) {
                for (GraphNode n : nodes.values()) {
                    if (n.isMouseInside(x, y)) {
                        GraphNode gn = nodes.get(nodeName);
                        if (gn.hasConnectionTo(n)) {
                            nodes.get(nodeName).removeConnection(n);
                        } else {
                            nodes.get(nodeName).addConnection(n);
                        }
                        break;
                    }
                }
            }
        }
    }

    public void deleteCurrentNode() {
        if (currentNodeName != null) {
            GraphNode removedNode = nodes.remove(currentNodeName);
            for (GraphNode n : nodes.values()) {
                n.removeConnection(removedNode);
            }
            possibleNodes.clearSelection();
            currentNodeName = null;
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (GraphNode n : nodes.values()) {
            n.paintNode(g2, n.getName().equals(currentNodeName));
        }

        for (GraphNode n : nodes.values()) {
            n.paintConnections(g2);
        }

    }

    public void setSelectedNode(String currentNode) {
        this.currentNodeName = currentNode;
        repaint();
    }

}
