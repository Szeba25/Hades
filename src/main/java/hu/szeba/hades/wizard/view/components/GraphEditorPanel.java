package hu.szeba.hades.wizard.view.components;

import hu.szeba.hades.main.model.task.graph.Graph;
import hu.szeba.hades.main.model.task.graph.Tuple;
import hu.szeba.hades.main.util.GridBagSetter;
import hu.szeba.hades.main.view.elements.MappedElement;
import hu.szeba.hades.wizard.view.elements.DescriptiveElement;
import hu.szeba.hades.wizard.view.elements.GraphViewNode;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

public class GraphEditorPanel extends JPanel {

    private DynamicButtonListPanel possibleNodesPanel;
    private GraphCanvas canvas;

    private JButton dataPreviewButton;
    private JDialog dataPreview;
    private JTextArea dataArea;

    public GraphEditorPanel(String title, int width, int height) {
        this.setLayout(new GridBagLayout());

        possibleNodesPanel = new DynamicButtonListPanel(title, 200, "+", "-");

        // TEST
        DefaultListModel<MappedElement> model = (DefaultListModel<MappedElement>) possibleNodesPanel.getList().getModel();
        model.addElement(new DescriptiveElement("0000", "Title 0"));
        model.addElement(new DescriptiveElement("0001", "Title 1"));
        model.addElement(new DescriptiveElement("0002", "Title 2"));
        model.addElement(new DescriptiveElement("0003", "Title 3"));
        model.addElement(new DescriptiveElement("0004", "Title 4"));
        model.addElement(new DescriptiveElement("0005", "Title 5"));
        model.addElement(new DescriptiveElement("0006", "Title 6"));
        model.addElement(new DescriptiveElement("0007", "Title 7"));
        model.addElement(new DescriptiveElement("0008", "Title 8"));
        model.addElement(new DescriptiveElement("0009", "Title 9"));
        // TEST

        canvas = new GraphCanvas(possibleNodesPanel.getList());
        canvas.setPreferredSize(new Dimension(width, height));
        JScrollPane canvasScroller = new JScrollPane(canvas);
        canvasScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        canvasScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        canvasScroller.setPreferredSize(new Dimension(0, 0));
        canvasScroller.getVerticalScrollBar().setUnitIncrement(10);
        canvasScroller.getHorizontalScrollBar().setUnitIncrement(10);

        dataPreviewButton = new JButton("Preview graph data");
        dataPreviewButton.setFocusPainted(false);

        dataPreview = new JDialog();
        dataPreview.setMinimumSize(new Dimension(400, 400));
        dataPreview.setLayout(new BorderLayout());
        dataPreview.setModal(true);
        dataPreview.setTitle("Preview graph data");

        dataArea = new JTextArea();
        dataArea.setEditable(false);
        JScrollPane dataAreaScroll = new JScrollPane(dataArea);
        dataAreaScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        dataPreview.getContentPane().add(dataAreaScroll, BorderLayout.CENTER);
        dataPreview.pack();

        setupEvents();

        GridBagSetter gs = new GridBagSetter();
        gs.setComponent(this);

        gs.add(possibleNodesPanel,
                0,
                0,
                GridBagConstraints.BOTH,
                1,
                3,
                0,
                1,
                new Insets(0, 0, 0, 10));

        gs.add(new JLabel("Dependencies:"),
                1,
                0,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                0,
                new Insets(5, 5, 0, 5));

        gs.add(canvasScroller,
                1,
                1,
                GridBagConstraints.BOTH,
                1,
                1,
                1,
                1,
                new Insets(5, 5, 5, 5));

        gs.add(dataPreviewButton,
                1,
                2,
                GridBagConstraints.NONE,
                1,
                1,
                0,
                0,
                new Insets(0, 0, 5, 5),
                GridBagConstraints.SOUTHEAST);
    }

    private void setupEvents() {
        possibleNodesPanel.getList().getSelectionModel().addListSelectionListener((event) -> {
            ListSelectionModel listSelectionModel = (ListSelectionModel) event.getSource();
            ListModel listModel = possibleNodesPanel.getList().getModel();
            if (!listSelectionModel.isSelectionEmpty()) {
                int idx = listSelectionModel.getMinSelectionIndex();
                if (listSelectionModel.isSelectedIndex(idx)) {
                    MappedElement element = (MappedElement) listModel.getElementAt(idx);
                    canvas.setSelectedNode(element);
                }
            }
        });

        possibleNodesPanel.getModifier().getButton(1).addActionListener((event) -> {
            if (!possibleNodesPanel.getList().isSelectionEmpty()) {
                canvas.deleteCurrentNode();
                DefaultListModel<MappedElement> model = (DefaultListModel<MappedElement>) possibleNodesPanel.getList().getModel();
                model.remove(possibleNodesPanel.getList().getSelectedIndex());
            }
        });

        dataPreviewButton.addActionListener((event) -> {
            dataArea.setText(buildGraphStructureString());
            dataPreview.setLocationRelativeTo(null);
            dataPreview.setVisible(true);
        });
    }

    public JButton getAddNodeButton() {
        return possibleNodesPanel.getModifier().getButton(0);
    }

    public String buildGraphStructureString() {
        List<Tuple> tuples = buildTuples();

        StringBuilder builder = new StringBuilder();
        for (Tuple t : tuples) {
            builder.append(t.toString());
            builder.append("\n");
        }

        return builder.toString();
    }

    public List<Tuple> buildTuples() {
        List<Tuple> tuples = new ArrayList<>();
        Set<String> nodeNames = new HashSet<>();

        // Add by graph connections
        for (GraphViewNode viewNode : canvas.getViewNodes().values()) {
            if (viewNode.getConnections().size() > 0) {
                for (GraphViewNode connection : viewNode.getConnections().values()) {
                    tuples.add(new Tuple(viewNode.getDescription().getId(), connection.getDescription().getId()));
                    nodeNames.add(viewNode.getDescription().getId());
                    nodeNames.add(connection.getDescription().getId());
                }
            } else {
                tuples.add(new Tuple(viewNode.getDescription().getId(), "NULL"));
                nodeNames.add(viewNode.getDescription().getId());
            }
        }

        // Add remaining nodes
        DefaultListModel<MappedElement> model = (DefaultListModel<MappedElement>) possibleNodesPanel.getList().getModel();
        for (int i = 0; i < model.getSize(); i++) {
            MappedElement element = model.getElementAt(i);
            if (!nodeNames.contains(element.getId())) {
                tuples.add(new Tuple(element.getId(), "NULL"));
            }
        }

        tuples.sort(Comparator.comparing(Tuple::toString));

        return tuples;
    }

    public Map<String, GraphViewNode> shallowCopyViewNodes() {
        return new HashMap<>(canvas.getViewNodes());
    }

    public void setAllGraphData(Map<String, GraphViewNode> viewNodes, Graph graph, Map<String, String> idToTitleMapping) {
        // Clear the possible nodes list
        DefaultListModel<MappedElement> possibleNodesModel = (DefaultListModel<MappedElement>) possibleNodesPanel.getList().getModel();
        possibleNodesModel.removeAllElements();

        // Create the descriptive elements by ID, and add them to the view list
        for (String node : graph.getNodes()) {
            DescriptiveElement desc = new DescriptiveElement(node, idToTitleMapping.get(node));
            possibleNodesModel.addElement(desc);
        }

        // Clear the graph nodes from the canvas, and put new data!
        canvas.getViewNodes().clear();
        canvas.getViewNodes().putAll(viewNodes);

        // Reset current node
        canvas.setSelectedNode(null);
    }

}
