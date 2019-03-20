package hu.szeba.hades.wizard.components;

import hu.szeba.hades.util.GridBagSetter;

import javax.swing.*;
import java.awt.*;

public class GraphEditorPanel extends JPanel {

    private JList<String> possibleNodes;
    private GraphCanvas canvas;

    public GraphEditorPanel() {
        this.setLayout(new GridBagLayout());

        possibleNodes = new JList<>();
        possibleNodes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        possibleNodes.setFixedCellWidth(200);
        possibleNodes.setModel(new DefaultListModel<>());
        JScrollPane possibleNodesScroll = new JScrollPane(possibleNodes);
        possibleNodesScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Testing
        DefaultListModel<String> model = (DefaultListModel<String>) possibleNodes.getModel();
        model.addElement("Test 1");
        model.addElement("Test 2");
        model.addElement("Test 3");
        model.addElement("Test 4");
        model.addElement("Test 5");
        model.addElement("Test 6");

        canvas = new GraphCanvas();

        setupEvents();

        GridBagSetter gs = new GridBagSetter();
        gs.setComponent(this);

        gs.add(possibleNodesScroll,
                0,
                0,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                1,
                new Insets(5, 5, 5, 5));

        gs.add(canvas,
                1,
                0,
                GridBagConstraints.BOTH,
                1,
                1,
                1,
                1,
                new Insets(5, 0, 5, 5));
    }

    private void setupEvents() {
        possibleNodes.getSelectionModel().addListSelectionListener((event) -> {
            ListSelectionModel listSelectionModel = (ListSelectionModel) event.getSource();
            ListModel listModel = possibleNodes.getModel();
            if (!listSelectionModel.isSelectionEmpty()) {
                int idx = listSelectionModel.getMinSelectionIndex();
                if (listSelectionModel.isSelectedIndex(idx)) {
                    String element = (String) listModel.getElementAt(idx);
                    canvas.setSelectedNode(element);
                }
            }
        });
    }

}
