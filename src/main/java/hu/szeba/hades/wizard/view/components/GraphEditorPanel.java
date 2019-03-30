package hu.szeba.hades.wizard.view.components;

import hu.szeba.hades.main.model.task.graph.Graph;
import hu.szeba.hades.main.util.GridBagSetter;
import hu.szeba.hades.main.util.SortUtilities;
import hu.szeba.hades.main.view.elements.MappedElement;
import hu.szeba.hades.wizard.view.elements.DescriptiveElement;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GraphEditorPanel extends JPanel {

    private DynamicButtonListPanel possibleNodesPanel;
    private Graph graph;
    private GraphCanvas canvas;

    private JButton dataPreviewButton;
    private JDialog dataPreview;
    private JTextArea dataArea;

    public GraphEditorPanel(String title, int width, int height) {
        this.setLayout(new GridBagLayout());

        possibleNodesPanel = new DynamicButtonListPanel(title, 200, "+", "-");

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
                    canvas.setSelectedNode(element.getId());
                }
            }
        });

        possibleNodesPanel.getModifier().getButton(1).addActionListener((event) -> {
            if (!possibleNodesPanel.getList().isSelectionEmpty()) {
                canvas.deleteCurrentNode();
                graph.removeNode(possibleNodesPanel.getList().getSelectedValue().getId());
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
        StringBuilder builder = new StringBuilder();
        for (String node : graph.getNodes()) {
            builder.append(node);
            for (String child : graph.getChildNodes(node)) {
                builder.append("|");
                builder.append(child);
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public void setGraphData(Graph graph, Map<String, String> idToTitleMap) {
        // Set graph
        this.graph = graph;

        // Clear the possible nodes list
        DefaultListModel<MappedElement> possibleNodesModel = (DefaultListModel<MappedElement>) possibleNodesPanel.getList().getModel();
        possibleNodesModel.removeAllElements();

        // Create the descriptive elements by ID, and add them to the view list. Sort them by id first!
        List<DescriptiveElement> possibleNodes = new ArrayList<>();
        for (String node : graph.getNodes()) {
            possibleNodes.add(new DescriptiveElement(node, idToTitleMap.get(node)));
        }
        possibleNodes.sort(SortUtilities::mappedElementIntegerComparator);
        for (DescriptiveElement desc : possibleNodes) {
            possibleNodesModel.addElement(desc);
        }

        // Set canvas graph
        canvas.setGraph(graph, idToTitleMap);

        // Reset current node
        canvas.setSelectedNode(null);
    }

    public void addNodes(List<MappedElement> selections) {
        DefaultListModel<MappedElement> possibleNodesModel = (DefaultListModel<MappedElement>) possibleNodesPanel.getList().getModel();

        List<DescriptiveElement> possibleNodes = new ArrayList<>();
        for (int i = 0; i < possibleNodesModel.size(); i++) {
            possibleNodes.add((DescriptiveElement) possibleNodesModel.getElementAt(i));
        }

        for (MappedElement element : selections) {
            if (!graph.containsNode(element.getId())) {
                graph.addNode(element.getId());
                possibleNodes.add((DescriptiveElement) element);
            }
        }
        possibleNodes.sort(SortUtilities::mappedElementIntegerComparator);

        possibleNodesModel.removeAllElements();
        for (DescriptiveElement desc : possibleNodes) {
            possibleNodesModel.addElement(desc);
        }

        canvas.setSelectedNode(null);
    }
}
