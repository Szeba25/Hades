package hu.szeba.hades.wizard.view.components;

import hu.szeba.hades.main.view.elements.MappedElement;
import hu.szeba.hades.wizard.view.elements.GraphNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class GraphCanvas extends JPanel {

    private JList<MappedElement> possibleNodes;

    private Map<String, GraphNode> nodes;
    private MappedElement currentNodeDescription;

    private long delayTime;
    private long dragDelayedUntil;

    public GraphCanvas(JList<MappedElement> possibleNodes) {
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

                    if (!changeSelection(e.getX(), e.getY())) {
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
        for (GraphNode node : nodes.values()) {
            if ((currentNodeDescription == null || !node.getDescription().getId().equals(currentNodeDescription.getId())) && node.isMouseInside(x, y)) {
                possibleNodes.setSelectedValue(node.getDescription(), true);
                currentNodeDescription = node.getDescription();
                return true;
            }
        }
        return false;
    }

    private void relocateOrPutNode(MappedElement nodeDescription, int x, int y) {
        if (nodeDescription != null) {
            if (nodes.containsKey(nodeDescription.getId())) {
                GraphNode node = nodes.get(nodeDescription.getId());
                node.getLocation().setLocation(x, y);
            } else {
                nodes.put(nodeDescription.getId(), new GraphNode(nodeDescription, new Point(x, y)));
            }
        }
    }

    private void addConnectionFromNode(MappedElement nodeDescription, int x, int y) {
        if (nodeDescription != null && nodes.containsKey(nodeDescription.getId())) {
            GraphNode sourceNode = nodes.get(nodeDescription.getId());
            for (GraphNode destNode : nodes.values()) {
                if (destNode.isMouseInside(x, y)) {
                    if (sourceNode.hasConnectionTo(destNode)) {
                        nodes.get(nodeDescription.getId()).removeConnection(destNode);
                    } else {
                        nodes.get(nodeDescription.getId()).addConnection(destNode);
                    }
                    break;
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
            n.paintNode(g2, currentNodeDescription != null && n.getDescription().getId().equals(currentNodeDescription.getId()));
        }

        for (GraphNode n : nodes.values()) {
            n.paintConnections(g2);
        }

    }

    public void setSelectedNode(MappedElement currentNodeDescription) {
        this.currentNodeDescription = currentNodeDescription;
        repaint();
    }

    public Map<String, GraphNode> getNodes() {
        return nodes;
    }

}
