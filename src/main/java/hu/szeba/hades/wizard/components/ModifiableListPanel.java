package hu.szeba.hades.wizard.components;

import hu.szeba.hades.util.GridBagSetter;
import hu.szeba.hades.view.elements.MappedElement;

import javax.swing.*;
import java.awt.*;

public class ModifiableListPanel extends JPanel {

    private JList<MappedElement> list;
    private AddEditDeletePanel modifier;

    public ModifiableListPanel(String title) {
        this.setLayout(new GridBagLayout());
        this.setBorder(BorderFactory.createEtchedBorder());

        JLabel label = new JLabel(title);

        list = new JList<>();
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScroll = new JScrollPane(list);
        listScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        modifier = new AddEditDeletePanel();

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

        gs.add(listScroll,
                0,
                1,
                GridBagConstraints.BOTH,
                1,
                1,
                0.7,
                1,
                new Insets(0, 5, 5, 5));

        gs.add(modifier,
                1,
                1,
                GridBagConstraints.BOTH,
                1,
                1,
                0.3,
                1,
                new Insets(0, 5, 5, 5));
    }

    public JList<MappedElement> getList() {
        return list;
    }

    public AddEditDeletePanel getModifier() {
        return modifier;
    }

}
