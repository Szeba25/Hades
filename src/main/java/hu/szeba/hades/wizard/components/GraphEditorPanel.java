package hu.szeba.hades.wizard.components;

import hu.szeba.hades.util.GridBagSetter;
import hu.szeba.hades.wizard.elements.DescriptiveElement;

import javax.swing.*;
import java.awt.*;

public class GraphEditorPanel extends JPanel {

    private JList<DescriptiveElement> possibleNodes;
    private PlusMinusPanel possibleNodesAdder;
    private GraphCanvas canvas;

    public GraphEditorPanel(String title, int width, int height) {
        this.setLayout(new GridBagLayout());

        possibleNodes = new JList<>();
        possibleNodes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        possibleNodes.setFixedCellWidth(200);
        possibleNodes.setModel(new DefaultListModel<>());
        JScrollPane possibleNodesScroll = new JScrollPane(possibleNodes);
        possibleNodesScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        possibleNodesAdder = new PlusMinusPanel();

        // Testing
        DefaultListModel<DescriptiveElement> model = (DefaultListModel<DescriptiveElement>) possibleNodes.getModel();
        model.addElement(new DescriptiveElement("0001", "Task 1"));
        model.addElement(new DescriptiveElement("0002", "Task 2"));
        model.addElement(new DescriptiveElement("0003", "Task 3"));
        model.addElement(new DescriptiveElement("0004", "Task 4"));
        model.addElement(new DescriptiveElement("0005", "Task 5"));
        model.addElement(new DescriptiveElement("0006", "Task 6"));

        canvas = new GraphCanvas(possibleNodes);
        canvas.setPreferredSize(new Dimension(width, height));
        JScrollPane canvasScroller = new JScrollPane(canvas);
        canvasScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        canvasScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        canvasScroller.setPreferredSize(new Dimension(0, 0));
        canvasScroller.getVerticalScrollBar().setUnitIncrement(10);
        canvasScroller.getHorizontalScrollBar().setUnitIncrement(10);

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

        gs.add(canvasScroller,
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
                    DescriptiveElement element = (DescriptiveElement) listModel.getElementAt(idx);
                    canvas.setSelectedNode(element);
                }
            }
        });

        possibleNodesAdder.getMinus().addActionListener((event) -> {
            if (!possibleNodes.isSelectionEmpty()) {
                canvas.deleteCurrentNode();
                DefaultListModel<DescriptiveElement> model = (DefaultListModel<DescriptiveElement>) possibleNodes.getModel();
                model.remove(possibleNodes.getSelectedIndex());
            }
        });
    }

    public JButton getAddNodeButton() {
        return possibleNodesAdder.getPlus();
    }

}
