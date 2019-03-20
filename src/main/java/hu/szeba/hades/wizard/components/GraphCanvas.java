package hu.szeba.hades.wizard.components;

import hu.szeba.hades.wizard.elements.DescriptiveElement;
import hu.szeba.hades.wizard.elements.GraphNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class GraphCanvas extends JPanel {

    private JList<DescriptiveElement> possibleNodes;

    private Map<String, GraphNode> nodes;
    private DescriptiveElement currentNodeDescription;

    private long delayTime;
    private long dragDelayedUntil;

    public GraphCanvas(JList<DescriptiveElement> possibleNodes) {
        this.setBorder(BorderFactory.createEtchedBorder());
        this.setBackground(Color.WHITE);

        this.possibleNodes = possibleNodes;

        nodes = new HashMap<>();
        currentNodeDescription = null;
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
                        relocateOrPutNode(currentNodeDescription, e.getX(), e.getY());
                    }

                } else if (SwingUtilities.isRightMouseButton(e)) {
                    addConnectionFromNode(currentNodeDescription, e.getX(), e.getY());
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
                        relocateOrPutNode(currentNodeDescription, e.getX(), e.getY());
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
                possibleNodes.clearSelection();
            }
        });
    }

    private boolean changeSelection(int x, int y) {
        for (GraphNode n : nodes.values()) {
            if (n.getDescription() != currentNodeDescription && n.isMouseInside(x, y)) {
                possibleNodes.setSelectedValue(n.getDescription(), true);
                currentNodeDescription = n.getDescription();
                return true;
            }
        }
        return false;
    }

    private void relocateOrPutNode(DescriptiveElement nodeDescription, int x, int y) {
        if (nodeDescription != null) {
            if (nodes.containsKey(nodeDescription.getId())) {
                GraphNode node = nodes.get(nodeDescription.getId());
                node.getLocation().setLocation(x, y);
            } else {
                nodes.put(nodeDescription.getId(), new GraphNode(nodeDescription, new Point(x, y)));
            }
        }
    }

    private void addConnectionFromNode(DescriptiveElement nodeDescription, int x, int y) {
        if (nodeDescription != null) {
            if (nodes.containsKey(nodeDescription.getId())) {
                for (GraphNode n : nodes.values()) {
                    if (n.isMouseInside(x, y)) {
                        GraphNode node = nodes.get(nodeDescription.getId());
                        if (node.hasConnectionTo(n)) {
                            nodes.get(nodeDescription.getId()).removeConnection(n);
                        } else {
                            nodes.get(nodeDescription.getId()).addConnection(n);
                        }
                        break;
                    }
                }
            }
        }
    }

    public void deleteCurrentNode() {
        if (currentNodeDescription != null && nodes.containsKey(currentNodeDescription.getId())) {
            GraphNode removedNode = nodes.remove(currentNodeDescription.getId());
            for (GraphNode n : nodes.values()) {
                n.removeConnection(removedNode);
            }
            currentNodeDescription = null;
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (GraphNode n : nodes.values()) {
            n.paintNode(g2, n.getDescription() == currentNodeDescription);
        }

        for (GraphNode n : nodes.values()) {
            n.paintConnections(g2);
        }

    }

    public void setSelectedNode(DescriptiveElement currentNodeName) {
        this.currentNodeDescription = currentNodeName;
        repaint();
    }

}
