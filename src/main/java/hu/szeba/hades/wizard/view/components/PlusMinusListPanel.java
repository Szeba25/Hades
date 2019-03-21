package hu.szeba.hades.wizard.view.components;

import hu.szeba.hades.main.util.GridBagSetter;
import hu.szeba.hades.main.view.elements.MappedElement;

import javax.swing.*;
import java.awt.*;

public class PlusMinusListPanel extends JPanel {

    private JList<MappedElement> list;
    private PlusMinusPanel modifier;

    public PlusMinusListPanel(String title, int width) {
        this.setLayout(new GridBagLayout());

        JLabel label = new JLabel(title);

        list = new JList<>();
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setModel(new DefaultListModel<>());
        list.setFixedCellWidth(width);
        JScrollPane listScroll = new JScrollPane(list);
        listScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        modifier = new PlusMinusPanel();

        GridBagSetter gs = new GridBagSetter();
        gs.setComponent(this);

        gs.add(label,
                0,
                0,
                GridBagConstraints.BOTH,
                2,
                1,
                0,
                0,
                new Insets(5, 5, 5, 5));

        gs.add(modifier,
                0,
                1,
                GridBagConstraints.NONE,
                1,
                1,
                1,
                0,
                new Insets(0, 5, 5, 5),
                GridBagConstraints.WEST);

        gs.add(listScroll,
                0,
                2,
                GridBagConstraints.BOTH,
                1,
                1,
                1,
                1,
                new Insets(0, 5, 5, 5));
    }

    public JList<MappedElement> getList() {
        return list;
    }

    public PlusMinusPanel getModifier() {
        return modifier;
    }

}
