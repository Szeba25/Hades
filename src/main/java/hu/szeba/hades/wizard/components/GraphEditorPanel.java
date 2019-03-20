package hu.szeba.hades.wizard.components;

import hu.szeba.hades.util.GridBagSetter;

import javax.swing.*;
import java.awt.*;

public class GraphEditorPanel extends JPanel {

    private JList<String> possibleNodes;
    private PlusMinusPanel possibleNodesAdder;
    private GraphCanvas canvas;

    public GraphEditorPanel(String title) {
        this.setLayout(new GridBagLayout());

        possibleNodes = new JList<>();
        possibleNodes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        possibleNodes.setFixedCellWidth(200);
        possibleNodes.setModel(new DefaultListModel<>());
        JScrollPane possibleNodesScroll = new JScrollPane(possibleNodes);
        possibleNodesScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        possibleNodesAdder = new PlusMinusPanel();

        // Testing
        DefaultListModel<String> model = (DefaultListModel<String>) possibleNodes.getModel();
        model.addElement("Test 1");
        model.addElement("Test 2");
        model.addElement("Test 3");
        model.addElement("Test 4");
        model.addElement("Test 5");
        model.addElement("Test 6");

        canvas = new GraphCanvas(possibleNodes);

        setupEvents();

        GridBagSetter gs = new GridBagSetter();
        gs.setComponent(this);

        gs.add(new JLabel(title),
                0,
                0,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                0,
                new Insets(5, 5, 0, 5));

        gs.add(possibleNodesScroll,
                0,
                1,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                1,
                new Insets(5, 5, 5, 5));

        gs.add(possibleNodesAdder,
                1,
                1,
                GridBagConstraints.NONE,
                1,
                1,
                0,
                1,
                new Insets(5, 0, 5, 5),
                GridBagConstraints.NORTH);

        gs.add(new JLabel("Dependencies:"),
                2,
                0,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                0,
                new Insets(5, 5, 0, 5));

        gs.add(canvas,
                2,
                1,
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

        possibleNodesAdder.getMinus().addActionListener((event) -> {
            canvas.deleteCurrentNode();
            DefaultListModel<String> model = (DefaultListModel<String>) possibleNodes.getModel();
            model.remove(possibleNodes.getSelectedIndex());
        });
    }

    public JButton getAddNodeButton() {
        return possibleNodesAdder.getPlus();
    }

}
